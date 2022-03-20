package id.co.app.camera

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import id.co.app.camera.databinding.FragmentCameraBinding
import id.co.app.camera.databinding.LayoutImagePreviewBinding
import id.co.app.core.model.eventbus.DataEvent
import id.co.app.core.model.eventbus.EventBus
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
    private var imageCapture: ImageCapture? = null
    private val binding by lazy { FragmentCameraBinding.inflate(layoutInflater) }
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private val pictureList = mutableListOf<File>()
    private var lensFacing = CameraSelector.DEFAULT_BACK_CAMERA
    private var isDisposeByAction = false

    @Inject
    lateinit var eventBus: EventBus

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
        binding.cancelButton.setOnClickListener {
            eventBus.invokeEvent(DataEvent(pictureList))
            isDisposeByAction = true
            activity?.onBackPressed()
        }
        binding.okButton.setOnClickListener {
            eventBus.invokeEvent(DataEvent(pictureList))
            isDisposeByAction = true
            activity?.onBackPressed()
        }
        binding.swapCamera.setOnClickListener {
//            CameraX.getCameraWithCameraSelector(lensFacing)
            lensFacing =
                if (lensFacing == CameraSelector.DEFAULT_FRONT_CAMERA) CameraSelector.DEFAULT_BACK_CAMERA else
                    CameraSelector.DEFAULT_FRONT_CAMERA
            startCamera()
        }
        binding.okButton.isVisible = isMultiShotCamera
        binding.captureButton.setOnClickListener {
            takePhoto()
        }
    }

    override fun onDestroy() {
        if (!isDisposeByAction) eventBus.invokeEvent(DataEvent(pictureList))
        cameraExecutor.shutdown()
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
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

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
                    Toasty.error(requireContext(), "Photo capture failed: ${exc.message}", Toasty.LENGTH_LONG).show()
                    binding.loadingCapture.isVisible = false
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    if (isMultiShotCamera) {
                        addPhotoList(photoFile)
                    } else {
                        eventBus.invokeEvent(DataEvent(listOf(photoFile)))
                        isDisposeByAction = true
                        activity?.onBackPressed()
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
                cameraProvider.bindToLifecycle(
                    this, lensFacing, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toasty.error(requireContext(), "Permissions not granted by the user.", Toasty.LENGTH_LONG).show()
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