package com.playlab.broadenbrowser.ui.screens.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playlab.broadenbrowser.domain.SaveEditHistoryPageUseCase
import com.playlab.broadenbrowser.domain.SaveEditTabUseCase
import com.playlab.broadenbrowser.repository.BrowserRepository
import com.playlab.broadenbrowser.repository.PreferencesRepository
import com.playlab.broadenbrowser.ui.utils.SearchMechanism
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val browserRepository: BrowserRepository,
    private val saveEditHistoryPageUseCase: SaveEditHistoryPageUseCase,
    private val saveEditTabUseCase: SaveEditTabUseCase,
) : ViewModel() {

    private var _state = MutableStateFlow(BrowserState())
    val state = _state.asStateFlow()

    init {
        with(preferencesRepository) {
            isStartInFullscreenEnabled().onEach { enabled ->
                _state.update { it.copy(isStartInFullscreenEnabled = enabled) }
            }.launchIn(viewModelScope)

            isJavascriptAllowed().onEach { value ->
                _state.update { it.copy(isJavascriptAllowed = value) }
            }.launchIn(viewModelScope)

            isDarkThemeEnabled().onEach { value ->
                _state.update { it.copy(isDarkThemeEnabled = value) }
            }.launchIn(viewModelScope)

            searchMechanism().onEach { value ->
                _state.update { it.copy(searchMechanism = SearchMechanism.valueOf(value)) }
            }.launchIn(viewModelScope)
        }

        with(browserRepository) {
            getTabs().onEach { value ->
                _state.update { it.copy(tabs = value) }
            }.launchIn(viewModelScope)

            getHistory().onEach { value ->
                _state.update { it.copy(history = value) }
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
                    _state.update { it.copy(isInFullscreen = uiEvent.enabled) }
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
                    _state.update { it.copy(isDefaultBrowser = uiEvent.isDefaultBrowser) }
                }

                is UiEvent.OnTabChange -> {
                    _state.update { it.copy(currentTab = uiEvent.tabPage) }
                }

                is UiEvent.OnSaveEditTab -> {
                    val result = saveEditTabUseCase(
                        currentTabPage = _state.value.currentTab,
                        tabPage = uiEvent.tabPage
                    )
                    _state.update { it.copy(currentTab = result) }

                }

                is UiEvent.OnCloseTabs -> {
                    browserRepository.deleteTabPages(uiEvent.tabPages)
                    if (_state.value.currentTab in uiEvent.tabPages)
                        _state.update { it.copy(currentTab = null) }
                }

                is UiEvent.OnCloseAllTabs -> {
                    browserRepository.deleteAllTabPages()
                    _state.update { it.copy(currentTab = null) }
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