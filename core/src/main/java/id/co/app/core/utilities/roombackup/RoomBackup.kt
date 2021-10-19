package id.co.app.core.utilities.roombackup

import kotlinx.coroutines.flow.Flow


/**
 * Created by Lukas Kristianto on 5/21/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
interface RoomBackup {
	fun restore(fileName: String): Flow<Int>
	fun backup(pernr: String): Flow<Int>
	fun deleteBackupFile(fileName: String): Flow<Boolean>
	fun getListFileName(): List<Map<String, String>>
	fun deleteOldBackup(): Flow<Boolean>
}