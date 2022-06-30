package test.geo.shortly.ui.model

import test.geo.shortly.data.local.ShortLink

data class LinkHistoryListItem(
    val shortLink: ShortLink,
    var isCopied: Boolean = false
) {
    companion object {
        fun fromAllLinks(allLink: List<ShortLink>, copiedLink: LinkHistoryListItem?): List<LinkHistoryListItem> {
            val linkHistory = ArrayList<LinkHistoryListItem>()
            allLink.forEach {
                if(copiedLink != null && copiedLink.shortLink == it)
                    linkHistory.add(LinkHistoryListItem(it, true))
                else linkHistory.add(LinkHistoryListItem(it))
            }
            return linkHistory
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other?.javaClass != javaClass) return false
        return other.hashCode() == hashCode()
    }

    override fun hashCode(): Int {
        var result = shortLink.hashCode()
        result = 31 * result + isCopied.hashCode()
        return result
    }
}