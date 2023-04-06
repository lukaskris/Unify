@file:JvmName("FetchTypeConverterExtensions")

package id.co.app.lsdownloader.utils

import id.co.app.lsdownloader.model.CompletedDownload
import id.co.app.lsdownloader.core.base.Download
import id.co.app.lsdownloader.core.database.DownloadInfo
import id.co.app.lsdownloader.model.*

fun Request.toDownloadInfo(downloadInfo: DownloadInfo): DownloadInfo {
    downloadInfo.id = id
    downloadInfo.url = url
    downloadInfo.file = file
    downloadInfo.priority = priority
    downloadInfo.headers = headers.toMap()
    downloadInfo.group = groupId
    downloadInfo.networkType = networkType
    downloadInfo.status = defaultStatus
    downloadInfo.lsDownloaderError = defaultNoLSDownloaderError
    downloadInfo.downloaded = 0L
    downloadInfo.tag = tag
    downloadInfo.enqueueAction = enqueueAction
    downloadInfo.identifier = identifier
    downloadInfo.downloadOnEnqueue = downloadOnEnqueue
    downloadInfo.extras = extras
    downloadInfo.autoRetryMaxAttempts = autoRetryMaxAttempts
    downloadInfo.autoRetryAttempts = DEFAULT_AUTO_RETRY_ATTEMPTS
    return downloadInfo
}

fun Download.toDownloadInfo(downloadInfo: DownloadInfo): DownloadInfo {
    downloadInfo.id = id
    downloadInfo.namespace = namespace
    downloadInfo.url = url
    downloadInfo.file = file
    downloadInfo.group = group
    downloadInfo.priority = priority
    downloadInfo.headers = headers.toMap()
    downloadInfo.downloaded = downloaded
    downloadInfo.total = total
    downloadInfo.status = status
    downloadInfo.networkType = networkType
    downloadInfo.lsDownloaderError = lsDownloaderError
    downloadInfo.created = created
    downloadInfo.tag = tag
    downloadInfo.enqueueAction = enqueueAction
    downloadInfo.identifier = identifier
    downloadInfo.downloadOnEnqueue = downloadOnEnqueue
    downloadInfo.extras = extras
    downloadInfo.autoRetryMaxAttempts = autoRetryMaxAttempts
    downloadInfo.autoRetryAttempts = autoRetryAttempts
    return downloadInfo
}

fun CompletedDownload.toDownloadInfo(downloadInfo: DownloadInfo): DownloadInfo {
    downloadInfo.id = getUniqueId(url, file)
    downloadInfo.url = url
    downloadInfo.file = file
    downloadInfo.group = group
    downloadInfo.priority = Priority.NORMAL
    downloadInfo.headers = headers.toMap()
    downloadInfo.downloaded = fileByteSize
    downloadInfo.total = fileByteSize
    downloadInfo.status = Status.COMPLETED
    downloadInfo.networkType = NetworkType.ALL
    downloadInfo.lsDownloaderError = LSDownloaderError.NONE
    downloadInfo.created = created
    downloadInfo.tag = tag
    downloadInfo.enqueueAction = EnqueueAction.REPLACE_EXISTING
    downloadInfo.identifier = identifier
    downloadInfo.downloadOnEnqueue = DEFAULT_DOWNLOAD_ON_ENQUEUE
    downloadInfo.extras = extras
    downloadInfo.autoRetryMaxAttempts = DEFAULT_AUTO_RETRY_ATTEMPTS
    downloadInfo.autoRetryAttempts = DEFAULT_AUTO_RETRY_ATTEMPTS
    return downloadInfo
}