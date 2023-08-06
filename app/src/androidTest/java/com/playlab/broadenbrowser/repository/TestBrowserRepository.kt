package com.playlab.broadenbrowser.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.playlab.broadenbrowser.data.local.BrowserDatabase
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage1
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage2
import com.playlab.broadenbrowser.mocks.MockTabPages.tab1
import com.playlab.broadenbrowser.mocks.MockTabPages.tab2
import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.model.TabHistoryEntry
import com.playlab.broadenbrowser.model.TabPage
import com.playlab.broadenbrowser.ui.utils.Util.isDateToday
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
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

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
    fun insertAndGetAllTabs() = runTest {

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
    fun deleteGivenTabs() = runTest {

        repository.insertTabPage(tab1)
        repository.insertTabPage(tab2)

        var allTabs = repository.getTabs().first()

        assertThat(allTabs.size).isEqualTo(2)

        repository.deleteTabPages(listOf(tab1))

        allTabs = repository.getTabs().first()

        assertThat(allTabs.size).isEqualTo(1)
    }

    @Test
    fun deleteAllTabs() = runTest {

        repository.insertTabPage(tab1)
        repository.insertTabPage(tab2)

        var allTabs = repository.getTabs().first()

        assertThat(allTabs.size).isEqualTo(2)

        repository.deleteAllTabPages()

        allTabs = repository.getTabs().first()

        assertThat(allTabs).isEmpty()
    }

    @Test
    fun gettingATabPageById() = runTest {

        val id = repository.insertTabPage(tab1)

        val tabPageResult: TabPage? = repository.getTab(id = id)

        assertThat(tabPageResult).isNotNull()
    }

    @Test
    fun editingAndGettingATabPage() = runTest {

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

    @Test
    fun insertAndGetHistoryPages() = runTest {
        val historyPage1Id = repository.insertHistoryPage(historyPage1)
        val historyPage2Id = repository.insertHistoryPage(historyPage2)

        assertThat(historyPage1Id).isGreaterThan(-1)
        assertThat(historyPage2Id).isGreaterThan(-1)

        val history = repository.getHistory().first()

        assertThat(history.size).isEqualTo(2)
        assertThat(history).contains(historyPage1)
        assertThat(history).contains(historyPage2)
    }

    @Test
    fun deleteHistoryPages() = runTest {
        repository.insertHistoryPage(historyPage1)
        repository.insertHistoryPage(historyPage2)

        var history = repository.getHistory().first()

        assertThat(history.size).isEqualTo(2)

        repository.deleteHistoryPages(listOf(historyPage1))

        history = repository.getHistory().first()

        assertThat(history.size).isEqualTo(1)
    }

    @Test
    fun deleteAllHistoryPages() = runTest {
        repository.insertHistoryPage(historyPage1)
        repository.insertHistoryPage(historyPage2)

        repository.deleteAllHistoryPages()

        val history = repository.getHistory().first()

        assertThat(history).isEmpty()
    }

    @Test
    fun insertDuplicatedHistoryPageReturnHistoryPagesWithSameUrl() = runTest {
        repository.insertHistoryPage(
            historyPage1.copy(id = 0) // 0 to make the id autogenerated
        )

        repository.insertHistoryPage(
            historyPage1.copy(id = 0) // 0 to make the id autogenerated
        )

        val history = repository.getHistory().first()

        history.forEach {
            assertThat(it.url).isEqualTo(historyPage1.url)
        }
    }

    @Test
    fun gettingAHistoryPageById() = runTest {
        val id = repository.insertHistoryPage(historyPage1)

        val historyPage: HistoryPage? = repository.getHistoryPage(id = id)

        assertThat(historyPage).isNotNull()
    }

    @Test
    fun editingAndGettingAHistoryPage() = runTest {
        val id = repository.insertHistoryPage(historyPage1)

        val historyPage1Modified = historyPage1.copy(
            id = id.toInt(),
            url = "https://m3.material.io",
            title = "Material Design"
        )

        val affectedRows: Int = repository.editHistoryPage(historyPage1Modified)

        val historyPageResult: HistoryPage? = repository.getHistoryPage(id = id)

        assertThat(affectedRows).isGreaterThan(0)

        assertThat(historyPageResult).isEqualTo(historyPage1Modified)
    }

    @Test
    fun gettingTodayMostRecentHistoryPageByUrl() = runTest {
        val todayDateInMillis = System.currentTimeMillis()

        repository.insertHistoryPage(historyPage1.copy(timestamp = todayDateInMillis))
        repository.insertHistoryPage(
            historyPage1.copy(
                timestamp = todayDateInMillis - 1.minutes.inWholeMilliseconds
            )
        )
        repository.insertHistoryPage(
            historyPage1.copy(
                timestamp = todayDateInMillis - 1.days.inWholeMilliseconds
            )
        )

        val historyPage = repository.getTodayLatestHistoryPageByUrl(url = historyPage1.url)

        assertThat(historyPage).isNotNull()

        historyPage?.let {
            assertThat(isDateToday(it.timestamp)).isTrue()
            assertThat(it.timestamp).isEqualTo(todayDateInMillis)
        }
    }

    @Test
    fun insertingATabHistoryEntry() = runTest {
        val tabHistoryEntry = TabHistoryEntry(
            tabId =  2,
            historyPageId = 1,
            creationTime = System.currentTimeMillis()
        )
        val id = repository.insertTabHistoryEntry(tabHistoryEntry)

        assertThat(id != -1L).isTrue()
        assertThat(id ).isGreaterThan(0)
    }

    @Test
    fun gettingTabHistoryEntries() = runTest {

        val tabId = repository.insertTabPage(tab1)

        val historyPage1Id = repository.insertHistoryPage(historyPage1)
        val historyPage2Id = repository.insertHistoryPage(historyPage2)

        val tabHistoryEntry1 = TabHistoryEntry(
            tabId = tabId,
            historyPageId = historyPage1Id,
            creationTime = System.currentTimeMillis()
        )
        val tabHistoryEntry2 = TabHistoryEntry(
            tabId =  tabId,
            historyPageId = historyPage2Id,
            creationTime = System.currentTimeMillis()
        )

        repository.insertTabHistoryEntry(tabHistoryEntry1)
        repository.insertTabHistoryEntry(tabHistoryEntry2)

        val tabHistory = repository.getTabHistory(tabId).first()

        assertThat(tabHistory).isNotEmpty()
        assertThat(tabHistory.size).isEqualTo(2)
    }

    @Test
    fun deletingAllTabHistory() = runTest {

        val tabId = 2L

        val tabHistoryEntry1 = TabHistoryEntry(
            tabId = tabId,
            historyPageId = 1,
            creationTime = System.currentTimeMillis()
        )
        val tabHistoryEntry2 = TabHistoryEntry(
            tabId =  tabId,
            historyPageId = 2,
            creationTime = System.currentTimeMillis()
        )

        repository.insertTabHistoryEntry(tabHistoryEntry1)
        repository.insertTabHistoryEntry(tabHistoryEntry2)

        val affectedRows = repository.deleteTabHistory(tabId)

        assertThat(affectedRows).isEqualTo(2)
    }
}