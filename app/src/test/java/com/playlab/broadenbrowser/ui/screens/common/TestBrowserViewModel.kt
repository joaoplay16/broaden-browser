package com.playlab.broadenbrowser.ui.screens.common

import com.google.common.truth.Truth.assertThat
import com.playlab.broadenbrowser.MainCoroutineRule
import com.playlab.broadenbrowser.domain.SaveEditHistoryPageUseCase
import com.playlab.broadenbrowser.domain.SaveEditTabUseCase
import com.playlab.broadenbrowser.domain.SaveTabHistoryEntryUseCase
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage1
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage2
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage3
import com.playlab.broadenbrowser.mocks.MockTabPages.tab1
import com.playlab.broadenbrowser.mocks.MockTabPages.tab2
import com.playlab.broadenbrowser.mocks.MockTabPages.tab3
import com.playlab.broadenbrowser.repository.FakeBrowserRepository
import com.playlab.broadenbrowser.repository.FakePreferencesRepository
import com.playlab.broadenbrowser.ui.utils.SearchMechanism
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
class TestBrowserViewModel {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: BrowserViewModel

    @Before
    fun before() = runTest {
        val fakeBrowserRepository = FakeBrowserRepository()
        viewModel = BrowserViewModel(
            FakePreferencesRepository(),
            fakeBrowserRepository,
            SaveEditHistoryPageUseCase(fakeBrowserRepository),
            SaveEditTabUseCase(fakeBrowserRepository),
            SaveTabHistoryEntryUseCase(fakeBrowserRepository)
        )
    }

    @Test
    fun `save a tab`() = runTest {
        with(viewModel) {
            assertThat(state.value.tabs).isEmpty()
            onUiEvent(UiEvent.OnSaveEditTab(tab1))
            assertThat(state.value.tabs.size).isEqualTo(1)
        }
    }

    @Test
    fun `save a duplicated tab`() = runTest {
        with(viewModel) {
            assertThat(state.value.tabs).isEmpty()
            // set id to 0 to indicate a new tab
            onUiEvent(UiEvent.OnSaveEditTab(tab1.copy(id = 0)))
            onUiEvent(UiEvent.OnSaveEditTab(tab1.copy(id = 0)))
            assertThat(state.value.tabs.size).isEqualTo(2)
        }
    }


    @Test
    fun `set the successfully saved tab as current tab ` () = runTest {
        with(viewModel) {
            onUiEvent(UiEvent.OnSaveEditTab(tab1))

            assertThat(state.value.currentTab).isNotNull()

            assertThat(state.value.currentTab!!.id).isGreaterThan(0)
        }
    }

    @Test
    fun `add a new tab`() = runTest {
        with(viewModel) {
            onUiEvent(UiEvent.OnSaveEditTab(tab1))
            onUiEvent(UiEvent.OnTabChange(null))
            assertThat(state.value.currentTab).isNull()
        }
    }

    @Test
    fun `close selected tabs`() = runTest {
        with(viewModel) {
            // set id to 0 to indicate a new tab
            onUiEvent(UiEvent.OnSaveEditTab(tab1.copy(id = 0)))
            onUiEvent(UiEvent.OnSaveEditTab(tab2.copy(id = 0)))
            onUiEvent(UiEvent.OnSaveEditTab(tab3.copy(id = 0)))

            val tabsToClose = listOf(tab1, tab3)

            onUiEvent(UiEvent.OnCloseTabs(tabsToClose))

            assertThat(state.value.tabs.size).isEqualTo(1)
            assertThat(state.value.tabs).containsExactly(tab2)
        }
    }

    @Test
    fun `close all tabs`() = runTest {
        with(viewModel) {
            // set id to 0 to indicate a new tab
            onUiEvent(UiEvent.OnSaveEditTab(tab1.copy(id = 0)))
            onUiEvent(UiEvent.OnSaveEditTab(tab2.copy(id = 0)))
            onUiEvent(UiEvent.OnSaveEditTab(tab3.copy(id = 0)))

            onUiEvent(UiEvent.OnCloseAllTabs)

            assertThat(state.value.tabs).isEmpty()
        }
    }

    @Test
    fun `enable javascript `() = runTest {
        with(viewModel) {
            assertThat(state.value.isJavascriptAllowed).isTrue()
            onUiEvent(UiEvent.OnAllowJavascript(false))

            assertThat(state.value.isJavascriptAllowed).isFalse()
        }
    }

    @Test
    fun `enable fullscreen`() = runTest {
        with(viewModel) {
            assertThat(state.value.isInFullscreen).isFalse()

            onUiEvent(UiEvent.OnEnableFullscreen(true))

            assertThat(state.value.isInFullscreen).isTrue()
        }
    }

