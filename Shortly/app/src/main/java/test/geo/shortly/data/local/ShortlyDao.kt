package test.geo.shortly.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShortlyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLink(shortLink: ShortLink)

    @Delete
    suspend fun deleteLink(shortLink: ShortLink)

    @Query("SELECT * FROM short_links")
    fun observeAllLink(): Flow<List<ShortLink>>

}