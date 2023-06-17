package com.playlab.broadenbrowser.ui.screens

import com.playlab.broadenbrowser.ui.utils.SearchMechanism

sealed class UiEvent {
    data class OnAllowJavascript(val allowed: Boolean): UiEvent()
    data class OnEnableFullscreen(val enabled: Boolean): UiEvent()
    data class OnEnableStartInFullscreen(val enabled: Boolean): UiEvent()
    data class OnEnableDarkTheme(val enabled: Boolean): UiEvent()
    data class OnSetSearchMechanism(val searchMechanism: SearchMechanism): UiEvent()
}
