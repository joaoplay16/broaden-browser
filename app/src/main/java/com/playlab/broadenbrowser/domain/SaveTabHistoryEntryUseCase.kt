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
    ): HistoryPage? {
        val lastSavedTabHistoryEntry = repository.getLatestEntryFromTabHistory(tabPage.id.toLong())

        if (isTheSameHistoryPage(historyPage, lastSavedTabHistoryEntry).not()) {
            val tabHistoryEntry = TabHistoryEntry(
                tabId = tabPage.id.toLong(),
                historyPageId = historyPage.id.toLong(),
                creationTime = System.currentTimeMillis()
            )

            return repository.insertTabHistoryEntry(tabHistoryEntry).let{
                  historyPage.copy(id = it.toInt())
            }
        }
        
        return null
    }

    private fun isTheSameHistoryPage(
        currentHistoryPage: HistoryPage,
        lastSavedTabHistoryEntry: HistoryPage?
    ): Boolean {
        return currentHistoryPage.url == lastSavedTabHistoryEntry?.url
    }
}