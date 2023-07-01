package com.playlab.broadenbrowser.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    
    fun isJavascriptAllowed(): Flow<Boolean>

    suspend fun allowJavascript(allowed: Boolean)

    fun isStartInFullscreenEnabled(): Flow<Boolean>

    suspend fun enableStartInFullscreen(enabled: Boolean)

    fun isDarkThemeEnabled(): Flow<Boolean> 
    
    suspend fun enableDarkTheme(enabled: Boolean)

    fun searchMechanism(): Flow<String>
    
    suspend fun setSearchMechanism(searchMechanism: String)
}