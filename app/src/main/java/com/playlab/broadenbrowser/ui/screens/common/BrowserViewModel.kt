package com.playlab.broadenbrowser.ui.screens.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playlab.broadenbrowser.domain.SaveEditHistoryPageUseCase
import com.playlab.broadenbrowser.repository.BrowserRepository
import com.playlab.broadenbrowser.repository.PreferencesRepository
import com.playlab.broadenbrowser.ui.utils.SearchMechanism
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val browserRepository: BrowserRepository,
    private val saveEditHistoryPageUseCase: SaveEditHistoryPageUseCase
) : ViewModel() {

    var state by mutableStateOf(BrowserState())
        private set

    init {
        with(preferencesRepository) {
            isStartInFullscreenEnabled().onEach {
                state = state.copy(isStartInFullscreenEnabled = it)
            }.launchIn(viewModelScope)

            isJavascriptAllowed().onEach {
                state = state.copy(isJavascriptAllowed = it)
            }.launchIn(viewModelScope)

            isDarkThemeEnabled().onEach {
                state = state.copy(isDarkThemeEnabled = it)
            }.launchIn(viewModelScope)

            searchMechanism().onEach {
                state = state.copy(searchMechanism = SearchMechanism.valueOf(it))
            }.launchIn(viewModelScope)
        }

        with(browserRepository) {
            getTabs().onEach {
                state = state.copy(tabs = it)
            }.launchIn(viewModelScope)

            getHistory().onEach {
                state = state.copy(history = it)
            }.launchIn(viewModelScope)
        }
    }

    fun onUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is UiEvent.OnAllowJavascript -> {
                    preferencesRepository.allowJavascript(uiEvent.allowed)
                }
                is UiEvent.OnEnableFullscreen -> {
                    state = state.copy(isInFullscreen = uiEvent.enabled)
                }
                is UiEvent.OnEnableStartInFullscreen -> {
                    preferencesRepository.enableStartInFullscreen(uiEvent.enabled)
                }
                is UiEvent.OnEnableDarkTheme -> {
                    preferencesRepository.enableDarkTheme(uiEvent.enabled)
                }
                is UiEvent.OnSetSearchMechanism -> {
                    preferencesRepository.setSearchMechanism(uiEvent.searchMechanism.name)
                }
                is UiEvent.OnSetAsDefaultBrowser -> {
                    state = state.copy(isDefaultBrowser = uiEvent.isDefaultBrowser)
                }
                is UiEvent.OnTabChange -> {
                    state = state.copy(currentTab = uiEvent.tabPage)
                }
                is UiEvent.OnSaveTab -> {
                    val rowId = browserRepository.insertTabPage(uiEvent.tabPage)
                    if (rowId != -1L)
                        state = state.copy(currentTab = uiEvent.tabPage.copy(id = rowId.toInt()))
                }
                is UiEvent.OnEditTab -> {
                    val result = browserRepository.editTabPage(tabPage = uiEvent.tabPage)
                    if (result > 0)
                        state = state.copy(currentTab = uiEvent.tabPage)
                }
                is UiEvent.OnCloseTabs -> {
                    browserRepository.deleteTabPages(uiEvent.tabPages)
                    if(state.currentTab in uiEvent.tabPages)
                        state = state.copy(currentTab = null)
                }
                is UiEvent.OnCloseAllTabs -> {
                    browserRepository.deleteAllTabPages()
                    state = state.copy(currentTab = null)
                }
                is UiEvent.OnSaveHistoryPage -> {
                    saveEditHistoryPageUseCase(uiEvent.historyPage)
                }
                is UiEvent.OnDeleteHistoryPages -> {
                    browserRepository.deleteHistoryPages(uiEvent.historyPages)
                }
                is UiEvent.OnDeleteAllHistoryPages -> {
                    browserRepository.deleteAllHistoryPages()
                }
            }
        }
    }
}