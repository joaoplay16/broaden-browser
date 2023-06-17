package com.playlab.broadenbrowser.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playlab.broadenbrowser.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    var state by mutableStateOf(BrowserState())
        private set

    init {
        viewModelScope.launch {
            with(preferencesRepository) {
                isStartInFullscreenEnabled().collect {
                    state = state.copy(isStartInFullscreenEnabled = it)
                }
            }
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
            }
        }
    }
}