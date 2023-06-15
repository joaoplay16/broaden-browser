package com.playlab.broadenbrowser.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.playlab.broadenbrowser.ui.utils.SearchMechanism
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        val ALLOW_JAVASCRIPT_KEY = booleanPreferencesKey("allow_javascript")
        val START_IN_FULLSCREEN_KEY = booleanPreferencesKey("start_in_fullscreen")
        val IS_DARK_THEME_KEY = booleanPreferencesKey("is_dark_theme")
        val SEARCH_MECHANISM_KEY = stringPreferencesKey("search_mechanism")
    }

    val isJavascriptAllowed: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[ALLOW_JAVASCRIPT_KEY] ?: true
        }

    suspend fun allowJavascript(allowed: Boolean) {
        dataStore.edit { preferences ->
            preferences[ALLOW_JAVASCRIPT_KEY] = allowed
        }
    }

    val isStartInFullscreenEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[START_IN_FULLSCREEN_KEY] ?: false
        }

    suspend fun enableStartInFullscreen(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[START_IN_FULLSCREEN_KEY] = enabled
        }
    }

    val isDarkThemeEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[IS_DARK_THEME_KEY] ?: false
        }

    suspend fun enableDarkTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME_KEY] = enabled
        }
    }

    val searchMechanism: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[SEARCH_MECHANISM_KEY] ?: SearchMechanism.GOOGLE.name
        }

    suspend fun setSearchMechanism(searchMechanism: String) {
        dataStore.edit { preferences ->
            preferences[SEARCH_MECHANISM_KEY] = searchMechanism
        }
    }
}