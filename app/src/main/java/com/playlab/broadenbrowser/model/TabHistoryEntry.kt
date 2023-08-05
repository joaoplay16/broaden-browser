package com.playlab.broadenbrowser.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.playlab.broadenbrowser.ui.utils.Constants.TAB_HISTORY
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TAB_HISTORY)
data class TabHistoryEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tabId: Long = 0,
    val historyPageId: Long = 0,
    val creationTime: Long
): Parcelable