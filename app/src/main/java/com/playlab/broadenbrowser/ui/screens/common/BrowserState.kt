package com.playlab.broadenbrowser.ui.screens.common

import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.model.TabPage
import com.playlab.broadenbrowser.ui.utils.SearchMechanism

data class BrowserState(
    val isInFullscreen: Boolean = false,
    val isStartInFullscreenEnabled: Boolean = false,
    val isJavascriptAllowed: Boolean = true,
    val isDarkThemeEnabled: Boolean = false,
    val searchMechanism: SearchMechanism = SearchMechanism.GOOGLE,
    val externalLink: String? = null,
    val isDefaultBrowser: Boolean = false,
    val tabs: List<TabPage> = emptyList(),
    val currentTab: TabPage? = null,
    val history: List<HistoryPage> = emptyList()
)