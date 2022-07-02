package test.geo.shortly.repositories

import kotlinx.coroutines.flow.Flow
import test.geo.shortly.data.local.ShortLink
import test.geo.shortly.data.remote.responses.ShortLinkResponse

/*
* Represents the interface of the ShortlyRepository
* */

interface ShortlyRepository {

    suspend fun insertLink(shortLink: String): ShortLinkResponse

    suspend fun insertLink(shortLink: ShortLink)

    suspend fun deleteLink(shortLink: ShortLink)

    fun getLinkHistory(): Flow<List<ShortLink>>
}