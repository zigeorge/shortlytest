package test.geo.shortly.repositories

import kotlinx.coroutines.flow.Flow
import test.geo.shortly.data.local.ShortLink
import test.geo.shortly.data.local.ShortlyDao
import test.geo.shortly.data.remote.ShortlyAPI
import test.geo.shortly.data.remote.responses.ShortLinkResponse
import javax.inject.Inject

/*
* Represents the implementation of the ShortlyRepository
* */

class ShortlyRepositoryImpl @Inject constructor(
    private val shortlyDao: ShortlyDao,
    private val shortlyAPI: ShortlyAPI
) : ShortlyRepository {

    /*
    * Returns ShortLinkResponse from ShortlyAPI
    * @param shortLink: String represents a given url to shorten
    * This functions returns unknownError if API respond with failure
    * */
    override suspend fun insertLink(shortLink: String): ShortLinkResponse {
        val response = shortlyAPI.getShortenedLink(shortLink)
        return if (response.isSuccessful) {
            if (response.body() != null && response.body()!!.result != null) {
                response.body()!!
            } else if (response.body() !== null) {
                response.body()!!
            } else {
                unknownError()
            }
        } else unknownError()
    }

    /*
    * Insert a ShortLink instance in DB
    * @param shortLink: ShortLink is the link to be inserted
    * */
    override suspend fun insertLink(shortLink: ShortLink) {
        shortlyDao.insertLink(shortLink)
    }

    /*
    * Returns an instance of ShortLinkResponse in a format so that later can be handled as an error response
    * */
    private fun unknownError(): ShortLinkResponse =
        ShortLinkResponse(
            ok = false,
            result = null,
            error = "Unknown Error",
            error_code = 500
        )


    /*
    * Delete a ShortLink instance from DB
    * @param shortLink: ShortLink is the link to be deleted
    * */
    override suspend fun deleteLink(shortLink: ShortLink) {
        shortlyDao.deleteLink(shortLink)
    }


    /*
    * Returns all shortLink available in DB wrapped in coroutine Flow object
    * ** Flow is a type that can emit multiple values sequentially, as opposed to suspend functions
    * ** that return only a single value.
    * */
    override fun getLinkHistory(): Flow<List<ShortLink>> {
        return shortlyDao.observeAllLink()
    }


}