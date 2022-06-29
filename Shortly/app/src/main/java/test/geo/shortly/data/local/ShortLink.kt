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
)