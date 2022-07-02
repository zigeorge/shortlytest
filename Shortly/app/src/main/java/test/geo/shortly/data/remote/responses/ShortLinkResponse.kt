package test.geo.shortly.data.remote.responses

/*
* The response data class mirrored from all possible JSON responses of [shrtcode API] (https://app.shrtco.de/docs)
* */

data class ShortLinkResponse(
    val ok: Boolean,
    val result: ShortLinkResult? = null,
    val error_code: Int = 0,
    val error: String = ""
)