package id.co.app.lsdownloader.core.downloader

import id.co.app.lsdownloader.core.base.Download
import id.co.app.lsdownloader.core.base.DownloadBlock
import id.co.app.lsdownloader.core.database.DownloadInfo
import id.co.app.lsdownloader.model.LSDownloaderError

interface FileDownloader : Runnable {

    var interrupted: Boolean
    var terminated: Boolean
    val completedDownload: Boolean
    var delegate: Delegate?
    val download: Download

    interface Delegate {

        val interrupted: Boolean

        fun onStarted(download: Download, downloadBlocks: List<DownloadBlock>, totalBlocks: Int)

        fun onDownloadBlockUpdated(download: Download, downloadBlock: DownloadBlock, totalBlocks: Int)

        fun onProgress(download: Download, etaInMilliSeconds: Long, downloadedBytesPerSecond: Long)

        fun onError(download: Download, LSDownloaderError: LSDownloaderError, throwable: Throwable?)

        fun onComplete(download: Download)

        fun saveDownloadProgress(download: Download)

        fun getNewDownloadInfoInstance(): DownloadInfo

    }

}