package com.playlab.broadenbrowser.repository

import com.playlab.broadenbrowser.data.local.BrowserDatabase
import com.playlab.broadenbrowser.model.TabPage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultBrowserRepository @Inject constructor(
    private val db: BrowserDatabase
) : BrowserRepository{

    override fun getTabs(): Flow<List<TabPage>> = db.browserDao().getTabPages()

    override suspend fun insertTabPage(tabPage: TabPage): Long {
        return db.browserDao().insertTabPage(tabPage)
    }

    override suspend fun deleteTabPages(tabPages: List<TabPage>) {
        db.browserDao().deleteTabPages(tabPages)
    }

    override suspend fun deleteAllTabPages() {
        db.browserDao().deleteAllTabPages()
    }
}