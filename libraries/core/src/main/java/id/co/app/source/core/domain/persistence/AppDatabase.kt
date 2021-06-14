package id.co.app.source.core.domain.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [AppDao::class], version = 1, exportSchema = true)
@TypeConverters(value = [])
abstract class AppDatabase : RoomDatabase() {
}