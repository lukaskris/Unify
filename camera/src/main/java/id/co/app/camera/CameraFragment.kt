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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import id.co.app.camera.R
import id.co.app.camera.databinding.LayoutImagePreviewBinding
import id.co.app.core.extension.gone
import id.co.app.core.extension.show
import id.co.app.core.extension.showErrorToast
import id.co.app.core.extension.showToast
import id.co.app.core.model.eventbus.DataEvent
import id.co.app.core.model.eventbus.EventBus
import dagger.hilt.android.AndroidEntryPoint
import id.co.app.camera.databinding.FragmentCameraBinding
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
	private var imageCapture: ImageCapture? = null
	private val binding by lazy { FragmentCameraBinding.inflate(layoutInflater) }
	private lateinit var outputDirectory: File
	private lateinit var cameraExecutor: ExecutorService
	private val pictureList = mutableListOf<File>()

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
			activity?.onBackPressed()
		}
		binding.okButton.setOnClickListener {
			eventBus.invokeEvent(DataEvent(pictureList))
			activity?.onBackPressed()
		}
		binding.captureButton.setOnClickListener {
			takePhoto()
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		cameraExecutor.shutdown()
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
				android.Manifest.permission.CAMERA,
				android.Manifest.permission.WRITE_EXTERNAL_STORAGE
			)
		}

	}

	private fun takePhoto() {
		binding.loadingCapture.show()
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
					context?.showErrorToast("Photo capture failed: ${exc.message}")
					binding.loadingCapture.gone()
				}

				override fun onImageSaved(output: ImageCapture.OutputFileResults) {
					val savedUri = Uri.fromFile(photoFile)
					val msg = "Photo capture succeeded: $savedUri"
					addPhotoList(photoFile)
					Log.d(TAG, msg)
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

			// Select back camera as a default
			val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

			try {
				// Unbind use cases before rebinding
				cameraProvider.unbindAll()

				// Bind use cases to camera
				cameraProvider.bindToLifecycle(
					this, cameraSelector, preview, imageCapture
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
		binding.loadingCapture.gone()
		val view = LayoutImagePreviewBinding.inflate(layoutInflater)
		view.imageView.setImageURI(Uri.fromFile(photo))
		view.root.setOnClickListener {
			context?.showToast(photo.name)
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
				context?.showErrorToast(
					"Permissions not granted by the user."
				)
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