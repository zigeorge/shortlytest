package test.geo.shortly.repositories

import kotlinx.coroutines.flow.Flow
import test.geo.shortly.data.local.ShortLink
import test.geo.shortly.data.remote.responses.CommonResponse

interface ShortlyRepository {

    suspend fun insertLink(shortLink: String): CommonResponse

    suspend fun deleteLink(shortLink: ShortLink)

    fun getLinkHistory(): Flow<List<ShortLink>>
}