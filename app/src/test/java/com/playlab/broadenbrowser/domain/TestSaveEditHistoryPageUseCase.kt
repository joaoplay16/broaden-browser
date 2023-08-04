package com.playlab.broadenbrowser.domain

import com.google.common.truth.Truth.assertThat
import com.playlab.broadenbrowser.MainCoroutineRule
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage1
import com.playlab.broadenbrowser.repository.FakeBrowserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
class TestSaveEditHistoryPageUseCase {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var saveEditHistoryPageUseCase: SaveEditHistoryPageUseCase
    private lateinit var fakeBrowserRepository: FakeBrowserRepository

    @Before
    fun before() = runTest {
        fakeBrowserRepository = FakeBrowserRepository()
        saveEditHistoryPageUseCase = SaveEditHistoryPageUseCase(fakeBrowserRepository)
    }

    @Test
    fun `saving a history page that it's already in today history, just updates the timestamp`() =
        runTest {
            val todayDateInMillis = System.currentTimeMillis()
            val historyPageToSave = historyPage1.copy(
                id = 0,
                timestamp = todayDateInMillis
            )

            saveEditHistoryPageUseCase(historyPageToSave)
            saveEditHistoryPageUseCase(historyPageToSave)
            saveEditHistoryPageUseCase(
                historyPageToSave.copy(
                    timestamp = todayDateInMillis + 1.minutes.inWholeMilliseconds
                )
            )

            val history = fakeBrowserRepository.getHistory().first()

            assertThat(history).isNotEmpty()
            assertThat(history.size).isEqualTo(1)

            val savedHistoryPage = history.first()

            assertThat(savedHistoryPage.timestamp).isGreaterThan(historyPageToSave.timestamp)
        }

    @Test
    fun `save a duplicated history page only if it isn't in the current day's history`() =
        runTest {
            val todayDateInMillis = System.currentTimeMillis()

            saveEditHistoryPageUseCase(
                historyPage1.copy(
                    timestamp = todayDateInMillis - 2.days.inWholeMilliseconds
                )
            )
            saveEditHistoryPageUseCase(
                historyPage1.copy(timestamp = todayDateInMillis)
            )

            val history = fakeBrowserRepository.getHistory().first()

            assertThat(history.size).isEqualTo(2)
        }
}