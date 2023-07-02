package com.playlab.broadenbrowser.ui.screens

import com.google.common.truth.Truth.assertThat
import com.playlab.broadenbrowser.MainCoroutineRule
import com.playlab.broadenbrowser.mocks.MockTabPages.tab1
import com.playlab.broadenbrowser.mocks.MockTabPages.tab2
import com.playlab.broadenbrowser.mocks.MockTabPages.tab3
import com.playlab.broadenbrowser.repository.FakeBrowserRepository
import com.playlab.broadenbrowser.repository.FakePreferencesRepository
import com.playlab.broadenbrowser.ui.screens.common.UiEvent
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
    fun `test add new tab`() = runTest {
        with(viewModel) {
            assertThat(state.tabs).isEmpty()
            onUiEvent(UiEvent.OnNewTab(tab1))
            assertThat(state.tabs.size).isEqualTo(1)
        }
    }

    @Test
    fun `test close selected tabs`() = runTest {
        with(viewModel) {
            onUiEvent(UiEvent.OnNewTab(tab1))
            onUiEvent(UiEvent.OnNewTab(tab2))
            onUiEvent(UiEvent.OnNewTab(tab3))

            val tabsToClose = listOf(tab1, tab3)

            onUiEvent(UiEvent.OnCloseTabs(tabsToClose))

            assertThat(state.tabs.size).isEqualTo(1)
            assertThat(state.tabs).containsExactly(tab2)
        }
    }

    @Test
    fun `test close all tabs`() = runTest {
        with(viewModel) {
            onUiEvent(UiEvent.OnNewTab(tab1))
            onUiEvent(UiEvent.OnNewTab(tab2))
            onUiEvent(UiEvent.OnNewTab(tab3))

            onUiEvent(UiEvent.OnCloseAllTabs)

            assertThat(state.tabs).isEmpty()
        }
    }

    @Test
    fun `test javascript enabling`() = runTest {
        with(viewModel) {
            assertThat(state.isJavascriptAllowed).isTrue()
            onUiEvent(UiEvent.OnAllowJavascript(false))

            assertThat(state.isJavascriptAllowed).isFalse()
        }
    }

    @Test
    fun `test fullscreen enabling`() = runTest {
        with(viewModel) {
            assertThat(state.isInFullscreen).isFalse()

            onUiEvent(UiEvent.OnEnableFullscreen(true))

            assertThat(state.isInFullscreen).isTrue()
        }
    }
}