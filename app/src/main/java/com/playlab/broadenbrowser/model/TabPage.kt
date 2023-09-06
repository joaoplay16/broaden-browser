package com.playlab.broadenbrowser.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.playlab.broadenbrowser.ui.utils.Constants.TABS_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TABS_TABLE)
data class TabPage(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    override val title: String,
    override val url: String,
    override val timestamp: Long,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val historyByteArray: ByteArray? = null
): Parcelable, Page {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TabPage

        if (id != other.id) return false
        if (title != other.title) return false
        if (url != other.url) return false
        if (historyByteArray != null) {
            if (other.historyByteArray == null) return false
            if (!historyByteArray.contentEquals(other.historyByteArray)) return false
        } else if (other.historyByteArray != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + (historyByteArray?.contentHashCode() ?: 0)
        return result
    }
}