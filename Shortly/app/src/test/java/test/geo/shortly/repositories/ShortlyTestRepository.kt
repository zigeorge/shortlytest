package test.geo.shortly.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import test.geo.shortly.data.local.ShortLink
import test.geo.shortly.data.remote.responses.ShortLinkResponse
import test.geo.shortly.data.remote.responses.ShortLinkResult

class ShortlyTestRepository : ShortlyRepository {

    private val linkHistory = mutableListOf<ShortLink>()

    private val observableLinkHistory = MutableLiveData<List<ShortLink>>(linkHistory)

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun insertLink(shortLink: String): ShortLinkResponse {
        val response = if(shouldReturnNetworkError) {
            ShortLinkResponse(
                ok = false,
                result = null,
                error_code = 500,
                error = "No Internet"
            )
        } else {
            ShortLinkResponse(
                ok = true,
                result = ShortLinkResult(
                    code = "3mcjVS",
                    short_link = "shrtco.de/3mcjVS",
                    full_short_link = "https://shrtco.de/3mcjVS",
                    original_link = "http://ajskdhfgajksdhglkjsahdf."
                )
            )
        }
        if(response.ok) {
            linkHistory.add(
                ShortLink(
                    origin = response.result!!.original_link,
                    shortLink = response.result!!.short_link
                )
            )
        }
        return response
    }

    override suspend fun deleteLink(shortLink: ShortLink) {
        linkHistory.removeIf {
            it.id == shortLink.id
        }
    }

    override fun getLinkHistory(): LiveData<List<ShortLink>> {
        return observableLinkHistory
    }
}