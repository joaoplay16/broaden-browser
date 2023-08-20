package com.playlab.broadenbrowser.domain

import com.google.common.truth.Truth.assertThat
import com.playlab.broadenbrowser.MainCoroutineRule
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage1
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage2
import com.playlab.broadenbrowser.mocks.MockTabPages.tab4
import com.playlab.broadenbrowser.repository.FakeBrowserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TestSaveTabHistoryEntryUseCase {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var saveTabHistoryEntryUseCase: SaveTabHistoryEntryUseCase
    private lateinit var repository: FakeBrowserRepository

    @Before
    fun before() {
        repository = FakeBrowserRepository()
        saveTabHistoryEntryUseCase = SaveTabHistoryEntryUseCase(repository)
    }

    @Test
    fun `saving a tab history entry use case`() =
        runTest {
            val tabId = repository.insertTabPage(tab4)

            repository.insertHistoryPage(historyPage1)
            repository.insertHistoryPage(historyPage2)

            val history = repository.getHistory().first()

            history.forEach {
                saveTabHistoryEntryUseCase(
                    tabPage = tab4.copy(id = tabId.toInt()), // tab with updated the id
                    historyPage = it
                )
            }

            val tabHistory = repository.getTabHistory(tabId).first()

            assertThat(tabHistory).isNotEmpty()
            assertThat(tabHistory.size).isEqualTo(2)
        }

    @Test
    fun `save only if the history page isn't the same as the latest saved page`() =
        runTest {
            val tabId = repository.insertTabPage(tab4)
            repository.insertHistoryPage(historyPage1)
            repository.insertHistoryPage(historyPage2)
            repository.insertHistoryPage(historyPage2)

            val history = repository.getHistory().first()

            history.forEach {
                saveTabHistoryEntryUseCase(
                    tabPage = tab4.copy(id = tabId.toInt()), // tab with updated the id
                    historyPage = it
                )
            }

            val tabHistory = repository.getTabHistory(tabId).first()

            assertThat(tabHistory.size).isEqualTo(2)
        }
}