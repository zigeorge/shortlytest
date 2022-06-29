package test.geo.shortly.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShortlyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLink(shortLink: ShortLink)

    @Delete
    fun deleteLink(shortLink: ShortLink)

    @Query("SELECT * FROM short_links")
    fun observeAllLink(): Flow<List<ShortLink>>
}