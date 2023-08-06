package com.playlab.broadenbrowser.repository

import com.playlab.broadenbrowser.data.local.BrowserDatabase
import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.model.TabHistoryEntry
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

    override suspend fun editTabPage(tabPage: TabPage): Int {
        return db.browserDao().editTabPage(tabPage)
    }

    override suspend fun getTab(id: Long): TabPage? {
        return db.browserDao().getTab(id)
    }

    override fun getHistory(): Flow<List<HistoryPage>> {
        return db.browserDao().getHistory()
    }

    override suspend fun insertHistoryPage(historyPage: HistoryPage): Long {
        return db.browserDao().insertHistoryPage(historyPage)
    }

    override suspend fun deleteHistoryPages(historyPages: List<HistoryPage>) {
        db.browserDao().deleteHistoryPages(historyPages)
    }

    override suspend fun deleteAllHistoryPages() {
        db.browserDao().deleteAllHistoryPages()
    }

    override suspend fun editHistoryPage(historyPage: HistoryPage): Int {
        return db.browserDao().editHistoryPage(historyPage)
    }

    override suspend fun getHistoryPage(id: Long): HistoryPage? {
        return db.browserDao().getHistoryPage(id)
    }

    override suspend fun getTodayLatestHistoryPageByUrl(url: String): HistoryPage? {
        return db.browserDao().getTodayLatestHistoryPageByUrl(url)
    }

    override suspend fun insertTabHistoryEntry(tabHistoryEntry: TabHistoryEntry): Long {
        return db.browserDao().insertTabHistoryEntry(tabHistoryEntry)
    }

    override fun getTabHistory(tabId: Long): Flow<List<HistoryPage>> {
        return db.browserDao().getTabHistory(tabId)
    }
}