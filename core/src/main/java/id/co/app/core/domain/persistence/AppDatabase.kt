package id.co.app.core.domain.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import id.co.app.core.domain.entities.Pokemon
import id.co.app.core.domain.entities.PokemonInfo

@Database(entities = [Pokemon::class, PokemonInfo::class], version = 1, exportSchema = true)
@TypeConverters(value = [TypeResponseConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun pokemonInfoDao(): PokemonInfoDao
}