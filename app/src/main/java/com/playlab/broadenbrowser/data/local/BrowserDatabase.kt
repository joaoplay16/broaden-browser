package com.playlab.broadenbrowser.data.local

import androidx.room.RoomDatabase
import com.playlab.broadenbrowser.data.local.dao.BrowserDao

abstract class BrowserDatabase : RoomDatabase() {
    abstract fun browserDao(): BrowserDao
}