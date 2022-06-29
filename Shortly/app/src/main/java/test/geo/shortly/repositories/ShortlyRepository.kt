package test.geo.shortly.repositories

import androidx.lifecycle.LiveData
import test.geo.shortly.data.local.ShortLink
import test.geo.shortly.data.remote.responses.ShortLinkResponse

interface ShortlyRepository {

    suspend fun insertLink(shortLink: String): ShortLinkResponse

    suspend fun deleteLink(shortLink: ShortLink)

    fun getLinkHistory(): LiveData<List<ShortLink>>
}