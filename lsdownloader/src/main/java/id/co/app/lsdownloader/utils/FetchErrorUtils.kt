@file:JvmName("FetchErrorUtils")

package id.co.app.lsdownloader.utils

import id.co.app.lsdownloader.model.*
import java.io.IOException
import java.net.SocketTimeoutException

fun getErrorFromThrowable(throwable: Throwable): LSDownloaderError {
    var message = throwable.message ?: ""
    if (throwable is SocketTimeoutException && message.isEmpty()) {
        message = CONNECTION_TIMEOUT
    }
    var error = getErrorFromMessage(message)
    error = when {
        error == LSDownloaderError.UNKNOWN && throwable is SocketTimeoutException -> LSDownloaderError.CONNECTION_TIMED_OUT
        error == LSDownloaderError.UNKNOWN && throwable is IOException -> LSDownloaderError.UNKNOWN_IO_LSDownloader_ERROR
        else -> error
    }
    error.throwable = throwable
    return error
}

fun getErrorFromMessage(message: String?): LSDownloaderError {
    return if (message == null || message.isEmpty()) {
        LSDownloaderError.UNKNOWN
    } else if (message.equals(REQUEST_WITH_FILE_PATH_ALREADY_EXIST, true)
            || message.contains(FAILED_TO_ENQUEUE_REQUEST_FILE_FOUND, true)) {
        LSDownloaderError.REQUEST_WITH_FILE_PATH_ALREADY_EXIST
    } else if (message.contains(UNIQUE_ID_DATABASE)) {
        LSDownloaderError.REQUEST_WITH_ID_ALREADY_EXIST
    } else if (message.contains(EMPTY_RESPONSE_BODY, true)) {
        LSDownloaderError.EMPTY_RESPONSE_FROM_SERVER
    } else if (message.equals(FNC, ignoreCase = true) || message.equals(ENOENT, ignoreCase = true)) {
        LSDownloaderError.FILE_NOT_CREATED
    } else if (message.contains(ETIMEDOUT, ignoreCase = true)
            || message.contains(CONNECTION_TIMEOUT, ignoreCase = true)
            || message.contains(SOFTWARE_ABORT_CONNECTION, ignoreCase = true)
            || message.contains(READ_TIME_OUT, ignoreCase = true)) {
        LSDownloaderError.CONNECTION_TIMED_OUT
    } else if (message.equals(IO404, ignoreCase = true) || message.contains(NO_ADDRESS_HOSTNAME)) {
        LSDownloaderError.HTTP_NOT_FOUND
    } else if (message.contains(HOST_RESOLVE_ISSUE)) {
        LSDownloaderError.UNKNOWN_HOST
    } else if (message.equals(EACCES, ignoreCase = true)) {
        LSDownloaderError.WRITE_PERMISSION_DENIED
    } else if (message.equals(ENOSPC, ignoreCase = true)
            || message.equals(DATABASE_DISK_FULL, ignoreCase = true)) {
        LSDownloaderError.NO_STORAGE_SPACE
    } else if (message.equals(FAILED_TO_ENQUEUE_REQUEST, true)) {
        LSDownloaderError.REQUEST_ALREADY_EXIST
    } else if (message.equals(DOWNLOAD_NOT_FOUND, true)) {
        LSDownloaderError.DOWNLOAD_NOT_FOUND
    } else if (message.equals(FETCH_DATABASE_ERROR, true)) {
        LSDownloaderError.FETCH_DATABASE_LSDownloader_ERROR
    } else if (message.contains(RESPONSE_NOT_SUCCESSFUL, true) || message.contains(FAILED_TO_CONNECT, true)) {
        LSDownloaderError.REQUEST_NOT_SUCCESSFUL
    } else if (message.contains(INVALID_CONTENT_HASH, true)) {
        LSDownloaderError.INVALID_CONTENT_HASH
    } else if (message.contains(DOWNLOAD_INCOMPLETE, true)) {
        LSDownloaderError.UNKNOWN_IO_LSDownloader_ERROR
    } else if (message.contains(FAILED_TO_UPDATE_REQUEST, true)) {
        LSDownloaderError.FAILED_TO_UPDATE_REQUEST
    } else if (message.contains(FAILED_TO_ADD_COMPLETED_DOWNLOAD, true)) {
        LSDownloaderError.FAILED_TO_ADD_COMPLETED_DOWNLOAD
    } else if (message.contains(FETCH_FILE_SERVER_INVALID_RESPONSE_TYPE, true)) {
        LSDownloaderError.FETCH_FILE_SERVER_INVALID_RESPONSE
    } else if (message.contains(REQUEST_DOES_NOT_EXIST, true)) {
        LSDownloaderError.REQUEST_DOES_NOT_EXIST
    } else if (message.contains(NO_NETWORK_CONNECTION, true)) {
        LSDownloaderError.NO_NETWORK_CONNECTION
    } else if (message.contains(FILE_NOT_FOUND, true)) {
        LSDownloaderError.FILE_NOT_FOUND
    } else if (message.contains(FETCH_FILE_SERVER_URL_INVALID, true)) {
        LSDownloaderError.FETCH_FILE_SERVER_URL_INVALID
    } else if (message.contains(ENQUEUED_REQUESTS_ARE_NOT_DISTINCT, true)) {
        LSDownloaderError.ENQUEUED_REQUESTS_ARE_NOT_DISTINCT
    } else if (message.contains(ENQUEUE_NOT_SUCCESSFUL, true)) {
        LSDownloaderError.ENQUEUE_NOT_SUCCESSFUL
    } else if(message.contains(FAILED_RENAME_FILE_ASSOCIATED_WITH_INCOMPLETE_DOWNLOAD, true)) {
        LSDownloaderError.FAILED_TO_RENAME_INCOMPLETE_DOWNLOAD_FILE
    } else if(message.contains(FILE_CANNOT_BE_RENAMED, true)) {
        LSDownloaderError.FAILED_TO_RENAME_FILE
    } else if(message.contains(FILE_ALLOCATION_ERROR, true)) {
        LSDownloaderError.FILE_ALLOCATION_FAILED
    }  else if(message.contains(CLEAR_TEXT_NETWORK_VIOLATION, true)) {
        LSDownloaderError.HTTP_CONNECTION_NOT_ALLOWED
    } else {
        LSDownloaderError.UNKNOWN
    }

}