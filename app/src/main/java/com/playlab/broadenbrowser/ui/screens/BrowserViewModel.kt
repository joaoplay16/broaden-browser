package com.playlab.broadenbrowser.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playlab.broadenbrowser.repository.BrowserRepository
import com.playlab.broadenbrowser.repository.PreferencesRepository
import com.playlab.broadenbrowser.ui.screens.common.BrowserState
import com.playlab.broadenbrowser.ui.screens.common.UiEvent
import com.playlab.broadenbrowser.ui.utils.SearchMechanism
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val browserRepository: BrowserRepository
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

                is UiEvent.OnNewTab -> {
                    state = state.copy(currentTab = uiEvent.tabPage)
                }

                is UiEvent.OnSaveTab -> {
                    val rowId = browserRepository.insertTabPage(uiEvent.tabPage)
                    if (rowId != -1L)
                        state = state.copy(currentTab = uiEvent.tabPage.copy(id = rowId.toInt()))
                }

                is UiEvent.OnCloseTabs -> {
                    browserRepository.deleteTabPages(uiEvent.tabPages)
                }

                is UiEvent.OnCloseAllTabs -> {
                    browserRepository.deleteAllTabPages()
                }
            }
        }
    }
}