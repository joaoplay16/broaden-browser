package com.playlab.broadenbrowser.ui.screens.common

data class BrowserState(
    val isInFullscreen: Boolean = false,
    val isStartInFullscreenEnabled: Boolean = false,
    val isJavascriptAllowed: Boolean = true,
    val isDarkThemeEnabled: Boolean = false,
    val searchMechanism: String = "",
    val externalLink: String? = null
)