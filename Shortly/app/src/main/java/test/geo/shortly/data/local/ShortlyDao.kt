package test.geo.shortly.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShortlyDao {

    /*
    * Insert a shortLink instance DB
    * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLink(shortLink: ShortLink)

    /*
    * Delete shortLink from DB
    * */
    @Delete
    suspend fun deleteLink(shortLink: ShortLink)

    /*
    * Get all shortLinks from DB as a coroutine flow
    * ** Flow is a type that can emit multiple values sequentially, as opposed to suspend functions
    * ** that return only a single value.
    * Room recognizes Flow object and stream column data into the flow object so that coroutine gets
    * update each time a value is inserted, deleted or updated
    * */
    @Query("SELECT * FROM short_links")
    fun observeAllLink(): Flow<List<ShortLink>>

}