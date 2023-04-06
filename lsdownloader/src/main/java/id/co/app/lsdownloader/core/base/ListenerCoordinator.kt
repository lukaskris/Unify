package id.co.app.lsdownloader.core.base

import android.os.Handler
import android.os.HandlerThread
import id.co.app.lsdownloader.model.provider.DownloadProvider
import id.co.app.lsdownloader.model.provider.GroupInfoProvider
import id.co.app.lsdownloader.model.LSDownloaderError
import java.lang.ref.WeakReference

class ListenerCoordinator(val namespace: String,
                          private val groupInfoProvider: GroupInfoProvider,
                          private val downloadProvider: DownloadProvider,
                          private val uiHandler: Handler) {

    private val lock = Any()
    private val fetchListenerMap = mutableMapOf<Int, MutableSet<WeakReference<LSDownloaderListener>>>()
    private val LSDownloaderGroupListenerMap = mutableMapOf<Int, MutableSet<WeakReference<LSDownloaderGroupListener>>>()
    private val fetchNotificationManagerList = mutableListOf<LSDownloaderNotificationManager>()
    private val fetchNotificationHandler = run {
        val handlerThread = HandlerThread("FetchNotificationsIO")
        handlerThread.start()
        Handler(handlerThread.looper)
    }
    private val downloadsObserverMap = mutableMapOf<Int, MutableList<WeakReference<LSDownloaderObserver<Download>>>>()

    fun addListener(id: Int, fetchListener: LSDownloaderListener) {
        synchronized(lock) {
            val set = fetchListenerMap[id] ?: mutableSetOf()
            set.add(WeakReference(fetchListener))
            fetchListenerMap[id] = set
            if (fetchListener is LSDownloaderGroupListener) {
                val groupSet = LSDownloaderGroupListenerMap[id] ?: mutableSetOf()
                groupSet.add(WeakReference(fetchListener))
                LSDownloaderGroupListenerMap[id] = groupSet
            }
        }
    }

    fun removeListener(id: Int, fetchListener: LSDownloaderListener) {
        synchronized(lock) {
            val iterator = fetchListenerMap[id]?.iterator()
            if (iterator != null) {
                while (iterator.hasNext()) {
                    val reference = iterator.next()
                    if (reference.get() == fetchListener) {
                        iterator.remove()
                        break
                    }
                }
            }
            if (fetchListener is LSDownloaderGroupListener) {
                val groupIterator = LSDownloaderGroupListenerMap[id]?.iterator()
                if (groupIterator != null) {
                    while (groupIterator.hasNext()) {
                        val reference = groupIterator.next()
                        if (reference.get() == fetchListener) {
                            groupIterator.remove()
                            break
                        }
                    }
                }
            }
        }
    }

    fun clearListener(){
        fetchListenerMap.clear()
    }

    fun addNotificationManager(fetchNotificationManager: LSDownloaderNotificationManager) {
        synchronized(lock) {
            if (!fetchNotificationManagerList.contains(fetchNotificationManager)) {
                fetchNotificationManagerList.add(fetchNotificationManager)
            }
        }
    }

    fun removeNotificationManager(fetchNotificationManager: LSDownloaderNotificationManager) {
        synchronized(lock) {
            fetchNotificationManagerList.remove(fetchNotificationManager)
        }
    }

    fun cancelOnGoingNotifications(fetchNotificationManager: LSDownloaderNotificationManager) {
        synchronized(lock) {
            fetchNotificationHandler.post {
                synchronized(lock) {
                    fetchNotificationManager.cancelOngoingNotifications()
                }
            }
        }
    }

    val mainListener: LSDownloaderListener = object : LSDownloaderListener {

        override fun onAdded(download: Download) {
            synchronized(lock) {
                fetchListenerMap.values.forEach {
                    val iterator = it.iterator()
                    while (iterator.hasNext()) {
                        val fetchListener = iterator.next().get()
                        if (fetchListener == null) {
                            iterator.remove()
                        } else {
                            uiHandler.post {
                                fetchListener.onAdded(download)
                            }
                        }
                    }
                }
                if (LSDownloaderGroupListenerMap.isNotEmpty()) {
                    val groupId = download.group
                    val fetchGroup = groupInfoProvider.getGroupReplace(groupId, download, Reason.DOWNLOAD_ADDED)
                    LSDownloaderGroupListenerMap.values.forEach {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            val fetchListener = iterator.next().get()
                            if (fetchListener == null) {
                                iterator.remove()
                            } else {
                                uiHandler.post {
                                    fetchListener.onAdded(groupId, download, fetchGroup)
                                }
                            }
                        }
                    }
                } else {
                    groupInfoProvider.postGroupReplace(download.group, download, Reason.DOWNLOAD_ADDED)
                }
                val downloadObserverSet = downloadsObserverMap[download.id]
                downloadObserverSet?.forEach {
                    val observer = it.get()
                    if (observer != null) {
                        uiHandler.post {
                            observer.onChanged(download, Reason.DOWNLOAD_ADDED)
                        }
                    }
                }
            }
        }

        override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
            synchronized(lock) {
                fetchListenerMap.values.forEach {
                    val iterator = it.iterator()
                    while (iterator.hasNext()) {
                        val fetchListener = iterator.next().get()
                        if (fetchListener == null) {
                            iterator.remove()
                        } else {
                            uiHandler.post {
                                fetchListener.onQueued(download, waitingOnNetwork)
                            }
                        }
                    }
                }
                if (LSDownloaderGroupListenerMap.isNotEmpty()) {
                    val groupId = download.group
                    val fetchGroup = groupInfoProvider.getGroupReplace(groupId, download, Reason.DOWNLOAD_QUEUED)
                    LSDownloaderGroupListenerMap.values.forEach {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            val fetchListener = iterator.next().get()
                            if (fetchListener == null) {
                                iterator.remove()
                            } else {
                                fetchListener.onQueued(groupId, download, waitingOnNetwork, fetchGroup)
                            }
                        }
                    }
                } else {
                    groupInfoProvider.postGroupReplace(download.group, download, Reason.DOWNLOAD_QUEUED)
                }
                val downloadObserverSet = downloadsObserverMap[download.id]
                downloadObserverSet?.forEach {
                    val observer = it.get()
                    if (observer != null) {
                        uiHandler.post {
                            observer.onChanged(download, Reason.DOWNLOAD_QUEUED)
                        }
                    }
                }
            }
        }

        override fun onWaitingNetwork(download: Download) {
            synchronized(lock) {
                fetchListenerMap.values.forEach {
                    val iterator = it.iterator()
                    while (iterator.hasNext()) {
                        val fetchListener = iterator.next().get()
                        if (fetchListener == null) {
                            iterator.remove()
                        } else {
                            uiHandler.post {
                                fetchListener.onWaitingNetwork(download)
                            }
                        }
                    }
                }
                if (LSDownloaderGroupListenerMap.isNotEmpty()) {
                    val groupId = download.group
                    val fetchGroup = groupInfoProvider.getGroupReplace(groupId, download, Reason.DOWNLOAD_WAITING_ON_NETWORK)
                    LSDownloaderGroupListenerMap.values.forEach {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            val fetchListener = iterator.next().get()
                            if (fetchListener == null) {
                                iterator.remove()
                            } else {
                                fetchListener.onWaitingNetwork(groupId, download, fetchGroup)
                            }
                        }
                    }
                } else {
                    groupInfoProvider.postGroupReplace(download.group, download, Reason.DOWNLOAD_WAITING_ON_NETWORK)
                }
                val downloadObserverSet = downloadsObserverMap[download.id]
                downloadObserverSet?.forEach {
                    val observer = it.get()
                    if (observer != null) {
                        uiHandler.post {
                            observer.onChanged(download, Reason.DOWNLOAD_WAITING_ON_NETWORK)
                        }
                    }
                }
            }
        }

        override fun onCompleted(download: Download) {
            synchronized(lock) {
                fetchNotificationHandler.post {
                    synchronized(lock) {
                        for (fetchNotificationManager in fetchNotificationManagerList) {
                            if (fetchNotificationManager.postDownloadUpdate(download)) break
                        }
                    }
                }
                fetchListenerMap.values.forEach {
                    val iterator = it.iterator()
                    while (iterator.hasNext()) {
                        val fetchListener = iterator.next().get()
                        if (fetchListener == null) {
                            iterator.remove()
                        } else {
                            uiHandler.post {
                                fetchListener.onCompleted(download)
                            }
                        }
                    }
                }
                if (LSDownloaderGroupListenerMap.isNotEmpty()) {
                    val groupId = download.group
                    val fetchGroup = groupInfoProvider.getGroupReplace(groupId, download, Reason.DOWNLOAD_COMPLETED)
                    LSDownloaderGroupListenerMap.values.forEach {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            val fetchListener = iterator.next().get()
                            if (fetchListener == null) {
                                iterator.remove()
                            } else {
                                fetchListener.onCompleted(groupId, download, fetchGroup)
                            }
                        }
                    }
                } else {
                    groupInfoProvider.postGroupReplace(download.group, download, Reason.DOWNLOAD_COMPLETED)
                }
                val downloadObserverSet = downloadsObserverMap[download.id]
                downloadObserverSet?.forEach {
                    val observer = it.get()
                    if (observer != null) {
                        uiHandler.post {
                            observer.onChanged(download, Reason.DOWNLOAD_COMPLETED)
                        }
                    }
                }
            }
        }

        override fun onError(download: Download, LSDownloaderError: LSDownloaderError, throwable: Throwable?) {
            synchronized(lock) {
                fetchNotificationHandler.post {
                    synchronized(lock) {
                        for (fetchNotificationManager in fetchNotificationManagerList) {
                            if (fetchNotificationManager.postDownloadUpdate(download)) break
                        }
                    }
                }
                fetchListenerMap.values.forEach {
                    val iterator = it.iterator()
                    while (iterator.hasNext()) {
                        val fetchListener = iterator.next().get()
                        if (fetchListener == null) {
                            iterator.remove()
                        } else {
                            uiHandler.post {
                                fetchListener.onError(download, LSDownloaderError, throwable)
                            }
                        }
                    }
                }
                if (LSDownloaderGroupListenerMap.isNotEmpty()) {
                    val groupId = download.group
                    val fetchGroup = groupInfoProvider.getGroupReplace(groupId, download, Reason.DOWNLOAD_ERROR)
                    LSDownloaderGroupListenerMap.values.forEach {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            val fetchListener = iterator.next().get()
                            if (fetchListener == null) {
                                iterator.remove()
                            } else {
                                fetchListener.onError(groupId, download, LSDownloaderError, throwable, fetchGroup)
                            }
                        }
                    }
                } else {
                    groupInfoProvider.postGroupReplace(download.group, download, Reason.DOWNLOAD_ERROR)
                }
                val downloadObserverSet = downloadsObserverMap[download.id]
                downloadObserverSet?.forEach {
                    val observer = it.get()
                    if (observer != null) {
                        uiHandler.post {
                            observer.onChanged(download, Reason.DOWNLOAD_ERROR)
                        }
                    }
                }
            }
        }

        override fun onDownloadBlockUpdated(download: Download, downloadBlock: DownloadBlock, totalBlocks: Int) {
            synchronized(lock) {
                fetchListenerMap.values.forEach {
                    val iterator = it.iterator()
                    while (iterator.hasNext()) {
                        val fetchListener = iterator.next().get()
                        if (fetchListener == null) {
                            iterator.remove()
                        } else {
                            fetchListener.onDownloadBlockUpdated(download, downloadBlock, totalBlocks)
                        }
                    }
                }
                if (LSDownloaderGroupListenerMap.isNotEmpty()) {
                    val groupId = download.group
                    val fetchGroup = groupInfoProvider.getGroupReplace(groupId, download, Reason.DOWNLOAD_BLOCK_UPDATED)
                    LSDownloaderGroupListenerMap.values.forEach {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            val fetchListener = iterator.next().get()
                            if (fetchListener == null) {
                                iterator.remove()
                            } else {
                                fetchListener.onDownloadBlockUpdated(groupId, download, downloadBlock, totalBlocks, fetchGroup)
                            }
                        }
                    }
                }
            }
        }

        override fun onStarted(download: Download, downloadBlocks: List<DownloadBlock>, totalBlocks: Int) {
            synchronized(lock) {
                fetchNotificationHandler.post {
                    synchronized(lock) {
                        for (fetchNotificationManager in fetchNotificationManagerList) {
                            if (fetchNotificationManager.postDownloadUpdate(download)) break
                        }
                    }
                }
                fetchListenerMap.values.forEach {
                    val iterator = it.iterator()
                    while (iterator.hasNext()) {
                        val fetchListener = iterator.next().get()
                        if (fetchListener == null) {
                            iterator.remove()
                        } else {
                            uiHandler.post {
                                fetchListener.onStarted(download, downloadBlocks, totalBlocks)
                            }
                        }
                    }
                }
                if (LSDownloaderGroupListenerMap.isNotEmpty()) {
                    val groupId = download.group
                    val fetchGroup = groupInfoProvider.getGroupReplace(groupId, download, Reason.DOWNLOAD_STARTED)
                    LSDownloaderGroupListenerMap.values.forEach {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            val fetchListener = iterator.next().get()
                            if (fetchListener == null) {
                                iterator.remove()
                            } else {
                                fetchListener.onStarted(groupId, download, downloadBlocks, totalBlocks, fetchGroup)
                            }
                        }
                    }
                } else {
                    groupInfoProvider.postGroupReplace(download.group, download, Reason.DOWNLOAD_STARTED)
                }
                val downloadObserverSet = downloadsObserverMap[download.id]
                downloadObserverSet?.forEach {
                    val observer = it.get()
                    if (observer != null) {
                        uiHandler.post {
                            observer.onChanged(download, Reason.DOWNLOAD_STARTED)
                        }
                    }
                }
            }
        }

        override fun onProgress(download: Download, etaInMilliSeconds: Long, downloadedBytesPerSecond: Long) {
            synchronized(lock) {
                fetchNotificationHandler.post {
                    synchronized(lock) {
                        for (fetchNotificationManager in fetchNotificationManagerList) {
                            if (fetchNotificationManager.postDownloadUpdate(download)) break
                        }
                    }
                }
                fetchListenerMap.values.forEach {
                    val iterator = it.iterator()
                    while (iterator.hasNext()) {
                        val fetchListener = iterator.next().get()
                        if (fetchListener == null) {
                            iterator.remove()
                        } else {
                            uiHandler.post {
                                fetchListener.onProgress(download, etaInMilliSeconds, downloadedBytesPerSecond)
                            }
                        }
                    }
                }
                if (LSDownloaderGroupListenerMap.isNotEmpty()) {
                    val groupId = download.group
                    val fetchGroup = groupInfoProvider.getGroupReplace(groupId, download, Reason.DOWNLOAD_PROGRESS_CHANGED)
                    LSDownloaderGroupListenerMap.values.forEach {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            val fetchListener = iterator.next().get()
                            if (fetchListener == null) {
                                iterator.remove()
                            } else {
                                fetchListener.onProgress(groupId, download, etaInMilliSeconds, downloadedBytesPerSecond, fetchGroup)
                            }
                        }
                    }
                } else {
                    groupInfoProvider.postGroupReplace(download.group, download, Reason.DOWNLOAD_PROGRESS_CHANGED)
                }
                val downloadObserverSet = downloadsObserverMap[download.id]
                downloadObserverSet?.forEach {
                    val observer = it.get()
                    if (observer != null) {
                        uiHandler.post {
                            observer.onChanged(download, Reason.DOWNLOAD_PROGRESS_CHANGED)
                        }
                    }
                }
            }
        }

        override fun onPaused(download: Download) {
            synchronized(lock) {
                fetchNotificationHandler.post {
                    synchronized(lock) {
                        for (fetchNotificationManager in fetchNotificationManagerList) {
                            if (fetchNotificationManager.postDownloadUpdate(download)) break
                        }
                    }
                }
                fetchListenerMap.values.forEach {
                    val iterator = it.iterator()
                    while (iterator.hasNext()) {
                        val fetchListener = iterator.next().get()
                        if (fetchListener == null) {
                            iterator.remove()
                        } else {
                            uiHandler.post {
                                fetchListener.onPaused(download)
                            }
                        }
                    }
                }
                if (LSDownloaderGroupListenerMap.isNotEmpty()) {
                    val groupId = download.group
                    val fetchGroup = groupInfoProvider.getGroupReplace(groupId, download,  Reason.DOWNLOAD_PAUSED)
                    LSDownloaderGroupListenerMap.values.forEach {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            val fetchListener = iterator.next().get()
                            if (fetchListener == null) {
                                iterator.remove()
                            } else {
                                fetchListener.onPaused(groupId, download, fetchGroup)
                            }
                        }
                    }
                } else {
                    groupInfoProvider.postGroupReplace(download.group, download, Reason.DOWNLOAD_PAUSED)
                }
                val downloadObserverSet = downloadsObserverMap[download.id]
                downloadObserverSet?.forEach {
                    val observer = it.get()
                    if (observer != null) {
                        uiHandler.post {
                            observer.onChanged(download, Reason.DOWNLOAD_PAUSED)
                        }
                    }
                }
            }
        }

        override fun onResumed(download: Download) {
            synchronized(lock) {
                fetchNotificationHandler.post {
                    synchronized(lock) {
                        for (fetchNotificationManager in fetchNotificationManagerList) {
                            if (fetchNotificationManager.postDownloadUpdate(download)) break
                        }
                    }
                }
                fetchListenerMap.values.forEach {
                    val iterator = it.iterator()
                    while (iterator.hasNext()) {
                        val fetchListener = iterator.next().get()
                        if (fetchListener == null) {
                            iterator.remove()
                        } else {
                            uiHandler.post {
                                fetchListener.onResumed(download)
                            }
                        }
                    }
                }
                if (LSDownloaderGroupListenerMap.isNotEmpty()) {
                    val groupId = download.group
                    val fetchGroup = groupInfoProvider.getGroupReplace(groupId, download, Reason.DOWNLOAD_RESUMED)
                    LSDownloaderGroupListenerMap.values.forEach {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            val fetchListener = iterator.next().get()
                            if (fetchListener == null) {
                                iterator.remove()
                            } else {
                                fetchListener.onResumed(groupId, download, fetchGroup)
                            }
                        }
                    }
                } else {
                    groupInfoProvider.postGroupReplace(download.group, download, Reason.DOWNLOAD_RESUMED)
                }
                val downloadObserverSet = downloadsObserverMap[download.id]
                downloadObserverSet?.forEach {
                    val observer = it.get()
                    if (observer != null) {
                        uiHandler.post {
                            observer.onChanged(download, Reason.DOWNLOAD_RESUMED)
                        }
                    }
                }
            }
        }

        override fun onCancelled(download: Download) {
            synchronized(lock) {
                fetchNotificationHandler.post {
                    synchronized(lock) {
                        for (fetchNotificationManager in fetchNotificationManagerList) {
                            if (fetchNotificationManager.postDownloadUpdate(download)) break
                        }
                    }
                }
                fetchListenerMap.values.forEach {
                    val iterator = it.iterator()
                    while (iterator.hasNext()) {
                        val fetchListener = iterator.next().get()
                        if (fetchListener == null) {
                            iterator.remove()
                        } else {
                            uiHandler.post {
                                fetchListener.onCancelled(download)
                            }
                        }
                    }
                }
                if (LSDownloaderGroupListenerMap.isNotEmpty()) {
                    val groupId = download.group
                    val fetchGroup = groupInfoProvider.getGroupReplace(groupId, download, Reason.DOWNLOAD_CANCELLED)
                    LSDownloaderGroupListenerMap.values.forEach {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            val fetchListener = iterator.next().get()
                            if (fetchListener == null) {
                                iterator.remove()
                            } else {
                                fetchListener.onCancelled(groupId, download, fetchGroup)
                            }
                        }
                    }
                } else {
                    groupInfoProvider.postGroupReplace(download.group, download, Reason.DOWNLOAD_CANCELLED)
                }
                val downloadObserverSet = downloadsObserverMap[download.id]
                downloadObserverSet?.forEach {
                    val observer = it.get()
                    if (observer != null) {
                        uiHandler.post {
                            observer.onChanged(download, Reason.DOWNLOAD_CANCELLED)
                        }
                    }
                }
            }
        }

        override fun onRemoved(download: Download) {
            synchronized(lock) {
                fetchNotificationHandler.post {
                    synchronized(lock) {
                        for (fetchNotificationManager in fetchNotificationManagerList) {
                            if (fetchNotificationManager.postDownloadUpdate(download)) break
                        }
                    }
                }
                fetchListenerMap.values.forEach {
                    val iterator = it.iterator()
                    while (iterator.hasNext()) {
                        val fetchListener = iterator.next().get()
                        if (fetchListener == null) {
                            iterator.remove()
                        } else {
                            uiHandler.post {
                                fetchListener.onRemoved(download)
                            }
                        }
                    }
                }
                if (LSDownloaderGroupListenerMap.isNotEmpty()) {
                    val groupId = download.group
                    val fetchGroup = groupInfoProvider.getGroupReplace(groupId, download, Reason.DOWNLOAD_REMOVED)
                    LSDownloaderGroupListenerMap.values.forEach {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            val fetchListener = iterator.next().get()
                            if (fetchListener == null) {
                                iterator.remove()
                            } else {
                                fetchListener.onRemoved(groupId, download, fetchGroup)
                            }
                        }
                    }
                } else {
                    groupInfoProvider.postGroupReplace(download.group, download, Reason.DOWNLOAD_REMOVED)
                }
                val downloadObserverSet = downloadsObserverMap[download.id]
                downloadObserverSet?.forEach {
                    val observer = it.get()
                    if (observer != null) {
                        uiHandler.post {
                            observer.onChanged(download, Reason.DOWNLOAD_REMOVED)
                        }
                    }
                }
            }
        }

        override fun onDeleted(download: Download) {
            synchronized(lock) {
                fetchNotificationHandler.post {
                    synchronized(lock) {
                        for (fetchNotificationManager in fetchNotificationManagerList) {
                            if (fetchNotificationManager.postDownloadUpdate(download)) break
                        }
                    }
                }
                fetchListenerMap.values.forEach {
                    val iterator = it.iterator()
                    while (iterator.hasNext()) {
                        val fetchListener = iterator.next().get()
                        if (fetchListener == null) {
                            iterator.remove()
                        } else {
                            uiHandler.post {
                                fetchListener.onDeleted(download)
                            }
                        }
                    }
                }
                if (LSDownloaderGroupListenerMap.isNotEmpty()) {
                    val groupId = download.group
                    val fetchGroup = groupInfoProvider.getGroupReplace(groupId, download, Reason.DOWNLOAD_DELETED)
                    LSDownloaderGroupListenerMap.values.forEach {
                        val iterator = it.iterator()
                        while (iterator.hasNext()) {
                            val fetchListener = iterator.next().get()
                            if (fetchListener == null) {
                                iterator.remove()
                            } else {
                                fetchListener.onDeleted(groupId, download, fetchGroup)
                            }
                        }
                    }
                } else {
                    groupInfoProvider.postGroupReplace(download.group, download, Reason.DOWNLOAD_DELETED)
                }
                val downloadObserverSet = downloadsObserverMap[download.id]
                downloadObserverSet?.forEach {
                    val observer = it.get()
                    if (observer != null) {
                        uiHandler.post {
                            observer.onChanged(download, Reason.DOWNLOAD_DELETED)
                        }
                    }
                }
            }
        }
    }

    fun clearAll() {
        synchronized(lock) {
            fetchListenerMap.clear()
            LSDownloaderGroupListenerMap.clear()
            fetchNotificationManagerList.clear()
            downloadsObserverMap.clear()
        }
    }

    fun addFetchObserversForDownload(downloadId: Int, vararg fetchObservers: LSDownloaderObserver<Download>) {
        synchronized(lock) {
            val newFetchObservers = fetchObservers.distinct()
            val set = downloadsObserverMap[downloadId] ?: mutableListOf()
            val attachedObservers = set.mapNotNull { it.get() }
            val addedObservers = mutableListOf<LSDownloaderObserver<Download>>()
            for (fetchObserver in newFetchObservers) {
                if (!attachedObservers.contains(fetchObserver)) {
                    set.add(WeakReference(fetchObserver))
                    addedObservers.add(fetchObserver)
                }
            }
            val download = downloadProvider.getDownload(downloadId)
            if (download != null) {
                uiHandler.post {
                    for (addedObserver in addedObservers) {
                        addedObserver.onChanged(download, Reason.OBSERVER_ATTACHED)
                    }
                }
            }
            downloadsObserverMap[downloadId] = set
        }
    }

    fun removeFetchObserversForDownload(downloadId: Int, vararg fetchObservers: LSDownloaderObserver<Download>) {
        synchronized(lock) {
            for (fetchObserver in fetchObservers) {
                val iterator = downloadsObserverMap[downloadId]?.iterator()
                if (iterator != null) {
                    while (iterator.hasNext()) {
                        val reference = iterator.next()
                        if (reference.get() == fetchObserver) {
                            iterator.remove()
                            break
                        }
                    }
                }
            }
        }
    }

}