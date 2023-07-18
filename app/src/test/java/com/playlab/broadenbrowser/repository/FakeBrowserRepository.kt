package com.playlab.broadenbrowser.repository

import com.playlab.broadenbrowser.model.TabPage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeBrowserRepository : BrowserRepository {

    private var tabPages: MutableStateFlow<List<TabPage>> = MutableStateFlow(emptyList())

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
}