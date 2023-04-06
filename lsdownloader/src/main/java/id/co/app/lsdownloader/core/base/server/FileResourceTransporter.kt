package id.co.app.lsdownloader.core.base.server

import java.io.InputStream
import java.io.OutputStream
import java.net.SocketAddress

/**
 * Created by Lukas Kristianto on 23/09/22 09.59.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
interface FileResourceTransporter : FileResourceTransporterWriter {

    val isClosed: Boolean

    fun connect(socketAddress: SocketAddress)

    fun receiveFileRequest(): FileRequest?

    fun receiveFileResponse(): FileResponse?

    fun readRawBytes(byteArray: ByteArray, offset: Int, length: Int): Int

    fun getInputStream(): InputStream

    fun getOutputStream(): OutputStream

    fun close()

    companion object {
        const val BUFFER_SIZE = 8192
    }

}