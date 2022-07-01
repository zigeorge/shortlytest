package test.geo.shortly.ui.model

import test.geo.shortly.data.local.ShortLink

data class ListHistory(
    val list: List<ShortLink>,
    val copiedLink: Int = -1
) {

}
