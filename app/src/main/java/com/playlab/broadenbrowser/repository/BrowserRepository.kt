package com.playlab.broadenbrowser.repository

import com.playlab.broadenbrowser.model.TabPage
import kotlinx.coroutines.flow.Flow

interface BrowserRepository {
    fun getTabs(): Flow<List<TabPage>>

    suspend fun insertTabPage(tabPage: TabPage)

    suspend fun deleteTabPages(tabPages: List<TabPage>)

    suspend fun deleteAllTabPages()
}