package com.playlab.broadenbrowser.repository

import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.model.TabHistoryEntry
import com.playlab.broadenbrowser.model.TabPage
import kotlinx.coroutines.flow.Flow

interface BrowserRepository {
    fun getTabs(): Flow<List<TabPage>>

    suspend fun insertTabPage(tabPage: TabPage): Long

    suspend fun deleteTabPages(tabPages: List<TabPage>)

    suspend fun deleteAllTabPages()

    suspend fun editTabPage(tabPage: TabPage): Int

    suspend fun getTab(id: Long): TabPage?

    fun getHistory(): Flow<List<HistoryPage>>

    suspend fun insertHistoryPage(historyPage: HistoryPage): Long

    suspend fun deleteHistoryPages(historyPages: List<HistoryPage>)

    suspend fun deleteAllHistoryPages()

    suspend fun editHistoryPage(historyPage: HistoryPage): Int

    suspend fun getHistoryPage(id: Long): HistoryPage?

    suspend fun getTodayLatestHistoryPageByUrl(url: String): HistoryPage?

    suspend fun insertTabHistoryEntry(tabHistoryEntry: TabHistoryEntry): Long

    fun getTabHistory(tabId:Long): Flow<List<HistoryPage>>

    suspend fun deleteTabHistory(tabId: Long): Int
}