package com.playlab.broadenbrowser.repository

import com.playlab.broadenbrowser.ui.utils.SearchMechanism
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePreferencesRepository : PreferencesRepository {

    private var isJavascriptAllowed = MutableStateFlow(true)
    private var isStartInFullscreenEnabled = MutableStateFlow(false)
    private var isDarkThemeEnabled = MutableStateFlow(false)
    private var searchMechanism = MutableStateFlow(SearchMechanism.GOOGLE.name)

    override fun isJavascriptAllowed(): Flow<Boolean> {
        return isJavascriptAllowed
    }

    override suspend fun allowJavascript(allowed: Boolean) {
        isJavascriptAllowed.value = allowed
    }

    override fun isStartInFullscreenEnabled(): Flow<Boolean> {
        return isStartInFullscreenEnabled
    }

    override suspend fun enableStartInFullscreen(enabled: Boolean) {
        isStartInFullscreenEnabled.value = enabled
    }

    override fun isDarkThemeEnabled(): Flow<Boolean> {
        return isDarkThemeEnabled
    }

    override suspend fun enableDarkTheme(enabled: Boolean) {
        isDarkThemeEnabled.value = enabled
    }

    override fun searchMechanism(): Flow<String> {
        return searchMechanism
    }

    override suspend fun setSearchMechanism(searchMechanism: String) {
        this.searchMechanism.value = searchMechanism
    }
}