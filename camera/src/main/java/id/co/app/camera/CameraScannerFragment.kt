package id.co.app.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.zxing.Result
import dagger.hilt.android.AndroidEntryPoint
import id.co.app.core.model.eventbus.DataEvent
import id.co.app.core.model.eventbus.EventBus
import me.dm7.barcodescanner.zxing.ZXingScannerView
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class CameraScannerFragment : Fragment(), ZXingScannerView.ResultHandler, EasyPermissions.PermissionCallbacks {

    @Inject
    lateinit var eventBus: EventBus

    private val mScannerView: ZXingScannerView by lazy { ZXingScannerView(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mScannerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
    }

    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.setAutoFocus(true)
        mScannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        eventBus.invokeEvent(DataEvent(rawResult?.text.orEmpty()))

        mScannerView.stopCamera()
        findNavController().navigateUp()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        mScannerView.startCamera()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        if (allPermissionsGranted()) {
            mScannerView.startCamera()
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

    companion object{
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}