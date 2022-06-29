package test.geo.shortly.data.remote.responses

data class ShortLinkResponse(
    val ok: Boolean,
    val result: ShortLinkResult? = null,
    val error_code: Int,
    val error: String = "Unknown Error"
)