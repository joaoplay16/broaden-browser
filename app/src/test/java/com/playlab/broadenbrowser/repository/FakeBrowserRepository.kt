package com.playlab.broadenbrowser.repository

import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.model.TabPage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull

class FakeBrowserRepository : BrowserRepository {

    private var tabPages: MutableStateFlow<List<TabPage>> = MutableStateFlow(emptyList())
    private var history: MutableStateFlow<List<HistoryPage>> = MutableStateFlow(emptyList())

    override fun getTabs(): Flow<List<TabPage>> {
        return tabPages
    }

    override suspend fun insertTabPage(tabPage: TabPage): Long {
        tabPages.value = tabPages.value.plus(tabPage)
        return tabPages.value.size.toLong()
    }

    override suspend fun deleteTabPages(tabPages: List<TabPage>) {
        this.tabPages.value = this.tabPages.value.minus(tabPages.toSet())
    }

    override suspend fun deleteAllTabPages() {
        this.tabPages.value = emptyList()
    }

    override suspend fun editTabPage(tabPage: TabPage): Int {
        val tabToEditIndex = tabPages.value.indexOfFirst { it.id == tabPage.id }
        val listWithTheModifiedTab = tabPages.value.toMutableList()
        listWithTheModifiedTab[tabToEditIndex] = tabPage
        tabPages.value = listWithTheModifiedTab

        return if (tabPages.value.contains(tabPage)) 1 else 0
    }

    override suspend fun getTab(id: Long): TabPage? {
        return tabPages.value.find { it.id.toLong() == id }
    }

    override suspend fun insertHistoryPage(historyPage: HistoryPage): Long {
        history.value = history.value.plus(historyPage)
        return history.value.size.toLong()
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
        val tabToEditIndex = history.value.indexOfFirst { it.id == historyPage.id }
        val listWithTheModifiedHistoryPage = history.value.toMutableList()
        listWithTheModifiedHistoryPage[tabToEditIndex] = historyPage
        history.value = listWithTheModifiedHistoryPage

        return if (history.value.contains(historyPage)) 1 else 0
    }

    override suspend fun getHistoryPage(id: Long): HistoryPage? {
        return history.value.find { it.id.toLong() == id }
    }

    override suspend fun getTodayLatestHistoryPageByUrl(url: String): HistoryPage? {
        return history.firstOrNull()?.filter { it.url == url }?.maxByOrNull { it.timestamp }
    }
}