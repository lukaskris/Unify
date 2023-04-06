package id.co.app.lsdownloader.model.provider

import id.co.app.lsdownloader.core.base.Download
import id.co.app.lsdownloader.core.database.LSDownloaderDatabaseManagerWrapper
import id.co.app.lsdownloader.model.PrioritySort
import id.co.app.lsdownloader.model.Status


class DownloadProvider(private val fetchDatabaseManagerWrapper: LSDownloaderDatabaseManagerWrapper) {

    fun getDownloads(): List<Download> {
        return fetchDatabaseManagerWrapper.get()
    }

    fun getDownload(id: Int): Download? {
        return fetchDatabaseManagerWrapper.get(id)
    }

    fun getDownloads(ids: List<Int>): List<Download?> {
        return fetchDatabaseManagerWrapper.get(ids)
    }

    fun getByGroup(group: Int): List<Download> {
        return fetchDatabaseManagerWrapper.getByGroup(group)
    }

    fun getByGroupReplace(group: Int, download: Download): List<Download> {
        val downloads = getByGroup(group) as ArrayList
        val index = downloads.indexOfFirst { it.id == download.id }
        if (index != -1) {
            downloads[index] = download
        }
        return downloads
    }

    fun getByStatus(status: Status): List<Download> {
        return fetchDatabaseManagerWrapper.getByStatus(status)
    }

    fun getPendingDownloadsSorted(prioritySort: PrioritySort): List<Download> {
        return fetchDatabaseManagerWrapper.getPendingDownloadsSorted(prioritySort)
    }

}