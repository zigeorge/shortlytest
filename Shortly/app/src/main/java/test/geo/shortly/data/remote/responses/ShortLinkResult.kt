package test.geo.shortly.data.remote.responses

/*
* Represents the result object for a valid link responded from /shorten API
* */

data class ShortLinkResult (
    val code: String,
    val short_link: String,
    val full_short_link: String,
    val original_link: String,
    val short_link2: String = "",
    val full_short_link2: String = "",
    val short_link3: String = "",
    val full_short_link3: String = "",
    val share_link: String = "",
    val full_share_link: String = ""
)