package id.co.app.lsdownloader.core

import android.os.Handler
import android.os.Looper
import id.co.app.lsdownloader.core.base.*
import id.co.app.lsdownloader.core.database.*
import id.co.app.lsdownloader.core.downloader.DownloadManager
import id.co.app.lsdownloader.core.downloader.DownloadManagerCoordinator
import id.co.app.lsdownloader.core.downloader.DownloadManagerImpl
import id.co.app.lsdownloader.model.provider.DownloadProvider
import id.co.app.lsdownloader.model.provider.GroupInfoProvider
import id.co.app.lsdownloader.model.provider.NetworkInfoProvider
import id.co.app.lsdownloader.model.DownloadInfoUpdater
import id.co.app.lsdownloader.model.LSConfiguration
import id.co.app.lsdownloader.model.LiveSettings
import id.co.app.lsdownloader.utils.deleteAllInFolderForId
import id.co.app.lsdownloader.utils.getFileTempDir
import id.co.app.lsdownloader.utils.getRequestForDownload

object LSDownloaderModulesBuilder {

    private val lock = Any()
    private val holderMap = mutableMapOf<String, Holder>()
    val mainUIHandler = Handler(Looper.getMainLooper())

    fun buildModulesFromPrefs(fetchConfiguration: LSConfiguration): Modules {
        return synchronized(lock) {
            val holder = holderMap[fetchConfiguration.namespace]
            val modules = if (holder != null) {
                Modules(fetchConfiguration, holder.handlerWrapper, holder.fetchDatabaseManagerWrapper, holder.downloadProvider,
                        holder.groupInfoProvider, holder.uiHandler, holder.downloadManagerCoordinator, holder.listenerCoordinator)
            } else {
                val newHandlerWrapper = HandlerWrapper(fetchConfiguration.namespace, fetchConfiguration.backgroundHandler)
                val liveSettings = LiveSettings(fetchConfiguration.namespace)
                val newDatabaseManager = fetchConfiguration.fetchDatabaseManager
                        ?: LSDownloaderDatabaseManagerImpl(
                                context = fetchConfiguration.appContext,
                                namespace = fetchConfiguration.namespace,
                                logger = fetchConfiguration.logger,
                                migrations = DownloadDatabase.getMigrations(),
                                liveSettings = liveSettings,
                                fileExistChecksEnabled = fetchConfiguration.fileExistChecksEnabled,
                                defaultStorageResolver = DefaultStorageResolver(
                                            fetchConfiguration.appContext,
                                            getFileTempDir(fetchConfiguration.appContext)
                                )
                        )
                val databaseManagerWrapper = LSDownloaderDatabaseManagerWrapper(newDatabaseManager)
                val downloadProvider = DownloadProvider(databaseManagerWrapper)
                val downloadManagerCoordinator = DownloadManagerCoordinator(fetchConfiguration.namespace)
                val groupInfoProvider = GroupInfoProvider(fetchConfiguration.namespace, downloadProvider)
                val listenerCoordinator = ListenerCoordinator(fetchConfiguration.namespace, groupInfoProvider, downloadProvider, mainUIHandler)
                val newModules = Modules(fetchConfiguration, newHandlerWrapper, databaseManagerWrapper, downloadProvider, groupInfoProvider, mainUIHandler,
                        downloadManagerCoordinator, listenerCoordinator)
                holderMap[fetchConfiguration.namespace] = Holder(newHandlerWrapper, databaseManagerWrapper, downloadProvider, groupInfoProvider, mainUIHandler,
                        downloadManagerCoordinator, listenerCoordinator, newModules.networkInfoProvider)
                newModules
            }
            modules.handlerWrapper.incrementUsageCounter()
            modules
        }
    }

    fun removeNamespaceInstanceReference(namespace: String) {
        synchronized(lock) {
            val holder = holderMap[namespace]
            if (holder != null) {
                holder.handlerWrapper.decrementUsageCounter()
                if (holder.handlerWrapper.usageCount() == 0) {
                    holder.handlerWrapper.close()
                    holder.listenerCoordinator.clearAll()
                    holder.groupInfoProvider.clear()
                    holder.fetchDatabaseManagerWrapper.close()
                    holder.downloadManagerCoordinator.clearAll()
                    holder.networkInfoProvider.unregisterAllNetworkChangeListeners()
                    holderMap.remove(namespace)
                }
            }
        }
    }

