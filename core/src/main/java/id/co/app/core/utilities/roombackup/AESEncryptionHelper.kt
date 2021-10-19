package id.co.app.core.utilities.roombackup


import android.annotation.SuppressLint
import android.content.SharedPreferences
import java.io.*
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Lukas Kristianto on 5/21/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

class AESEncryptionHelper {

	companion object {
		private const val BACKUP_SECRET_KEY = "backupsecretkey"
		private const val TAG = "debug_AESEncryptionHelper"
	}

	/**
	 * This method will convert a file to ByteArray
	 * @param file : the Path where the file is located
	 * @return ByteArray of the file
	 */
	@Throws(Exception::class)
	fun readFile(file: File): ByteArray {
		val fileContents = file.readBytes()
		val inputBuffer = BufferedInputStream(
			FileInputStream(file)
		)
		inputBuffer.read(fileContents)
		inputBuffer.close()
		return fileContents
	}

	/**
	 * This method will convert a ByteArray to a file, and saves it to the path
	 * @param fileData : the ByteArray
	 * @param file : the path where the ByteArray should be saved
	 */
	@Throws(Exception::class)
	fun saveFile(fileData: ByteArray, file: File) {
		val bos = BufferedOutputStream(FileOutputStream(file, false))
		bos.write(fileData)
		bos.flush()
		bos.close()
	}

	/**
	 * This method will convert a random password, saved in sharedPreferences to a SecretKey
	 * @param sharedPref : the sharedPref, to fetch / save the key
	 * @param iv : the encryption nonce
	 * @return SecretKey
	 */
	@SuppressLint("ApplySharedPref")
	fun getSecretKey(sharedPref: SharedPreferences, iv: ByteArray): SecretKey {

		//get key: String from sharedpref
		var password = sharedPref.getString(BACKUP_SECRET_KEY, null)

		//If no key is stored in shared pref, create one and save it
		if (password == null) {
			//generate random string
			val stringLength = 15
			val charset = ('a'..'z') + ('A'..'Z') + ('1'..'9')
			password = (1..stringLength)
				.map { charset.random() }
				.joinToString("")

			val secretKey = generateSecretKey(password, iv)
			//the key can be saved plain, because i am using EncryptedSharedPreferences
			val editor = sharedPref.edit()
			editor.putString(BACKUP_SECRET_KEY, password)
			//I use .commit because when using .apply the needed app restart is faster then apply and the preferences wont be saved
			editor.commit()

			return secretKey
		}

		//generate secretKey, and return it
		return generateSecretKey(password, iv)
	}

	/**
	 * This method will convert a custom password to a SecretKey
	 * @param encryptPassword : the custom user password as String
	 * @param iv : the encryption nonce
	 * @return SecretKey
	 */
	fun getSecretKeyWithCustomPw(encryptPassword: String, iv: ByteArray): SecretKey {
		//generate secretKey, and return it
		return generateSecretKey(encryptPassword, iv)
	}

	/**
	 * Function to generate a 128 bit key from the given password and iv
	 * @param password
	 * @param iv
	 * @return Secret key
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	@Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
	private fun generateSecretKey(password: String, iv: ByteArray?): SecretKey {
		//convert random string to secretKey
		val spec: KeySpec = PBEKeySpec(password.toCharArray(), iv, 65536, 128) // AES-128
		val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
		val key = secretKeyFactory.generateSecret(spec).encoded
		return SecretKeySpec(key, "AES")
	}

}