package id.co.app.lsdownloader.utils

import id.co.app.lsdownloader.core.LSDownloader
import id.co.app.lsdownloader.core.base.Download
import id.co.app.lsdownloader.core.base.DownloadBlock
import id.co.app.lsdownloader.core.base.LSDownloaderListener
import id.co.app.lsdownloader.model.LSDownloaderError


/**
 * Created by Lukas Kristianto on 26/09/22 16.33.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
inline fun LSDownloader.setListener(
    crossinline onAdded: (Download) -> Unit = {},
    crossinline onQueued: (Download, Boolean) -> Unit = { _, _ ->},
    crossinline onWaitingNetwork: (Download) -> Unit = {},
    crossinline onCompleted: (Download) -> Unit = {},
    crossinline onError: (Download, LSDownloaderError, Throwable?) -> Unit = {_,_,_ ->},
    crossinline onDownloadBlockUpdated: (Download, DownloadBlock, Int) -> Unit = { _,_, _ -> },
    crossinline onStarted: (Download, List<DownloadBlock>, Int) -> Unit = { _,_, _ -> },
    crossinline onProgress: (Download, Long, Long) -> Unit = { _,_, _ -> },
    crossinline onPaused: (Download) -> Unit = {},
    crossinline onResumed: (Download) -> Unit = {},
    crossinline onCancelled: (Download) -> Unit = {},
    crossinline onRemoved: (Download) -> Unit = {},
    crossinline onDeleted: (Download) -> Unit = {},
){

    addListener(object : LSDownloaderListener {
        override fun onAdded(download: Download) {
            onAdded(download)
        }

        override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
            onQueued(download, waitingOnNetwork)
        }

        override fun onWaitingNetwork(download: Download) {
            onWaitingNetwork(download)
        }

        override fun onCompleted(download: Download) {
            onCompleted(download)
        }

        override fun onError(
            download: Download,
            LSDownloaderError: LSDownloaderError,
            throwable: Throwable?
        ) {
            onError(download, LSDownloaderError, throwable)
        }

        override fun onDownloadBlockUpdated(
            download: Download,
            downloadBlock: DownloadBlock,
            totalBlocks: Int
        ) {
            onDownloadBlockUpdated(download, downloadBlock, totalBlocks)
        }

        override fun onStarted(
            download: Download,
            downloadBlocks: List<DownloadBlock>,
            totalBlocks: Int
        ) {
            onStarted(download, downloadBlocks, totalBlocks)
        }

        override fun onProgress(
            download: Download,
            etaInMilliSeconds: Long,
            downloadedBytesPerSecond: Long
        ) {
            onProgress(download, etaInMilliSeconds, downloadedBytesPerSecond)
        }

        override fun onPaused(download: Download) {
            onPaused(download)
        }

        override fun onResumed(download: Download) {
            onResumed(download)
        }

        override fun onCancelled(download: Download) {
            onCancelled(download)
        }

        override fun onRemoved(download: Download) {
            onRemoved(download)
        }

        override fun onDeleted(download: Download) {
            onDeleted(download)
        }
    })
}