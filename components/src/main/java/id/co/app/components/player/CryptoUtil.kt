package id.co.app.components.player

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Lukas Kristianto on 06/07/21 23.43.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
object CryptoUtil {
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES"
    @Throws(Throwable::class)
    fun encrypt(key: String, inputFile: File, outputFile: File) {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile)
    }

    @Throws(Throwable::class)
    fun decrypt(key: String, inputFile: File, outputFile: File) {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile)
    }

    @Throws(Throwable::class)
    private fun doCrypto(
        cipherMode: Int, key: String, inputFile: File,
        outputFile: File
    ) {
        try {
            val secretKey = SecretKeySpec(key.toByteArray(), ALGORITHM)
            val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(cipherMode, secretKey)
            val inputStream = FileInputStream(inputFile)
            val inputBytes = ByteArray(inputFile.length().toInt())
            inputStream.read(inputBytes)
            val outputBytes: ByteArray = cipher.doFinal(inputBytes)
            val outputStream = FileOutputStream(outputFile)
            outputStream.write(outputBytes)
            inputStream.close()
            outputStream.close()
        } catch (ex: NoSuchPaddingException) {
            throw Throwable("Error encrypting/decrypting file", ex)
        } catch (ex: NoSuchAlgorithmException) {
            throw Throwable("Error encrypting/decrypting file", ex)
        } catch (ex: InvalidKeyException) {
            throw Throwable("Error encrypting/decrypting file", ex)
        } catch (ex: BadPaddingException) {
            throw Throwable("Error encrypting/decrypting file", ex)
        } catch (ex: IllegalBlockSizeException) {
            throw Throwable("Error encrypting/decrypting file", ex)
        } catch (ex: IOException) {
            throw Throwable("Error encrypting/decrypting file", ex)
        }
    }
}