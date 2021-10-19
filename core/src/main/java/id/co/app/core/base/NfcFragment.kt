package id.co.app.core.base

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NfcManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import id.co.app.core.extension.showErrorToast
import id.co.app.core.extension.showInformationToast
import id.co.app.core.extension.showToast
import id.co.app.core.utilities.nfc.NfcUtils
import id.co.app.core.utilities.nfc.WritableTag


/**
 * Created by Lukas Kristianto on 08/09/21 22.25.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
abstract class NfcFragment : Fragment() {

    open val nfcManager by lazy { (requireContext().getSystemService(Context.NFC_SERVICE) as NfcManager) }
    open var tag: WritableTag? = null
    open var tagId: String? = null

    abstract fun onNewIntent(intent: Intent)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkNfc()
    }

    override fun onResume() {
        super.onResume()
        enableNfcForegroundDispatch()
    }

    override fun onPause() {
        disableNfcForegroundDispatch()
        super.onPause()
    }

    open fun enableNfcForegroundDispatch() {
        try {
            val nfcAdapter = nfcManager.defaultAdapter
            if (nfcAdapter != null) {
                if (!nfcAdapter.isEnabled)
                    showWirelessSettings()

                val intent =
                    Intent(
                        requireContext(),
                        requireActivity().javaClass
                    ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                val nfcPendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, 0)
                nfcAdapter.enableForegroundDispatch(requireActivity(), nfcPendingIntent, null, null)
            }
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
    }

    open fun showWirelessSettings() {
        requireContext().showInformationToast("You need to enable NFC")
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                checkNfc()
            }
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        resultLauncher.launch(intent)
    }

    open fun disableNfcForegroundDispatch() {
        try {
            val nfcAdapter = nfcManager.defaultAdapter
            nfcAdapter?.disableForegroundDispatch(requireActivity())
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
    }

    abstract fun writeNDefMessage(data: String): Boolean

    open fun checkNfc() {
        val nfcAdapter = nfcManager.defaultAdapter

        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            requireContext().showErrorToast("This device doesn't support NFC.")
        } else if (!nfcAdapter.isEnabled) {
            showWirelessSettings()
        }
    }
}