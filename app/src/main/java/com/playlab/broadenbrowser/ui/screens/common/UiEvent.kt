package com.playlab.broadenbrowser.ui.screens.common

import com.playlab.broadenbrowser.model.TabPage
import com.playlab.broadenbrowser.ui.utils.SearchMechanism

sealed class UiEvent {
    data class OnAllowJavascript(val allowed: Boolean): UiEvent()
    data class OnEnableFullscreen(val enabled: Boolean): UiEvent()
    data class OnEnableStartInFullscreen(val enabled: Boolean): UiEvent()
    data class OnEnableDarkTheme(val enabled: Boolean): UiEvent()
    data class OnSetSearchMechanism(val searchMechanism: SearchMechanism): UiEvent()
    data class OnSetAsDefaultBrowser(val isDefaultBrowser: Boolean): UiEvent()
    data class OnNewTab(val tabPage: TabPage?): UiEvent()
    data class OnSaveTab(val tabPage: TabPage): UiEvent()
    data class OnCloseTabs(val tabPages: List<TabPage>): UiEvent()
    object OnCloseAllTabs : UiEvent()
}
