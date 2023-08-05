package com.playlab.broadenbrowser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.playlab.broadenbrowser.data.local.dao.BrowserDao
import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.model.TabHistoryEntry
import com.playlab.broadenbrowser.model.TabPage

@Database(
    entities = [TabPage::class, HistoryPage::class, TabHistoryEntry::class],
    version = 1
)
abstract class BrowserDatabase : RoomDatabase() {
    abstract fun browserDao(): BrowserDao
}