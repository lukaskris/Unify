package id.co.app.source.ui.downloader

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import id.co.app.lsdownloader.core.LSDownloader
import id.co.app.lsdownloader.core.base.*
import id.co.app.lsdownloader.model.*
import id.co.app.source.R
import id.co.app.source.databinding.ActivitySingleDownloadBinding
import timber.log.Timber
import java.text.DecimalFormat


/**
 * Created by Lukas Kristianto on 23/09/22 15.56.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
class SingleDownloadActivity : AppCompatActivity(), LSDownloaderObserver<Download> {

    private val binding by lazy { ActivitySingleDownloadBinding.inflate(layoutInflater) }
    private val lsDownloader by lazy { LSDownloader.Impl.getDefaultInstance() }
    private lateinit var request: Request

    override fun onCreate(savedInstanceState: Bundle?) {
        val fetchConfiguration = LSConfiguration.Builder(this)
            .enableRetryOnNetworkGain(true)
            .setDownloadConcurrentLimit(3)
            .setHttpDownloader(HttpUrlConnectionDownloader(Downloader.FileDownloaderType.PARALLEL)) // OR
            //.setHttpDownloader(getOkHttpDownloader())
            .build()
        LSDownloader.Impl.setDefaultInstanceConfiguration(fetchConfiguration)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkStoragePermission()
    }

    override fun onChanged(data: Download, reason: Reason) {
        updateViews(data, reason)
    }

    override fun onResume() {
        super.onResume()
        lsDownloader.attachFetchObserversForDownload(request.id, this)
    }
    override fun onPause() {
        super.onPause()
        lsDownloader.removeFetchObserversForDownload(request.id, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        lsDownloader.close()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enqueueDownload()
        } else {
            Snackbar.make(binding.root, "Permission not enabled", Snackbar.LENGTH_LONG).show()
        }
    }
    
    private fun checkStoragePermission() {
        requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }


    private fun enqueueDownload() {
        val url = "http://speedtest.ftp.otenet.gr/files/test100Mb.db"
        val filePath: String = getSaveDir() + "/movies/" + getNameFromUrl(url)
        request = Request(url, filePath)

        request.priority = Priority.HIGH
        request.networkType = NetworkType.ALL

        lsDownloader.attachFetchObserversForDownload(request.id, this)
            .enqueue(request,
                { result -> request = result }, {
                    Timber.d("SingleDownloadActivity Error: %1\$s", it.toString())
                })
    }

    private fun getExtrasForRequest(request: Request): Extras {
        val extras = MutableExtras()
        extras.putBoolean("testBoolean", true)
        extras.putString("testString", "test")
        extras.putFloat("testFloat", Float.MIN_VALUE)
        extras.putDouble("testDouble", Double.MIN_VALUE)
        extras.putInt("testInt", Int.MAX_VALUE)
        extras.putLong("testLong", Long.MAX_VALUE)
        return extras
    }

    private fun getSaveDir(): String {
        return getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/fetch"
    }

    private fun getNameFromUrl(url: String): String {
        return Uri.parse(url).lastPathSegment ?: ""
    }


    private fun updateViews(download: Download, reason: Reason) {
        if (request.id == download.id) {
            if (reason === Reason.DOWNLOAD_QUEUED || reason === Reason.DOWNLOAD_COMPLETED) {
                setTitleView(download.file)
            }
            setProgressView(download.status, download.progress)
            binding.etaTextView.text = getETAString(this, download.etaInMilliSeconds)
            binding.downloadSpeedTextView.text = getDownloadSpeedString(
                this,
                download.downloadedBytesPerSecond
            )
            if (download.lsDownloaderError !== LSDownloaderError.NONE) {
                showDownloadErrorSnackBar(download.lsDownloaderError)
            }
        }
    }

    private fun setTitleView(fileName: String) {
        val uri = Uri.parse(fileName)
        binding.titleTextView.setText(uri.lastPathSegment)
    }

    private fun setProgressView(status: Status, progress: Int) {
        when (status) {
            Status.QUEUED -> {
                binding.progressTextView.setText(R.string.queued)
            }
            Status.ADDED -> {
                binding.progressTextView.setText(R.string.added)
            }
            Status.DOWNLOADING, Status.COMPLETED -> {
                if (progress == -1) {
                    binding.progressTextView.setText(R.string.downloading)
                } else {
                    val progressString = resources.getString(R.string.percent_progress, progress)
                    binding.progressTextView.text = progressString
                }
            }
            else -> {
                binding.progressTextView.setText(R.string.status_unknown)
            }
        }
    }

    private fun showDownloadErrorSnackBar(error: LSDownloaderError) {
        val snackbar = Snackbar.make(
            binding.root,
            "Download Failed: ErrorCode: $error", Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction(R.string.retry) { v ->
            lsDownloader.retry(request.id)
            snackbar.dismiss()
        }
        snackbar.show()
    }

    private fun getETAString(context: Context, etaInMilliSeconds: Long): String {
        if (etaInMilliSeconds < 0) {
            return ""
        }
        var seconds = (etaInMilliSeconds / 1000).toInt()
        val hours = (seconds / 3600).toLong()
        seconds -= (hours * 3600).toInt()
        val minutes = (seconds / 60).toLong()
        seconds -= (minutes * 60).toInt()
        return if (hours > 0) {
            context.getString(R.string.download_eta_hrs, hours, minutes, seconds)
        } else if (minutes > 0) {
            context.getString(R.string.download_eta_min, minutes, seconds)
        } else {
            context.getString(R.string.download_eta_sec, seconds)
        }
    }

    private fun getDownloadSpeedString(context: Context, downloadedBytesPerSecond: Long): String? {
        if (downloadedBytesPerSecond < 0) {
            return ""
        }
        val kb = downloadedBytesPerSecond.toDouble() / 1000.0
        val mb = kb / 1000.0
        val decimalFormat = DecimalFormat(".##")
        return if (mb >= 1) {
            context.getString(R.string.download_speed_mb, decimalFormat.format(mb))
        } else if (kb >= 1) {
            context.getString(R.string.download_speed_kb, decimalFormat.format(kb))
        } else {
            context.getString(R.string.download_speed_bytes, downloadedBytesPerSecond)
        }
    }
    
    companion object {
        private const val STORAGE_PERMISSION_CODE = 100
    }
}