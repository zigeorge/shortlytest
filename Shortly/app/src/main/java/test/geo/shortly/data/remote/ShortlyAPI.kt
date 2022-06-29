package test.geo.shortly.data.remote

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query
import test.geo.shortly.data.remote.responses.ShortLinkResponse

interface ShortlyAPI {

    @POST("shorten")
    suspend fun getShortenedLink(
        @Query("url") url: String
    ) : Response<ShortLinkResponse>

}