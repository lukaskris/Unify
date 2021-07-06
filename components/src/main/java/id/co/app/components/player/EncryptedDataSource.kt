package id.co.app.components.player

import android.net.Uri
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.TransferListener
import java.io.File
import java.io.IOException
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Lukas Kristianto on 06/07/21 19.34.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class EncryptedDataSource(private val key: String) : DataSource {
    private var inputStream: CipherInputStream? = null
    private lateinit var uri: Uri

    override fun addTransferListener(transferListener: TransferListener) {}

    override fun open(dataSpec: DataSpec): Long {
        uri = dataSpec.uri
        try {
            val file = File(uri.path)
            val secretKey = SecretKeySpec(key.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            inputStream = CipherInputStream(file.inputStream(), cipher)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dataSpec.length
    }

    @Throws(IOException::class)
    override fun read(buffer: ByteArray, offset: Int, readLength: Int): Int =
        if (readLength == 0) {
            0
        } else {
            inputStream?.read(buffer, offset, readLength) ?: 0
        }

    override fun getUri(): Uri = uri

    @Throws(IOException::class)
    override fun close() {
        inputStream?.close()
    }

    private fun generateSecretKey(password: String, iv: ByteArray?): SecretKey {
        //convert random string to secretKey
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), iv, 65536, 128) // AES-128
        val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val key = secretKeyFactory.generateSecret(spec).encoded
        return SecretKeySpec(key, "AES")
    }

}