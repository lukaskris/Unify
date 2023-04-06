package id.co.app.lsdownloader.core.database

import androidx.room.*
import id.co.app.lsdownloader.core.database.DownloadDatabase.Companion.COLUMN_CREATED
import id.co.app.lsdownloader.core.database.DownloadDatabase.Companion.COLUMN_DOWNLOADED
import id.co.app.lsdownloader.core.database.DownloadDatabase.Companion.COLUMN_EXTRAS
import id.co.app.lsdownloader.core.database.DownloadDatabase.Companion.COLUMN_FILE
import id.co.app.lsdownloader.core.database.DownloadDatabase.Companion.COLUMN_GROUP
import id.co.app.lsdownloader.core.database.DownloadDatabase.Companion.COLUMN_ID
import id.co.app.lsdownloader.core.database.DownloadDatabase.Companion.COLUMN_IDENTIFIER
import id.co.app.lsdownloader.core.database.DownloadDatabase.Companion.COLUMN_PRIORITY
import id.co.app.lsdownloader.core.database.DownloadDatabase.Companion.COLUMN_STATUS
import id.co.app.lsdownloader.core.database.DownloadDatabase.Companion.COLUMN_TAG
import id.co.app.lsdownloader.core.database.DownloadDatabase.Companion.COLUMN_TOTAL
import id.co.app.lsdownloader.core.database.DownloadDatabase.Companion.TABLE_NAME
import id.co.app.lsdownloader.model.Status
import kotlinx.coroutines.flow.Flow


@Dao
interface DownloadDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(downloadInfo: DownloadInfo): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(downloadInfoList: List<DownloadInfo>): List<Long>

    @Delete
    fun delete(downloadInfo: DownloadInfo)

    @Delete
    fun delete(downloadInfoList: List<DownloadInfo>)

    @Query("DELETE FROM $TABLE_NAME")
    fun deleteAll()

    @Query("UPDATE $TABLE_NAME SET $COLUMN_DOWNLOADED = :downloaded, $COLUMN_TOTAL = :total, $COLUMN_STATUS = :status where $COLUMN_ID = :id")
    fun updateFileBytesInfoAndStatusOnly(downloaded: Long, total: Long, status: Int, id: Int)

    @Query("UPDATE $TABLE_NAME SET $COLUMN_EXTRAS = :extras WHERE $COLUMN_ID = :id")
    fun updateExtras(extras: String, id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(download: DownloadInfo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(downloadInfoList: List<DownloadInfo>)

    @Query("SELECT * FROM $TABLE_NAME")
    fun get(): List<DownloadInfo>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = :id")
    fun get(id: Int): DownloadInfo?

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID IN (:ids)")
    fun get(ids: List<Int>): List<DownloadInfo>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_FILE = :file")
    fun getByFile(file: String): DownloadInfo?

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_STATUS = :status")
    fun getByStatus(status: Status): List<DownloadInfo>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_STATUS IN (:statuses)")
    fun getByStatus(statuses: List<@JvmSuppressWildcards Status>): List<DownloadInfo>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_GROUP = :group")
    fun getByGroup(group: Int): List<DownloadInfo>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_GROUP = :group AND $COLUMN_STATUS IN (:statuses)")
    fun getByGroupWithStatus(group: Int, statuses: List<@JvmSuppressWildcards Status>): List<DownloadInfo>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_STATUS = :status ORDER BY $COLUMN_PRIORITY DESC, $COLUMN_CREATED ASC")
    fun getPendingDownloadsSorted(status: Status): List<DownloadInfo>

    @Query("SELECT COUNT($COLUMN_ID) FROM $TABLE_NAME WHERE $COLUMN_STATUS in ('1', '2')")
    fun getPendingCountIncludeAddedQuery(): Int

    @Query("SELECT COUNT($COLUMN_ID) FROM $TABLE_NAME WHERE $COLUMN_STATUS in ('1', '2', '3')")
    fun getPendingCountQuery(): Int

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_STATUS = :status ORDER BY $COLUMN_PRIORITY DESC, $COLUMN_CREATED DESC")
    fun getPendingDownloadsSortedDesc(status: Status): List<DownloadInfo>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_IDENTIFIER = :identifier")
    fun getDownloadsByRequestIdentifier(identifier: Long): List<DownloadInfo>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_TAG = :tag")
    fun getDownloadsByTag(tag: String): List<DownloadInfo>

    @Query("SELECT DISTINCT $COLUMN_GROUP from $TABLE_NAME")
    fun getAllGroupIds(): List<Int>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_TAG = :tag limit 1")
    fun getDownloadByTagFlow(tag: String): Flow<DownloadInfo?>
}