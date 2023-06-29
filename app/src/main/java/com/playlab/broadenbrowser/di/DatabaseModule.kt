package com.playlab.broadenbrowser.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.playlab.broadenbrowser.data.local.BrowserDatabase
import com.playlab.broadenbrowser.data.preferences.PreferencesDataStore
import com.playlab.broadenbrowser.repository.PreferencesRepository
import com.playlab.broadenbrowser.ui.utils.Constants.BROWSER_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                appContext.preferencesDataStoreFile("settings")
        }
    )

    @Provides
    @Singleton
    fun providePreferencesRepository(
        dataStore: PreferencesDataStore
    ): PreferencesRepository =
        PreferencesRepository(dataStore)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BrowserDatabase {
        return Room.databaseBuilder(
            context,
            BrowserDatabase::class.java,
            BROWSER_DATABASE
        ).build()
    }
}