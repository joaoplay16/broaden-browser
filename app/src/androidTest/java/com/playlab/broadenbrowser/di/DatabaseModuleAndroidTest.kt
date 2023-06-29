package com.playlab.broadenbrowser.di

import android.content.Context
import androidx.room.Room
import com.playlab.broadenbrowser.data.local.BrowserDatabase
import com.playlab.broadenbrowser.repository.BrowserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModuleAndroidTest {
    @Named("test_browser_db")
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BrowserDatabase = Room.inMemoryDatabaseBuilder(
        context,
        BrowserDatabase::class.java
    ).allowMainThreadQueries().build()

    @Named("test_browser_repo")
    @Provides
    @Singleton
    fun provideBrowserRepository(
        @Named("test_browser_db")
        database: BrowserDatabase
    ) = BrowserRepository(database)
}