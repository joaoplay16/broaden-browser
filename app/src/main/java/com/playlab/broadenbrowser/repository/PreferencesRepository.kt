package com.playlab.broadenbrowser.repository

import com.playlab.broadenbrowser.data.preferences.PreferencesDataStore
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) {
    fun isJavascriptAllowed() =
        preferencesDataStore.isJavascriptAllowed

    suspend fun allowJavascript(allowed: Boolean) {
        preferencesDataStore.allowJavascript(allowed)
    }

    fun isStartInFullscreenEnabled() =
        preferencesDataStore.isStartInFullscreenEnabled

    suspend fun enableStartInFullscreen(enabled: Boolean) {
        preferencesDataStore.enableStartInFullscreen(enabled)
    }

    fun isDarkThemeEnabled() =
        preferencesDataStore.isDarkThemeEnabled

    suspend fun enableDarkTheme(enabled: Boolean) {
        preferencesDataStore.enableDarkTheme(enabled)
    }

    fun searchMechanism() =
        preferencesDataStore.searchMechanism

    suspend fun setSearchMechanism(searchMechanism: String) {
        preferencesDataStore.setSearchMechanism(searchMechanism)
    }
}