    data class Holder(val handlerWrapper: HandlerWrapper,
                      val fetchDatabaseManagerWrapper: LSDownloaderDatabaseManagerWrapper,
                      val downloadProvider: DownloadProvider,
                      val groupInfoProvider: GroupInfoProvider,
                      val uiHandler: Handler,
                      val downloadManagerCoordinator: DownloadManagerCoordinator,
                      val listenerCoordinator: ListenerCoordinator,
                      val networkInfoProvider: NetworkInfoProvider
    )

    class Modules constructor(val fetchConfiguration: LSConfiguration,
                              val handlerWrapper: HandlerWrapper,
                              val LSDownloaderDatabaseManagerWrapper: LSDownloaderDatabaseManagerWrapper,
                              val downloadProvider: DownloadProvider,
                              val groupInfoProvider: GroupInfoProvider,
                              val uiHandler: Handler,
                              downloadManagerCoordinator: DownloadManagerCoordinator,
                              val listenerCoordinator: ListenerCoordinator) {

        val downloadManager: DownloadManager
        val priorityListProcessor: PriorityListProcessor<Download>
        val downloadInfoUpdater = DownloadInfoUpdater(LSDownloaderDatabaseManagerWrapper)
        val networkInfoProvider = NetworkInfoProvider(fetchConfiguration.appContext, fetchConfiguration.internetCheckUrl)
        val fetchHandler: LSDownloaderHandler

        init {
            downloadManager = DownloadManagerImpl(
                    httpDownloader = fetchConfiguration.httpDownloader,
                    concurrentLimit = fetchConfiguration.concurrentLimit,
                    progressReportingIntervalMillis = fetchConfiguration.progressReportingIntervalMillis,
                    logger = fetchConfiguration.logger,
                    networkInfoProvider = networkInfoProvider,
                    retryOnNetworkGain = fetchConfiguration.retryOnNetworkGain,
                    downloadInfoUpdater = downloadInfoUpdater,
                    downloadManagerCoordinator = downloadManagerCoordinator,
                    listenerCoordinator = listenerCoordinator,
                    fileServerDownloader = fetchConfiguration.fileServerDownloader,
                    hashCheckingEnabled = fetchConfiguration.hashCheckingEnabled,
                    storageResolver = fetchConfiguration.storageResolver,
                    context = fetchConfiguration.appContext,
                    namespace = fetchConfiguration.namespace,
                    groupInfoProvider = groupInfoProvider,
                    globalAutoRetryMaxAttempts = fetchConfiguration.maxAutoRetryAttempts,
                    preAllocateFileOnCreation = fetchConfiguration.preAllocateFileOnCreation)
            priorityListProcessor = PriorityListProcessorImpl(
                    handlerWrapper = handlerWrapper,
                    downloadProvider = downloadProvider,
                    downloadManager = downloadManager,
                    networkInfoProvider = networkInfoProvider,
                    logger = fetchConfiguration.logger,
                    listenerCoordinator = listenerCoordinator,
                    downloadConcurrentLimit = fetchConfiguration.concurrentLimit,
                    context = fetchConfiguration.appContext,
                    namespace = fetchConfiguration.namespace,
                    prioritySort = fetchConfiguration.prioritySort)
            priorityListProcessor.globalNetworkType = fetchConfiguration.globalNetworkType
            fetchHandler = fetchConfiguration.fetchHandler ?: LSDownloaderHandlerImpl(
                    namespace = fetchConfiguration.namespace,
                    fetchDatabaseManagerWrapper = LSDownloaderDatabaseManagerWrapper,
                    downloadManager = downloadManager,
                    priorityListProcessor = priorityListProcessor,
                    logger = fetchConfiguration.logger,
                    autoStart = fetchConfiguration.autoStart,
                    httpDownloader = fetchConfiguration.httpDownloader,
                    fileServerDownloader = fetchConfiguration.fileServerDownloader,
                    listenerCoordinator = listenerCoordinator,
                    uiHandler = uiHandler,
                    storageResolver = fetchConfiguration.storageResolver,
                    fetchNotificationManager = fetchConfiguration.fetchNotificationManager,
                    groupInfoProvider = groupInfoProvider,
                    prioritySort = fetchConfiguration.prioritySort,
                    createFileOnEnqueue = fetchConfiguration.createFileOnEnqueue)
            LSDownloaderDatabaseManagerWrapper.delegate = object : LSDownloaderDatabaseManager.Delegate<DownloadInfo> {
                override fun deleteTempFilesForDownload(downloadInfo: DownloadInfo) {
                    val tempDir = fetchConfiguration.storageResolver
                            .getDirectoryForFileDownloaderTypeParallel(getRequestForDownload(downloadInfo))
                    deleteAllInFolderForId(downloadInfo.id, tempDir)
                }
            }
        }

    }

}