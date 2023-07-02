package com.playlab.broadenbrowser.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.playlab.broadenbrowser.mocks.MockTabPages.tab1
import com.playlab.broadenbrowser.mocks.MockTabPages.tab2
import com.playlab.broadenbrowser.data.local.BrowserDatabase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@MediumTest
@RunWith(AndroidJUnit4::class)
class
TestBrowserRepository {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_browser_repo")
    lateinit var repository: BrowserRepository

    @Inject
    @Named("test_browser_db")
    lateinit var db: BrowserDatabase

    @Before
    fun before() = runTest{
        hiltRule.inject()

        repository.deleteAllTabPages()
    }

    @Test
    fun testInsertAndGetAllTabs() = runTest {

        repository.insertTabPage(tab1)
        repository.insertTabPage(tab2)

        val allTabs = repository.getTabs().first()

        assertThat(allTabs.size).isEqualTo(2)
        assertThat(allTabs).contains(tab1)
        assertThat(allTabs).contains(tab2)
    }

    @Test
    fun testDeleteGivenTabs() = runTest {

        repository.insertTabPage(tab1)
        repository.insertTabPage(tab2)

        var allTabs = repository.getTabs().first()

        assertThat(allTabs.size).isEqualTo(2)

        repository.deleteTabPages(listOf(tab1))

        allTabs = repository.getTabs().first()

        assertThat(allTabs.size).isEqualTo(1)
    }

    @Test
    fun testDeleteAllTabs() = runTest {

        repository.insertTabPage(tab1)
        repository.insertTabPage(tab2)

        var allTabs = repository.getTabs().first()

        assertThat(allTabs.size).isEqualTo(2)

        repository.deleteAllTabPages()

        allTabs = repository.getTabs().first()

        assertThat(allTabs).isEmpty()
    }
}