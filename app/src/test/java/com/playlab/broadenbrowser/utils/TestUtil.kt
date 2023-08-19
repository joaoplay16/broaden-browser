package com.playlab.broadenbrowser.utils

import com.google.common.truth.Truth.assertThat
import com.playlab.broadenbrowser.mocks.MockHistoryPages.browserHistory
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage1
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage2
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage3
import com.playlab.broadenbrowser.mocks.MockHistoryPages.historyPage4
import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.ui.utils.Util.isDateToday
import com.playlab.broadenbrowser.ui.utils.Util.nextTabHistoryEntry
import com.playlab.broadenbrowser.ui.utils.Util.previousTabHistoryEntry
import org.junit.Test

class TestUtil {

    @Test
    fun `timestamp is date today`() {
        val timestamp = System.currentTimeMillis()

        assertThat(isDateToday(timestamp)).isTrue()
    }

    @Test
    fun `go to the next tab history entry`() {
        val afterTheThirdEntry = browserHistory.nextTabHistoryEntry(
            currentTabHistoryEntry = historyPage3
        )
        val afterTheLastEntry = browserHistory.nextTabHistoryEntry(
            currentTabHistoryEntry = historyPage4
        )
        val afterNothing = emptyList<HistoryPage>().nextTabHistoryEntry(
            currentTabHistoryEntry = historyPage1
        )

        assertThat(afterTheThirdEntry).isEqualTo(historyPage4)
        assertThat(afterTheLastEntry).isNull()
        assertThat(afterNothing).isNull()
    }

    @Test
    fun `go to the previous tab history entry`() {
        val beforeTheThirdEntry = browserHistory.previousTabHistoryEntry(
            currentTabHistoryEntry = historyPage3
        )
        val beforeTheFirstEntry = browserHistory.previousTabHistoryEntry(
            currentTabHistoryEntry = historyPage1
        )
        val beforeNothing = emptyList<HistoryPage>().previousTabHistoryEntry(
            currentTabHistoryEntry = historyPage1
        )

        assertThat(beforeTheThirdEntry).isEqualTo(historyPage2)
        assertThat(beforeTheFirstEntry).isNull()
        assertThat(beforeNothing).isNull()
    }
}