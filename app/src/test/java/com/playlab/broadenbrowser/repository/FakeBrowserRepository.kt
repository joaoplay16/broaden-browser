package com.playlab.broadenbrowser.repository

import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.model.TabHistoryEntry
import com.playlab.broadenbrowser.model.TabPage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class FakeBrowserRepository : BrowserRepository {

    private var tabPages: MutableStateFlow<List<TabPage>> = MutableStateFlow(emptyList())
    private var history: MutableStateFlow<List<HistoryPage>> = MutableStateFlow(emptyList())
    private var tabsHistory: MutableStateFlow<List<TabHistoryEntry>> = MutableStateFlow(emptyList())

    override fun getTabs(): Flow<List<TabPage>> {
        return tabPages
    }

    override suspend fun insertTabPage(tabPage: TabPage): Long {
        val newId = tabPages.value.size + 1
        tabPages.value = tabPages.value.plus(tabPage.copy(id = newId))
        return newId.toLong()
    }

    override suspend fun deleteTabPages(tabPages: List<TabPage>) {
        this.tabPages.value = this.tabPages.value.minus(tabPages.toSet())
    }

    override suspend fun deleteAllTabPages() {
        this.tabPages.value = emptyList()
    }

    override suspend fun editTabPage(tabPage: TabPage): Int {
        return try {
            val tabToEditIndex = tabPages.value.indexOfFirst { it.id == tabPage.id }
            val listWithTheModifiedTab = tabPages.value.toMutableList()
            listWithTheModifiedTab[tabToEditIndex] = tabPage
            tabPages.value = listWithTheModifiedTab
            return if (tabPages.value.contains(tabPage)) 1 else 0
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
            0
        }
    }

    override suspend fun getTab(id: Long): TabPage? {
        return tabPages.value.find { it.id.toLong() == id }
    }

    override suspend fun insertHistoryPage(historyPage: HistoryPage): Long {
        val newId = history.value.size + 1
        history.value = history.value.plus(historyPage.copy(id = newId))
        return newId.toLong()
    }

    override fun getHistory(): Flow<List<HistoryPage>> {
        return history
    }

    override suspend fun deleteHistoryPages(historyPages: List<HistoryPage>) {
        this.history.value = this.history.value.minus(historyPages.toSet())
    }

    override suspend fun deleteAllHistoryPages() {
        this.history.value = emptyList()
    }

    override suspend fun editHistoryPage(historyPage: HistoryPage): Int {
        return try {
            val tabToEditIndex = history.value.indexOfFirst { it.id == historyPage.id }
            if (tabToEditIndex == -1) return 0
            val listWithTheModifiedHistoryPage = history.value.toMutableList()
            listWithTheModifiedHistoryPage[tabToEditIndex] = historyPage
            history.value = listWithTheModifiedHistoryPage
            return if (history.value.contains(historyPage)) 1 else 0
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
            0
        }
    }

    override suspend fun getHistoryPage(id: Long): HistoryPage? {
        return history.value.find { it.id.toLong() == id }
    }

    override suspend fun getTodayLatestHistoryPageByUrl(url: String): HistoryPage? {
        return history.firstOrNull()?.filter { it.url == url }?.maxByOrNull { it.timestamp }
    }

    override suspend fun insertTabHistoryEntry(tabHistoryEntry: TabHistoryEntry): Long {
        val newId = tabsHistory.value.size + 1L
        tabsHistory.value = tabsHistory.value.plus(tabHistoryEntry.copy(id = newId))
        return newId
    }

    override fun getTabHistory(tabId: Long): Flow<List<HistoryPage>> {
        val tabHistoryEntries = this.tabsHistory.value.filter { it.tabId == tabId }
        val historyPages = tabHistoryEntries.mapNotNull { tabHistoryEntry ->
            history.value.find { it.id.toLong() == tabHistoryEntry.historyPageId }
        }
        return flow{emit(historyPages)}
    }

    override suspend fun getLatestEntryFromTabHistory(tabId: Long): HistoryPage? {
        return getTabHistory(tabId).first().maxByOrNull { it.timestamp }
    }

    override suspend fun deleteTabHistory(tabId: Long): Int {
        var deletedCount = 0

        this.tabsHistory.value = this.tabsHistory.value
            .filter {
                if (it.tabId != tabId) true
                else {
                    deletedCount++
                    false
                }
            }

        return deletedCount
    }
}