package id.co.app.lsdownloader.core.base

/**
 * Fetch observer for groups. This observer also specifies which download
 * triggers the onChanged in the group.
 * */
interface LSDownloaderGroupObserver: LSDownloaderObserver<List<Download>> {

    /**
     * Method called when the download list has changed.
     * @param data the download list.
     * @param triggerDownload the download that triggered the change.
     * @param reason the reason why onChanged was called for the triggered download.
     * */
    fun onChanged(data: List<Download>, triggerDownload: Download, reason: Reason)

}