package id.co.app.lsdownloader.model

import id.co.app.lsdownloader.core.database.DownloadInfo
import id.co.app.lsdownloader.core.database.LSDownloaderDatabaseManagerWrapper


class DownloadInfoUpdater(private val fetchDatabaseManagerWrapper: LSDownloaderDatabaseManagerWrapper) {

    fun updateFileBytesInfoAndStatusOnly(downloadInfo: DownloadInfo) {
        fetchDatabaseManagerWrapper.updateFileBytesInfoAndStatusOnly(downloadInfo)
    }

    fun update(downloadInfo: DownloadInfo) {
        fetchDatabaseManagerWrapper.update(downloadInfo)
    }

    fun getNewDownloadInfoInstance(): DownloadInfo {
        return fetchDatabaseManagerWrapper.getNewDownloadInfoInstance()
    }

}