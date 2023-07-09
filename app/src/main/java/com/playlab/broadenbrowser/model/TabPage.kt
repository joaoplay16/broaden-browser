package com.playlab.broadenbrowser.model

import android.os.Parcelable
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
    override val timestamp: Long
): Parcelable, Page