package id.co.app.camera

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import id.co.app.camera.databinding.FragmentCameraBinding
import id.co.app.camera.databinding.LayoutImagePreviewBinding
import id.co.app.core.model.eventbus.DataEvent
import id.co.app.core.model.eventbus.EventBus
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject


/**
 * Created by Lukas Kristianto on 5/11/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
@AndroidEntryPoint
class CameraFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private val isMultiShotCamera by lazy { arguments?.getString("multishot").orEmpty() == "true" }
    private val isSelfieCamera by lazy { arguments?.getString("selfie").orEmpty() == "true" }
    private val isQrCode by lazy { arguments?.getString("scanner").orEmpty() == "true" }

    private val binding by lazy { FragmentCameraBinding.inflate(layoutInflater) }
    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private val pictureList = mutableListOf<File>()
    private var lensFacing = CameraSelector.DEFAULT_BACK_CAMERA
    private var isDisposeByAction = false

    private val scaleAnim by lazy { AnimatorSet() }

    private val options by lazy {
        BarcodeScannerOptions.Builder().setBarcodeFormats(
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_CODE_39,
            Barcode.FORMAT_CODE_93,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E,
            Barcode.FORMAT_PDF417
        ).build()
    }

    private val scanner by lazy { BarcodeScanning.getClient(options) }

    // setting up the analysis use case
    private val analysisUseCase by lazy {
        ImageAnalysis.Builder().build().apply {
            setAnalyzer(
                // newSingleThreadExecutor() will let us perform analysis on a single worker thread
                Executors.newSingleThreadExecutor()
            ) { imageProxy ->
                processImageProxy(scanner, imageProxy)
            }
        }
    }

    @Inject
    lateinit var eventBus: EventBus
//    var eventBus = EventBus()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        startAnimWhenScanner()
    }

    override fun onDestroy() {
        if (!isDisposeByAction) eventBus.invokeEvent(DataEvent(pictureList))
        cameraExecutor.shutdown()
        scaleAnim.cancel()
        super.onDestroy()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        startCamera()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    private fun initView() {
        isDisposeByAction = false
        lensFacing = if (!isSelfieCamera) CameraSelector.DEFAULT_BACK_CAMERA else
            CameraSelector.DEFAULT_FRONT_CAMERA
        requestPermissions()
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        binding.cancelButton.setOnClickListener {
            eventBus.invokeEvent(DataEvent(pictureList))
            isDisposeByAction = true
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                "back",
                true
            )
            activity?.onBackPressed()
        }
        binding.okButton.setOnClickListener {
            eventBus.invokeEvent(DataEvent(pictureList))
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                "pictures",
                pictureList
            )
            isDisposeByAction = true
            activity?.onBackPressed()
        }
        binding.swapCamera.setOnClickListener {
            lensFacing =
                if (lensFacing == CameraSelector.DEFAULT_FRONT_CAMERA) CameraSelector.DEFAULT_BACK_CAMERA else
                    CameraSelector.DEFAULT_FRONT_CAMERA
            startCamera()
        }
        binding.backButton.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                "back",
                true
            )
            activity?.onBackPressed()
        }
        binding.okButton.isVisible = isMultiShotCamera
        binding.captureButton.setOnClickListener {
            takePhoto()
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    private fun requestPermissions() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept camera permissions to use this app",
                REQUEST_CODE_PERMISSIONS,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    private fun takePhoto() {
        binding.loadingCapture.isVisible = true
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val timeDate =
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val photoFile = File(outputDirectory, "$timeDate.jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    Toasty.error(
                        requireContext(),
                        "Photo capture failed: ${exc.message}",
                        Toasty.LENGTH_LONG
                    ).show()
                    binding.loadingCapture.isVisible = false
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    lifecycleScope.launch {
                        val compressedImageFile = Compressor.compress(requireContext(), photoFile) {
                            default(width = 640, format = Bitmap.CompressFormat.WEBP, quality = 70)
                        }
                        photoFile.delete()
                        if (isMultiShotCamera) {
                            addPhotoList(compressedImageFile)
                        } else {
                            eventBus.invokeEvent(DataEvent(listOf(compressedImageFile)))
                            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                                "pictures",
                                listOf(compressedImageFile)
                            )
                            isDisposeByAction = true
                            activity?.onBackPressed()
                        }
                    }
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewCamera.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setTargetResolution(Size(600, 800))
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                if (!isQrCode) {
                    cameraProvider.bindToLifecycle(
                        this, lensFacing, preview, imageCapture
                    )
                } else {
                    cameraProvider.bindToLifecycle(
                        this, lensFacing, preview, imageCapture, analysisUseCase
                    )
                }

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun startAnimWhenScanner() {
        binding.isScanner = isQrCode
        if (isQrCode) {
            val scaleX = ObjectAnimator.ofFloat(binding.qrCode, "scaleX", 1f, 1.1f)
            val scaleY = ObjectAnimator.ofFloat(binding.qrCode, "scaleY", 1f, 1.1f)

            scaleX.repeatCount = ObjectAnimator.INFINITE
            scaleX.repeatMode = ObjectAnimator.REVERSE

            scaleY.repeatCount = ObjectAnimator.INFINITE
            scaleY.repeatMode = ObjectAnimator.REVERSE

            scaleAnim.duration = 800
            scaleAnim.play(scaleX).with(scaleY)

            scaleAnim.start()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun addPhotoList(photo: File) {
        binding.loadingCapture.isVisible = false
        val view = LayoutImagePreviewBinding.inflate(layoutInflater)
        view.imageView.setImageURI(Uri.fromFile(photo))
        view.root.setOnClickListener {
            Toasty.success(requireContext(), photo.name, Toasty.LENGTH_LONG).show()
        }
        view.closeView.setOnClickListener {
            pictureList.remove(photo)
            binding.samplePictureView.removeView(view.root)
        }
        pictureList.add(photo)
        binding.samplePictureView.addView(view.root)
    }

    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    ) {

        imageProxy.image?.let { image ->
            val inputImage =
                InputImage.fromMediaImage(
                    image,
                    imageProxy.imageInfo.rotationDegrees
                )

            val process = barcodeScanner.process(inputImage)

            process
                .addOnSuccessListener { barcodeList ->
                    val barcode = barcodeList.getOrNull(0)
                    // `rawValue` is the decoded value of the barcode
                    barcode?.rawValue?.let { value ->
                        // update our textView to show the decoded value
                        if (!isDisposeByAction) {
                            eventBus.invokeEvent(DataEvent(value))
                            isDisposeByAction = true
                            activity?.onBackPressed()
                        }
                    }
                }
                .addOnFailureListener {
                    // This failure will happen if the barcode scanning model
                    // fails to download from Google Play Services
                    Log.e(TAG, it.message.orEmpty())
                }.addOnCompleteListener {
                    // When the image is from CameraX analysis use case, must
                    // call image.close() on received images when finished
                    // using them. Otherwise, new images may not be received
                    // or the camera may stall.
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toasty.error(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toasty.LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}