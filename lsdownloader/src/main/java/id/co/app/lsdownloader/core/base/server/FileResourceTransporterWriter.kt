package id.co.app.lsdownloader.core.base.server

/**
 * Created by Lukas Kristianto on 23/09/22 09.59.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
interface FileResourceTransporterWriter {

    fun sendFileRequest(fileRequest: FileRequest)

    fun sendFileResponse(fileResponse: FileResponse)

    fun sendRawBytes(byteArray: ByteArray, offset: Int, length: Int)

}