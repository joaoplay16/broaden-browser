package com.playlab.broadenbrowser.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.playlab.broadenbrowser.ui.utils.Constants.TABS_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TABS_TABLE)
open class TabPage(
    @PrimaryKey
    override val id: Int,
    override val title: String,
    override val url: String,
    override val timestamp: Long
): Parcelable, Page {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Page

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}