package com.playlab.broadenbrowser.model

class TabHistoryManager {
    private var historyPages: List<HistoryPage> = mutableListOf()
    val historySize get() = historyPages.size
    private var currentIndex: Int = 0

    val currentHistoryPage: HistoryPage?
        get() = historyPages.getOrNull(currentIndex)

    fun setCurrentHistoryEntry(
        currentHistoryEntry: HistoryPage? = null,
    ) {
        val initialEntryIndex = historyPages.indexOf(currentHistoryEntry)
        currentIndex = if (initialEntryIndex > 0) initialEntryIndex else 0
    }

    fun updateTabHistory(historyPages: List<HistoryPage>) {
        currentIndex = historyPages.size - 1
        this.historyPages = historyPages
    }

    fun hasNext(): Boolean {
        return currentIndex >= 0 && currentIndex < historyPages.size - 1
    }

    fun hasPrevious(): Boolean {
        return currentIndex > 0 && historyPages.isNotEmpty()
    }

    fun nextTabHistoryEntry(): HistoryPage? {
        return if (hasNext()) {
            currentIndex++
            historyPages[currentIndex]
        } else null
    }

    fun previousTabHistoryEntry(): HistoryPage? {
        return if (hasPrevious()) {
            currentIndex--
            historyPages[currentIndex]
        } else null
    }
}