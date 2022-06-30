package test.geo.shortly.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import test.geo.shortly.other.Constants.SHORT_LINK_TABLE

@Entity(tableName = SHORT_LINK_TABLE)
class ShortLink (
    var origin: String,
    var shortLink: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if(other?.javaClass != javaClass) return false
        return other.hashCode() == hashCode()
    }

    override fun hashCode(): Int {
        var result = origin.hashCode()
        result = 31 * result + shortLink.hashCode()
        result = 31 * result + (id ?: 0)
        return result
    }

}