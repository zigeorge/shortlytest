package test.geo.shortly.data.remote.responses

data class ShortLinkResult (
    val code: String,
    val short_link: String,
    val full_short_link: String,
    val original_link: String
)