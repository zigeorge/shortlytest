package test.geo.shortly.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import test.geo.shortly.other.Constants.SHORT_LINK_TABLE


/*
* Represents a shortened link responded from API
* var origin is original link
* var shortLink is the shortened link
* var id is the id of the item in the table column
* */
@Entity(tableName = SHORT_LINK_TABLE)
class ShortLink (
    var origin: String,
    var shortLink: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
) {

    /*
    * Returns true if two shortLink instances are the same
    * @param other is supposed to be an instance of ShortLink
    * Returns false if other is not an instance of shortLink or not the same
    * */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if(other?.javaClass != javaClass) return false
        return other.hashCode() == hashCode()
    }

    /*
    * Returns the hashCode of a ShortLink instance
    * */
    override fun hashCode(): Int {
        var result = origin.hashCode()
        result = 31 * result + shortLink.hashCode()
        result = 31 * result + (id ?: 0)
        return result
    }

}