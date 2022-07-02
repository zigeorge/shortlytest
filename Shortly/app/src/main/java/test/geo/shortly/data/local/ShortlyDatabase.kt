package test.geo.shortly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/*
* This abstraction represents the Room database instance of the application
* The annotation state that there is only one entity/table in the database, represented by ShortLink::class
* The version of the Database is 1
* */

@Database(
    entities = [ShortLink::class],
    version = 1
)

abstract class ShortlyDatabase: RoomDatabase() {

    abstract fun shortlyDao(): ShortlyDao
}