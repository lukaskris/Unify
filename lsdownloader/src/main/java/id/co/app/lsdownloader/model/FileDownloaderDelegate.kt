package id.co.app.lsdownloader.model

import id.co.app.lsdownloader.core.base.Download
import id.co.app.lsdownloader.core.base.DownloadBlock
import id.co.app.lsdownloader.core.base.LSDownloaderListener
import id.co.app.lsdownloader.core.database.DownloadInfo
import id.co.app.lsdownloader.core.downloader.FileDownloader
import id.co.app.lsdownloader.utils.DEFAULT_GLOBAL_AUTO_RETRY_ATTEMPTS
import id.co.app.lsdownloader.utils.defaultNoLSDownloaderError


class FileDownloaderDelegate(private val downloadInfoUpdater: DownloadInfoUpdater,
                             private val LSDownloaderListener: LSDownloaderListener,
                             private val retryOnNetworkGain: Boolean,
                             private val globalAutoRetryMaxAttempts: Int) : FileDownloader.Delegate {

    @Volatile
    override var interrupted = false

    override fun onStarted(download: Download, downloadBlocks: List<DownloadBlock>, totalBlocks: Int) {
        if (!interrupted) {
            val downloadInfo = download as DownloadInfo
            downloadInfo.status = Status.DOWNLOADING
            downloadInfoUpdater.update(downloadInfo)
            LSDownloaderListener.onStarted(download, downloadBlocks, totalBlocks)
        }
    }

    override fun onProgress(download: Download, etaInMilliSeconds: Long, downloadedBytesPerSecond: Long) {
        if (!interrupted) {
            LSDownloaderListener.onProgress(download, etaInMilliSeconds, downloadedBytesPerSecond)
        }
    }

    override fun onDownloadBlockUpdated(download: Download, downloadBlock: DownloadBlock, totalBlocks: Int) {
        if (!interrupted) {
            LSDownloaderListener.onDownloadBlockUpdated(download, downloadBlock, totalBlocks)
        }
    }

    override fun onError(download: Download, lsDownloaderError: LSDownloaderError, throwable: Throwable?) {
        if (!interrupted) {
            val maxAutoRetryAttempts = if (globalAutoRetryMaxAttempts != DEFAULT_GLOBAL_AUTO_RETRY_ATTEMPTS) {
                globalAutoRetryMaxAttempts
            } else {
                download.autoRetryMaxAttempts
            }
            val downloadInfo = download as DownloadInfo
            if (retryOnNetworkGain && downloadInfo.lsDownloaderError == LSDownloaderError.NO_NETWORK_CONNECTION) {
                downloadInfo.status = Status.QUEUED
                downloadInfo.lsDownloaderError = defaultNoLSDownloaderError
                downloadInfoUpdater.update(downloadInfo)
                LSDownloaderListener.onQueued(download, true)
            } else if (download.autoRetryAttempts < maxAutoRetryAttempts) {
                download.autoRetryAttempts += 1
                downloadInfo.status = Status.QUEUED
                downloadInfo.lsDownloaderError = defaultNoLSDownloaderError
                downloadInfoUpdater.update(downloadInfo)
                LSDownloaderListener.onQueued(download, true)
            } else {
                downloadInfo.status = Status.FAILED
                downloadInfoUpdater.update(downloadInfo)
                LSDownloaderListener.onError(download, lsDownloaderError, throwable)
            }
        }
    }

    override fun onComplete(download: Download) {
        if (!interrupted) {
            val downloadInfo = download as DownloadInfo
            downloadInfo.status = Status.COMPLETED
            downloadInfoUpdater.update(downloadInfo)
            LSDownloaderListener.onCompleted(download)
        }
    }

    override fun saveDownloadProgress(download: Download) {
        if (!interrupted) {
            val downloadInfo = download as DownloadInfo
            downloadInfo.status = Status.DOWNLOADING
            downloadInfoUpdater.updateFileBytesInfoAndStatusOnly(downloadInfo)
        }
    }

    override fun getNewDownloadInfoInstance(): DownloadInfo {
        return downloadInfoUpdater.getNewDownloadInfoInstance()
    }

}