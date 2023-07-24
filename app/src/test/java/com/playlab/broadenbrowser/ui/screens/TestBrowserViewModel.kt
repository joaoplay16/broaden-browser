package com.playlab.broadenbrowser.ui.screens

import com.google.common.truth.Truth.assertThat
import com.playlab.broadenbrowser.MainCoroutineRule
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage1
import com.playlab.broadenbrowser.mocks.MockTabPages.tab1
import com.playlab.broadenbrowser.mocks.MockTabPages.tab2
import com.playlab.broadenbrowser.mocks.MockTabPages.tab3
import com.playlab.broadenbrowser.repository.FakeBrowserRepository
import com.playlab.broadenbrowser.repository.FakePreferencesRepository
import com.playlab.broadenbrowser.ui.screens.common.BrowserViewModel
import com.playlab.broadenbrowser.ui.screens.common.UiEvent
import com.playlab.broadenbrowser.ui.utils.SearchMechanism
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TestBrowserViewModel {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: BrowserViewModel

    @Before
    fun before() = runTest {
        viewModel = BrowserViewModel(
            FakePreferencesRepository(),
            FakeBrowserRepository()
        )
    }

    @Test
    fun `save a tab`() = runTest {
        with(viewModel) {
            assertThat(state.tabs).isEmpty()
            onUiEvent(UiEvent.OnSaveTab(tab1))
            assertThat(state.tabs.size).isEqualTo(1)
        }
    }

    @Test
    fun `save a duplicated tab`() = runTest {
        with(viewModel) {
            assertThat(state.tabs).isEmpty()
            onUiEvent(UiEvent.OnSaveTab(tab1))
            onUiEvent(UiEvent.OnSaveTab(tab1))
            assertThat(state.tabs.size).isEqualTo(2)
            assertThat(state.tabs).containsExactly(tab1, tab1)
        }
    }


    @Test
    fun `set the successfully saved tab as current tab ` () = runTest {
        with(viewModel) {
            onUiEvent(UiEvent.OnSaveTab(tab1))

            assertThat(state.currentTab).isNotNull()

            assertThat(state.currentTab!!.id).isGreaterThan(0)
        }
    }

    @Test
    fun `add a new tab`() = runTest {
        with(viewModel) {
            onUiEvent(UiEvent.OnSaveTab(tab1))
            onUiEvent(UiEvent.OnNewTab(null))
            assertThat(state.currentTab).isNull()
        }
    }

    @Test
    fun `close selected tabs`() = runTest {
        with(viewModel) {
            onUiEvent(UiEvent.OnSaveTab(tab1))
            onUiEvent(UiEvent.OnSaveTab(tab2))
            onUiEvent(UiEvent.OnSaveTab(tab3))

            val tabsToClose = listOf(tab1, tab3)

            onUiEvent(UiEvent.OnCloseTabs(tabsToClose))

            assertThat(state.tabs.size).isEqualTo(1)
            assertThat(state.tabs).containsExactly(tab2)
        }
    }

    @Test
    fun `close all tabs`() = runTest {
        with(viewModel) {
            onUiEvent(UiEvent.OnSaveTab(tab1))
            onUiEvent(UiEvent.OnSaveTab(tab2))
            onUiEvent(UiEvent.OnSaveTab(tab3))

            onUiEvent(UiEvent.OnCloseAllTabs)

            assertThat(state.tabs).isEmpty()
        }
    }

    @Test
    fun `enable javascript `() = runTest {
        with(viewModel) {
            assertThat(state.isJavascriptAllowed).isTrue()
            onUiEvent(UiEvent.OnAllowJavascript(false))

            assertThat(state.isJavascriptAllowed).isFalse()
        }
    }

    @Test
    fun `enable fullscreen`() = runTest {
        with(viewModel) {
            assertThat(state.isInFullscreen).isFalse()

            onUiEvent(UiEvent.OnEnableFullscreen(true))

            assertThat(state.isInFullscreen).isTrue()
        }
    }

    @Test
    fun `enable in fullscreen startup`() = runTest {
        with(viewModel) {
            assertThat(state.isStartInFullscreenEnabled).isFalse()

            onUiEvent(UiEvent.OnEnableStartInFullscreen(true))

            assertThat(state.isStartInFullscreenEnabled).isTrue()
        }
    }

    @Test
    fun `enable dark theme`() = runTest {
        with(viewModel) {
            assertThat(state.isDarkThemeEnabled).isFalse()

            onUiEvent(UiEvent.OnEnableDarkTheme(true))

            assertThat(state.isDarkThemeEnabled).isTrue()
        }
    }

    @Test
    fun `set search engine`() = runTest {
        with(viewModel) {
            assertThat(state.searchMechanism).isEqualTo(SearchMechanism.GOOGLE)

            onUiEvent(UiEvent.OnSetSearchMechanism(SearchMechanism.BING))

            assertThat(state.searchMechanism).isEqualTo(SearchMechanism.BING)
        }
    }

    @Test
    fun `set the successfully modified tab as current tab` () = runTest {
        with(viewModel) {
            onUiEvent(UiEvent.OnSaveTab(tab1))

            val tab1Modified = tab1.copy(
                id = state.currentTab!!.id,
                url = "https://m3.material.io",
                title = "Material Design"
            )

            onUiEvent(UiEvent.OnEditTab(tab1Modified))

            assertThat(state.currentTab).isEqualTo(tab1Modified)
        }
    }

    @Test
    fun `save a history page`() = runTest {
        with(viewModel) {
            onUiEvent(UiEvent.OnSaveHistoryPage(historyPage1))
            assertThat(state.history.size).isEqualTo(1)
        }
    }
}