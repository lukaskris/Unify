package id.co.app.lsdownloader.core.base

import id.co.app.lsdownloader.utils.DEFAULT_LOGGING_ENABLED
import id.co.app.lsdownloader.utils.DEFAULT_TAG
import timber.log.Timber

/**
 * The default Fetch Logger.
 * */
open class LSDownloaderLogger(loggingEnabled: Boolean, loggingTag: String) : Logger {

    constructor() : this(DEFAULT_LOGGING_ENABLED, DEFAULT_TAG)

    /** Enable or disable logging.*/
    override var enabled: Boolean = loggingEnabled

    /** Sets the logging tag name. If the tag
     * name is more than 23 characters the default
     * tag name will be used as the tag.*/
    var tag: String = loggingTag

    private val loggingTag: String
        get() {
            return if (tag.length > 23) {
                DEFAULT_TAG
            } else {
                tag
            }
        }

    override fun d(message: String) {
        if (enabled) {
            Timber.tag(loggingTag).d(message)
        }
    }

    override fun d(message: String, throwable: Throwable) {
        if (enabled) {
            Timber.tag(loggingTag).d(throwable, message)
        }
    }

    override fun e(message: String) {
        if (enabled) {
            Timber.tag(loggingTag).e(message)
        }
    }

    override fun e(message: String, throwable: Throwable) {
        if (enabled) {
            Timber.tag(loggingTag).e(throwable, message)
        }
    }

}