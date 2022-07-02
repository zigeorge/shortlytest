package test.geo.shortly.data.remote

import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query
import test.geo.shortly.data.remote.responses.ShortLinkResponse

/*
* Represents the api interface of the [shrtcode API](https://app.shrtco.de/docs)
* */

interface ShortlyAPI {


    /*
    * Returns retrofit Response of the /shorten POST method
    * @param url is the url user inserts to shorten
    * */
    @POST("shorten")
    suspend fun getShortenedLink(
        @Query("url") url: String
    ) : Response<ShortLinkResponse>

}