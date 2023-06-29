package com.playlab.broadenbrowser.repository

import com.playlab.broadenbrowser.data.local.BrowserDatabase
import com.playlab.broadenbrowser.model.TabPage
import javax.inject.Inject

class BrowserRepository @Inject constructor(
    private val db: BrowserDatabase
) {

    fun getTabs() = db.browserDao().getTabPages()

    suspend fun insertTabPage(tabPage: TabPage) {
        db.browserDao().insertTabPage(tabPage)
    }

    suspend fun deleteTabPage(tabPages: List<TabPage>) {
        db.browserDao().deleteTabPages(tabPages)
    }

    suspend fun deleteAllTabPages() {
        db.browserDao().deleteAllTabPages()
    }
}