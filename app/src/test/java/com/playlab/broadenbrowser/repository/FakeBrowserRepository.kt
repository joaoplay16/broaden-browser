package com.playlab.broadenbrowser.repository

import com.playlab.broadenbrowser.model.TabPage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeBrowserRepository : BrowserRepository {

    private var tabPages: MutableStateFlow<List<TabPage>> = MutableStateFlow(emptyList())

    override fun getTabs(): Flow<List<TabPage>> {
        return tabPages
    }

    override suspend fun insertTabPage(tabPage: TabPage) {
        tabPages.value = tabPages.value.plus(tabPage)
    }

    override suspend fun deleteTabPages(tabPages: List<TabPage>) {
        this.tabPages.value = this.tabPages.value.minus(tabPages.toSet())
    }

    override suspend fun deleteAllTabPages() {
        this.tabPages.value = emptyList()
    }
}