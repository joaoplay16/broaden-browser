package com.playlab.broadenbrowser.domain

import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.model.TabHistoryEntry
import com.playlab.broadenbrowser.model.TabPage
import com.playlab.broadenbrowser.repository.BrowserRepository
import javax.inject.Inject

class SaveTabHistoryEntryUseCase @Inject constructor(
    private val repository: BrowserRepository
) {
    suspend operator fun invoke(
        tabPage: TabPage,
        historyPage: HistoryPage
    ): Long {
        val tabHistoryEntry = TabHistoryEntry(
            tabId = tabPage.id.toLong(),
            historyPageId = historyPage.id.toLong(),
            creationTime = System.currentTimeMillis()
        )

        return repository.insertTabHistoryEntry(tabHistoryEntry)
    }
}