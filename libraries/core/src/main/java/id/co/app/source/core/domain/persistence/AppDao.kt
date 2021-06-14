package id.co.app.source.core.domain.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class AppDao {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}