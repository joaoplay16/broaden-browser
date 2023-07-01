package com.playlab.broadenbrowser.repository

import com.playlab.broadenbrowser.data.preferences.PreferencesDataStore
import javax.inject.Inject

class DefaultPreferencesRepository @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : PreferencesRepository{
    override fun isJavascriptAllowed() =
        preferencesDataStore.isJavascriptAllowed

    override suspend fun allowJavascript(allowed: Boolean) {
        preferencesDataStore.allowJavascript(allowed)
    }

    override fun isStartInFullscreenEnabled() =
        preferencesDataStore.isStartInFullscreenEnabled

    override suspend fun enableStartInFullscreen(enabled: Boolean) {
        preferencesDataStore.enableStartInFullscreen(enabled)
    }

    override fun isDarkThemeEnabled() =
        preferencesDataStore.isDarkThemeEnabled

    override suspend fun enableDarkTheme(enabled: Boolean) {
        preferencesDataStore.enableDarkTheme(enabled)
    }

    override fun searchMechanism() =
        preferencesDataStore.searchMechanism

    override suspend fun setSearchMechanism(searchMechanism: String) {
        preferencesDataStore.setSearchMechanism(searchMechanism)
    }
}