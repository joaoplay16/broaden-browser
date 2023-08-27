package com.playlab.broadenbrowser.model

import com.google.common.truth.Truth.assertThat
import com.playlab.broadenbrowser.mocks.MockHistoryPages.browserHistory
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage1
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage2
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage3
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage4
import org.junit.Test

class TestTabHistoryManager {

    @Test
    fun `test navigate forward`(){
        val tabHistoryManager = TabHistoryManager()

        tabHistoryManager.updateTabHistory(browserHistory)

        assertThat(tabHistoryManager.nextTabHistoryEntry()).isEqualTo(historyPage2)
        assertThat(tabHistoryManager.nextTabHistoryEntry()).isEqualTo(historyPage3)
        assertThat(tabHistoryManager.nextTabHistoryEntry()).isEqualTo(historyPage4)
        assertThat(tabHistoryManager.nextTabHistoryEntry()).isNull()
    }

    @Test
    fun `test navigate back`(){
        val tabHistoryManager = TabHistoryManager()

        tabHistoryManager.updateTabHistory(browserHistory)
        tabHistoryManager.setCurrentHistoryEntry(historyPage4)

        assertThat(tabHistoryManager.previousTabHistoryEntry()).isEqualTo(historyPage3)
        assertThat(tabHistoryManager.previousTabHistoryEntry()).isEqualTo(historyPage2)
        assertThat(tabHistoryManager.previousTabHistoryEntry()).isEqualTo(historyPage1)
        assertThat(tabHistoryManager.previousTabHistoryEntry()).isNull()
    }

    @Test
    fun `test navigate back an forth`(){

        val tabHistoryManager = TabHistoryManager()

        tabHistoryManager.updateTabHistory(browserHistory)
        tabHistoryManager.setCurrentHistoryEntry(historyPage3)

        assertThat(tabHistoryManager.previousTabHistoryEntry()).isEqualTo(historyPage2)
        assertThat(tabHistoryManager.previousTabHistoryEntry()).isEqualTo(historyPage1)
        assertThat(tabHistoryManager.nextTabHistoryEntry()).isEqualTo(historyPage2)
        assertThat(tabHistoryManager.previousTabHistoryEntry()).isEqualTo(historyPage1)
        assertThat(tabHistoryManager.previousTabHistoryEntry()).isNull()
    }

    @Test
    fun `test tab history update`(){

        val tabHistoryManager = TabHistoryManager()

        tabHistoryManager.updateTabHistory(browserHistory)
        tabHistoryManager.setCurrentHistoryEntry(historyPage3)

        assertThat(tabHistoryManager.previousTabHistoryEntry()).isEqualTo(historyPage2)
        assertThat(tabHistoryManager.previousTabHistoryEntry()).isEqualTo(historyPage1)
        assertThat(tabHistoryManager.nextTabHistoryEntry()).isEqualTo(historyPage2)

        // Simulating the tab history updating
        tabHistoryManager.updateTabHistory(listOf(historyPage3, historyPage2, historyPage1))
        tabHistoryManager.setCurrentHistoryEntry(historyPage2)

        assertThat(tabHistoryManager.nextTabHistoryEntry()).isEqualTo(historyPage1)
    }
}