package com.playlab.broadenbrowser.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.playlab.broadenbrowser.data.local.BrowserDatabase
import com.playlab.broadenbrowser.mocks.MockTabPages.tab1
import com.playlab.broadenbrowser.mocks.MockTabPages.tab2
import com.playlab.broadenbrowser.model.TabPage
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
    fun before() = runTest {
        hiltRule.inject()

        repository.deleteAllTabPages()
    }

    @Test
    fun testInsertAndGetAllTabs() = runTest {

        val tab1Id = repository.insertTabPage(tab1)
        val tab2Id = repository.insertTabPage(tab2)

        assertThat(tab1Id).isGreaterThan(-1)
        assertThat(tab2Id).isGreaterThan(-1)

        val allTabs = repository.getTabs().first()

        assertThat(allTabs.size).isEqualTo(2)
        assertThat(allTabs).contains(tab1)
        assertThat(allTabs).contains(tab2)
    }

    @Test
    fun insertDuplicatedTabsReturnsTabsWithSameUrl() = runTest {

        repository.insertTabPage(
            TabPage(
                title = tab1.title,
                url = tab1.url,
                timestamp = tab1.timestamp
            )
        )
        repository.insertTabPage(
            TabPage(
                title = tab1.title,
                url = tab1.url,
                timestamp = tab1.timestamp
            )
        )

        val allTabs = repository.getTabs().first()

        assertThat(allTabs.size).isEqualTo(2)

        allTabs.forEach {
            assertThat(it.url).isEqualTo(tab1.url)
        }
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

    @Test
    fun gettingATabPageById_shouldReturnTheTabPage() = runTest {

        val id = repository.insertTabPage(tab1)

        val tabPageResult: TabPage? = repository.getTab(id = id)

        assertThat(tabPageResult).isNotNull()
    }

    @Test
    fun editingAndGettingATabPage_shouldReturnTheModifiedTabPage() = runTest {

        val tabId = repository.insertTabPage(tab1)

        val tab1Modified = tab1.copy(
            id = tabId.toInt(),
            url = "https://m3.material.io",
            title = "Material Design"
        )

        val affectedRows: Int = repository.editTabPage(tab1Modified)

        val tabPageResult: TabPage? = repository.getTab(id = tabId)

        assertThat(affectedRows).isGreaterThan(0)

        assertThat(tabPageResult).isEqualTo(tab1Modified)
    }
}