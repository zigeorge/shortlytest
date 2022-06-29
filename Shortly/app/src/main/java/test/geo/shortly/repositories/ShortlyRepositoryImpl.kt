package test.geo.shortly.repositories

import kotlinx.coroutines.flow.Flow
import test.geo.shortly.data.local.ShortLink
import test.geo.shortly.data.local.ShortlyDao
import test.geo.shortly.data.remote.ShortlyAPI
import test.geo.shortly.data.remote.responses.ShortLinkResponse
import javax.inject.Inject

class ShortlyRepositoryImpl @Inject constructor(
    private val shortlyDao: ShortlyDao,
    private val shortlyAPI: ShortlyAPI
) : ShortlyRepository {
    override suspend fun insertLink(shortLink: String): ShortLinkResponse {
        val response = shortlyAPI.getShortenedLink(shortLink)
        return if (response.isSuccessful) {
            if (response.body() != null && response.body()!!.result != null) {
                shortlyDao.insertLink(
                    ShortLink(
                        response.body()!!.result!!.original_link,
                        response.body()!!.result!!.shortLink
                    )
                )
                response.body()!!
            } else if (response.body() !== null) {
                response.body()!!
            } else {
                unknownError()
            }
        } else unknownError()
    }

    private fun unknownError(): ShortLinkResponse =
        ShortLinkResponse(
            ok = false,
            result = null,
            error = "Unknown Error",
            error_code = 500
        )

    override suspend fun deleteLink(shortLink: ShortLink) {
        shortlyDao.deleteLink(shortLink)
    }

    override fun getLinkHistory(): Flow<List<ShortLink>> {
        return shortlyDao.observeAllLink()
    }


}