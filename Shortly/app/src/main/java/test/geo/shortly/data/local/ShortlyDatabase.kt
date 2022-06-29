package test.geo.shortly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShortLink::class],
    version = 1
)

abstract class ShortlyDatabase: RoomDatabase() {

    abstract fun shortlyDao(): ShortlyDao
}