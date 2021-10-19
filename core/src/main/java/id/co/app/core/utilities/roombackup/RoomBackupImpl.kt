package id.co.app.core.utilities.roombackup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.room.RoomDatabase
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.comparator.LastModifiedFileComparator
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Lukas Kristianto on 5/21/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

class RoomBackupImpl(
	val context: Context,
	val roomDatabase: RoomDatabase
) : RoomBackup {

	companion object {
		private const val SHARED_PREFS = "com.forestry.plantation.roombackuprestore"
		private var TAG = "debug_RoomBackup"
		private lateinit var INTERNAL_BACKUP_PATH: File
		private lateinit var TEMP_BACKUP_PATH: File
		private lateinit var TEMP_BACKUP_FILE: File
		private lateinit var EXTERNAL_BACKUP_PATH: File
		private lateinit var DATABASE_FILE: File

		const val TITLE = "TITLE"
		const val FILE_NAME = "FILE_NAME"
		const val PATH = "PATH"
		const val CREATED_DATE = "CREATED_DATE"
		const val PERNR = "PERNR"
	}

	private lateinit var sharedPreferences: SharedPreferences
	private lateinit var dbName: String

	private var enableLogDebug: Boolean = id.co.app.core.BuildConfig.DEBUG
	private var restartIntent: Intent? = null
	private var customRestoreDialogTitle: String = "Choose file to restore"
	private var customBackupFileName: String? = null
	private var useExternalStorage: Boolean = true
	private val backupIsEncrypted: Boolean = true
	private val encryptPassword: String = "MY_CUSTOM_PREFO_SECURE"


	/**
	 * Set LogDebug enabled / disabled
	 *
	 * @param enableLogDebug Boolean
	 */
	fun enableLogDebug(enableLogDebug: Boolean): RoomBackup {
		this.enableLogDebug = enableLogDebug
		return this
	}

	/**
	 * Set Intent in which to boot after App restart
	 *
	 * @param restartIntent Intent
	 */
	fun restartApp(restartIntent: Intent): RoomBackup {
		this.restartIntent = restartIntent
		restartApp()
		return this
	}

	/**
	 * Init vars, and return true if no error occurred
	 */
	private fun initRoomBackup(): Boolean {

		//Create or retrieve the Master Key for encryption/decryption
		val masterKeyAlias = MasterKey.Builder(context)
			.setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
			.build()

		//Initialize/open an instance of EncryptedSharedPreferences
		//Encryption key is stored in plain text in an EncryptedSharedPreferences --> it is saved encrypted
		sharedPreferences = EncryptedSharedPreferences.create(
			context,
			SHARED_PREFS,
			masterKeyAlias,
			EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
			EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
		)

		dbName = roomDatabase.openHelper.databaseName ?: ""
		INTERNAL_BACKUP_PATH = File("${context.filesDir}/databasebackup/")
		TEMP_BACKUP_PATH = File("${context.filesDir}/databasebackup-temp/")
		TEMP_BACKUP_FILE = File("$TEMP_BACKUP_PATH/tempbackup.sqlite3")
		EXTERNAL_BACKUP_PATH = File(context.getExternalFilesDir("backup")!!.toURI())
		DATABASE_FILE = File(context.getDatabasePath(dbName).toURI())

		//Create internal and temp backup directory if does not exist
		try {
			INTERNAL_BACKUP_PATH.mkdirs()
			TEMP_BACKUP_PATH.mkdirs()
		} catch (e: FileAlreadyExistsException) {
		} catch (e: IOException) {
		}

		if (enableLogDebug) {
			Log.d(TAG, "DatabaseName: $dbName")
			Log.d(TAG, "Database Location: $DATABASE_FILE")
			Log.d(TAG, "INTERNAL_BACKUP_PATH: $INTERNAL_BACKUP_PATH")
			Log.d(TAG, "EXTERNAL_BACKUP_PATH: $EXTERNAL_BACKUP_PATH")
		}
		return true

	}

	/**
	 * restart App with custom Intent
	 */
	private fun restartApp() {
		restartIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		context.startActivity(restartIntent)
		//   finish()
		Runtime.getRuntime().exit(0)

	}

	/**
	 * Start Backup process, and set onComplete Listener to success, if no error occurred, else onComplete Listener success is false and error message is passed
	 */
	override fun backup(pernr: String) = flow {
		emit(0)
		if (enableLogDebug) Log.d(TAG, "Starting Backup ...")
		if (initRoomBackup()) {
			emit(10)
			//Close the database
			roomDatabase.close()

			//Create name for backup file, if no custom name is set: Database name + currentTime + .sqlite3
			var filename =
				if (customBackupFileName == null) "${getTime()}-$pernr.sqlite3" else customBackupFileName as String
			emit(25)
			//Add .aes extension to filename, if file is encrypted
			if (backupIsEncrypted) filename += ".aes"
			emit(45)
			//Path to save current database
			val backuppath =
				if (useExternalStorage) File("$EXTERNAL_BACKUP_PATH/$filename") else File("$INTERNAL_BACKUP_PATH/$filename")
			emit(70)
			if (backupIsEncrypted) encryptBackupFile(backuppath)
			else {
				//Copy current database to save location (/files dir)
				copy(DATABASE_FILE, backuppath)

				if (enableLogDebug) Log.d(TAG, "Saved to: $backuppath")

			}
			emit(100)
		} else {
			throw Throwable("Wrong initialization database")
		}
	}

	/**
	 * Encrypt backup file, and save it
	 *
	 * @param backuppath Path, where to save the backup (internal or external storage)
	 */
	private fun encryptBackupFile(backuppath: File) {
		try {

			//Copy database you want to backup to temp directory
			copy(DATABASE_FILE, TEMP_BACKUP_FILE)


			//encrypt temp file, and save it to backup location
			val encryptDecryptBackup = AESEncryptionHelper()
			val fileData = encryptDecryptBackup.readFile(TEMP_BACKUP_FILE)

			val aesEncryptionManager = AESEncryptionManager()
			val encryptedBytes =
				aesEncryptionManager.encryptData(sharedPreferences, encryptPassword, fileData)

			encryptDecryptBackup.saveFile(encryptedBytes, backuppath)

			//Delete temp file
			TEMP_BACKUP_FILE.delete()

			if (enableLogDebug) Log.d(TAG, "Saved and encrypted to: $backuppath")


		} catch (e: Exception) {
			e.printStackTrace()
			if (enableLogDebug) Log.d(TAG, "error during encryption: ${e.message}")
			return
			//    throw Exception("error during encryption: $e")
		}
	}

	/**
	 * Decrypt backup file, and save it
	 *
	 * @param backuppath Path, where to find the backup to restore (internal or external storage)
	 */
	private fun decryptBackupFile(backuppath: File) {
		try {
			//Copy database you want to restore to temp directory
			copy(backuppath, TEMP_BACKUP_FILE)

			//Decrypt temp file, and save it to database location
			val encryptDecryptBackup = AESEncryptionHelper()
			val fileData = encryptDecryptBackup.readFile(TEMP_BACKUP_FILE)

			val aesEncryptionManager = AESEncryptionManager()
			val decryptedBytes =
				aesEncryptionManager.decryptData(sharedPreferences, encryptPassword, fileData)

			encryptDecryptBackup.saveFile(decryptedBytes, DATABASE_FILE)

			//Delete tem file
			TEMP_BACKUP_FILE.delete()

			if (enableLogDebug) Log.d(TAG, "restored and decrypted from / to $backuppath")

		} catch (e: Exception) {
			if (enableLogDebug) Log.d(TAG, "error during decryption: ${e.message}")
			e.printStackTrace()
			return
			//   throw Exception("error during decryption: $e")
		}

	}

	/**
	 * @return current time formatted as String
	 */
	private fun getTime(): String {

		val currentTime = Calendar.getInstance().time

		val sdf = if (android.os.Build.VERSION.SDK_INT <= 28 && useExternalStorage) {
			SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
		} else {
			SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
		}

		return sdf.format(currentTime)

	}

	/**
	 * If maxFileCount is set, and reached, all old files will be deleted
	 *
	 * @return true if old files deleted or nothing to do
	 */
	override fun deleteOldBackup(): Flow<Boolean> = flow {
		//Path of Backup Directory
		val backupDirectory =
			if (useExternalStorage) File("$EXTERNAL_BACKUP_PATH/") else INTERNAL_BACKUP_PATH

		//All Files in an Array of type File
		val arrayOfFiles = backupDirectory.listFiles()

		//If array is null or empty nothing to do and return
		if (arrayOfFiles.isNullOrEmpty()) {
			if (enableLogDebug) Log.d(TAG, "")
			emit(false)
		} else if (arrayOfFiles.size > 20) {
			//Sort Array: lastModified
			Arrays.sort(arrayOfFiles, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR)

			//Get count of files to delete
			val fileCountToDelete = arrayOfFiles.size - 20

			for (i in 1..fileCountToDelete) {
				//Delete all old files (i-1 because array starts a 0)
				arrayOfFiles[i - 1].delete()

				if (enableLogDebug) Log.d(
					TAG,
					"maxFileCount reached: ${arrayOfFiles[i - 1]} deleted"
				)
			}
		}
		emit(true)
	}

	override fun deleteBackupFile(fileName: String): Flow<Boolean> = flow {

		val backuppath = if (useExternalStorage) {
			File("$EXTERNAL_BACKUP_PATH/$fileName")
		} else {
			File("$INTERNAL_BACKUP_PATH/$fileName")
		}

		if (backuppath.exists()) {
			emit(backuppath.delete())
		} else {
			emit(false)
		}
	}

	/**
	 * Start Restore process, and set onComplete Listener to success, if no error occurred, else onComplete Listener success is false and error message is passed
	 * this function shows a list of all available backup files in a MaterialAlertDialog
	 * and calls restoreSelectedFile(filename) to restore selected file
	 */
	fun restore() {
		if (enableLogDebug) Log.d(TAG, "Starting Restore ...")
		val success = initRoomBackup()
		if (!success) return

		//Path of Backup Directory
		val backupDirectory =
			if (useExternalStorage) File("$EXTERNAL_BACKUP_PATH/") else INTERNAL_BACKUP_PATH

		//All Files in an Array of type File
		val arrayOfFiles = backupDirectory.listFiles()

		//If array is null or empty show "error" and return
		if (arrayOfFiles.isNullOrEmpty()) {
			if (enableLogDebug) Log.d(TAG, "No backups available to restore")
			Toast.makeText(context, "No backups available to restore", Toast.LENGTH_SHORT).show()
			return
		}

		//Sort Array: lastModified
		Arrays.sort(arrayOfFiles, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR)

		//New empty MutableList of String
		val mutableListOfFilesAsString = mutableListOf<String>()

		//Add each filename to mutablelistOfFilesAsString
		runBlocking {
			for (i in arrayOfFiles.indices) {
				mutableListOfFilesAsString.add(arrayOfFiles[i].name)
			}
		}

		//Convert MutableList to Array
		val filesStringArray = mutableListOfFilesAsString.toTypedArray()

		//Show MaterialAlertDialog, with all available files, and on click Listener
		MaterialAlertDialogBuilder(context)
			.setTitle(customRestoreDialogTitle)
			.setItems(filesStringArray) { _, which ->
				restoreSelectedFile(filesStringArray[which])
			}
			.setOnCancelListener {
				if (enableLogDebug) Log.d(TAG, "Restore dialog canceled")
			}
			.show()
	}

	override fun restore(fileName: String) = flow {
		if (enableLogDebug) Log.d(TAG, "Starting Restore ...")
		emit(0)
		if (initRoomBackup()) {
			emit(10)
			if (enableLogDebug) Log.d(TAG, "Restore selected file...")
			//Close the database
			roomDatabase.close()

			emit(20)
			val backuppath = if (useExternalStorage) {
				File("$EXTERNAL_BACKUP_PATH/$fileName")
			} else {
				File("$INTERNAL_BACKUP_PATH/$fileName")
			}

			emit(40)
			val fileExtension = backuppath.extension
			if (backupIsEncrypted) {

				emit(70)
				if (fileExtension == "sqlite3") {
					//Copy back database and replace current database, if file is not encrypted
					copy(backuppath, DATABASE_FILE)
					if (enableLogDebug) Log.d(TAG, "File is not encrypted, trying to restore")
					if (enableLogDebug) Log.d(TAG, "Restored File: $backuppath")
				} else decryptBackupFile(backuppath)

			} else {
				if (fileExtension == "aes") {
					if (enableLogDebug) Log.d(
						TAG,
						"Cannot restore database, it is encrypted. Maybe you forgot to add the property .fileIsEncrypted(true)"
					)
					throw Exception("You are trying to restore an encrypted Database, but you did not add the property .fileIsEncrypted(true)")
				} else {
					//Copy back database and replace current database
					copy(backuppath, DATABASE_FILE)
					if (enableLogDebug) Log.d(TAG, "Restored File: $backuppath")
				}
			}
			emit(100)
		} else {
			throw Throwable("Wrong initialization database")
		}
	}

	override fun getListFileName(): List<Map<String, String>> {
		val success = initRoomBackup()
		if (!success) return listOf()

		//Path of Backup Directory
		val backupDirectory =
			if (useExternalStorage) File("$EXTERNAL_BACKUP_PATH/") else INTERNAL_BACKUP_PATH

		//All Files in an Array of type File
		val arrayOfFiles = backupDirectory.listFiles()

		//If array is null or empty show "error" and return
		if (arrayOfFiles.isNullOrEmpty()) {
			if (enableLogDebug) Log.d(TAG, "No backups available to restore")
			return listOf()
		}

		//Sort Array: lastModified
		Arrays.sort(arrayOfFiles, LastModifiedFileComparator.LASTMODIFIED_REVERSE)

		//New empty MutableList of String
		val mutableListOfFilesAsString = mutableListOf<Map<String, String>>()

		//Add each filename to mutablelistOfFilesAsString
		runBlocking {
			for (i in arrayOfFiles.indices) {
				// format {{dateformat}} - {{pernr}}
				val formattingTitle = arrayOfFiles[i].name.split('-')
				val title = formattingTitle.firstOrNull().orEmpty()
				val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
				val dateFormatWithTime = SimpleDateFormat("dd MMM yyyy HH:mm")
				val createdDate = dateFormatWithTime.format(dateFormat.parse(title))
				val pernr = formattingTitle.lastOrNull().orEmpty()
				mutableListOfFilesAsString.add(
					mapOf(
						TITLE to title,
						CREATED_DATE to createdDate,
						PERNR to pernr,
						FILE_NAME to arrayOfFiles[i].name,
						PATH to arrayOfFiles[i].path.replace(
							arrayOfFiles[i].name.split('/').last(),
							""
						)
					)
				)
			}
		}

		return mutableListOfFilesAsString
	}

	/**
	 * Restores the selected file
	 *
	 * @param filename String
	 */
	private fun restoreSelectedFile(filename: String) {
		if (enableLogDebug) Log.d(TAG, "Restore selected file...")
		//Close the database
		roomDatabase.close()

		val backuppath = if (useExternalStorage) {
			File("$EXTERNAL_BACKUP_PATH/$filename")
		} else {
			File("$INTERNAL_BACKUP_PATH/$filename")
		}


		val fileExtension = backuppath.extension
		if (backupIsEncrypted) {

			if (fileExtension == "sqlite3") {
				//Copy back database and replace current database, if file is not encrypted
				copy(backuppath, DATABASE_FILE)
				if (enableLogDebug) Log.d(TAG, "File is not encrypted, trying to restore")
				if (enableLogDebug) Log.d(TAG, "Restored File: $backuppath")
			} else decryptBackupFile(backuppath)
		} else {
			if (fileExtension == "aes") {
				if (enableLogDebug) Log.d(
					TAG,
					"Cannot restore database, it is encrypted. Maybe you forgot to add the property .fileIsEncrypted(true)"
				)
				return
				//      throw Exception("You are trying to restore an encrypted Database, but you did not add the property .fileIsEncrypted(true)")
			}
			//Copy back database and replace current database
			copy(backuppath, DATABASE_FILE)
			if (enableLogDebug) Log.d(TAG, "Restored File: $backuppath")
		}
	}

	private fun copy(path: File, databasePath: File) {
		val inputStream: InputStream = FileInputStream(path)
		inputStream.use { stream ->
			val out: OutputStream = FileOutputStream(databasePath)
			out.use {
				// Transfer bytes from in to out
				val buf = ByteArray(1024)
				var len: Int
				while (stream.read(buf).also { len = it } > 0) {
					out.write(buf, 0, len)
				}
			}
		}

	}

}