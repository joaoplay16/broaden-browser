package com.playlab.broadenbrowser.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.playlab.broadenbrowser.ui.utils.Constants.HISTORY_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = HISTORY_TABLE)
data class HistoryPage(
    @PrimaryKey
    override val id: Int,
    override val title: String,
    override val url: String,
    override val timestamp: Long
): Parcelable, Page