    @Test
    fun `enable in fullscreen startup`() = runTest {
        with(viewModel) {
            assertThat(state.value.isStartInFullscreenEnabled).isFalse()

            onUiEvent(UiEvent.OnEnableStartInFullscreen(true))

            assertThat(state.value.isStartInFullscreenEnabled).isTrue()
        }
    }

    @Test
    fun `enable dark theme`() = runTest {
        with(viewModel) {
            assertThat(state.value.isDarkThemeEnabled).isFalse()

            onUiEvent(UiEvent.OnEnableDarkTheme(true))

            assertThat(state.value.isDarkThemeEnabled).isTrue()
        }
    }

    @Test
    fun `set search engine`() = runTest {
        with(viewModel) {
            assertThat(state.value.searchMechanism).isEqualTo(SearchMechanism.GOOGLE)

            onUiEvent(UiEvent.OnSetSearchMechanism(SearchMechanism.BING))

            assertThat(state.value.searchMechanism).isEqualTo(SearchMechanism.BING)
        }
    }

    @Test
    fun `set the successfully modified tab as current tab` () = runTest {
        with(viewModel) {
            onUiEvent(UiEvent.OnSaveEditTab(tab1))

            val tab1Modified = tab1.copy(
                id = state.value.currentTab!!.id,
                url = "https://m3.material.io",
                title = "Material Design"
            )

            onUiEvent(UiEvent.OnSaveEditTab(tab1Modified))

            assertThat(state.value.currentTab).isEqualTo(tab1Modified)
        }
    }

    @Test
    fun `save a history page`() = runTest {
        with(viewModel) {
            onUiEvent(UiEvent.OnSaveHistoryPage(historyPage1))
            assertThat(state.value.history.size).isEqualTo(1)
        }
    }

    @Test
    fun `saving a history page that it's already in today history, just updates the timestamp`() =
        runTest {

            val todayDateInMillis = System.currentTimeMillis()
            val historyPageToSave = historyPage1.copy(
                id = 0,
                timestamp = todayDateInMillis)

            with(viewModel) {
                onUiEvent(UiEvent.OnSaveHistoryPage(historyPageToSave))
                onUiEvent(UiEvent.OnSaveHistoryPage(historyPageToSave))
                onUiEvent(
                    UiEvent.OnSaveHistoryPage(
                        historyPageToSave.copy(
                            timestamp = todayDateInMillis + 1.minutes.inWholeMilliseconds
                        )
                    )
                )

                assertThat(state.value.history).isNotEmpty()
                assertThat(state.value.history.size).isEqualTo(1)

                val savedHistoryPage = state.value.history.first()

                assertThat(savedHistoryPage.timestamp).isGreaterThan(historyPageToSave.timestamp)
            }
        }

    @Test
    fun `save a duplicated history page only if it isn't in the current day's history`() =
        runTest {
            val todayDateInMillis = System.currentTimeMillis()
            with(viewModel) {
                onUiEvent(
                    UiEvent.OnSaveHistoryPage(
                        historyPage1.copy(
                            timestamp = todayDateInMillis - 2.days.inWholeMilliseconds
                        )
                    )
                )
                onUiEvent(
                    UiEvent.OnSaveHistoryPage(
                        historyPage1.copy(timestamp = todayDateInMillis)
                    )
                )

                assertThat(state.value.history.size).isEqualTo(2)
            }
        }

    @Test
    fun `delete selected history pages`() = runTest {
        with(viewModel) {
            onUiEvent(UiEvent.OnSaveHistoryPage(historyPage1))
            onUiEvent(UiEvent.OnSaveHistoryPage(historyPage2))
            onUiEvent(UiEvent.OnSaveHistoryPage(historyPage3))

            val historyPagesToDelete = listOf(historyPage1, historyPage3)

            onUiEvent(UiEvent.OnDeleteHistoryPages(historyPagesToDelete))

            assertThat(state.value.history.size).isEqualTo(1)
            assertThat(state.value.history).containsExactly(historyPage2)
        }
    }

    @Test
    fun `delete all history pages`() = runTest {
        with(viewModel) {
            onUiEvent(UiEvent.OnSaveHistoryPage(historyPage1))
            onUiEvent(UiEvent.OnSaveHistoryPage(historyPage2))
            onUiEvent(UiEvent.OnSaveHistoryPage(historyPage3))

            onUiEvent(UiEvent.OnDeleteAllHistoryPages)

            assertThat(state.value.history).isEmpty()
        }
    }
}