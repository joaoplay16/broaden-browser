package com.playlab.broadenbrowser.mocks

import com.playlab.broadenbrowser.model.HistoryPage

object MockHistoryPages {
    val historyPage1 = HistoryPage(
        1,
        "Google",
        "https://google.com",
        1000000001000L
    )
    val historyPage2 = HistoryPage(
        2,
        "Bing",
        "https://bing.com",
        1000000002000L
    )
    val historyPage3 = HistoryPage(
        3,
        "Yahoo",
        "https://yahoo.com",
        1000000003000L
    )
    val historyPage4 = HistoryPage(
        4,
        "Duck Duck Go",
        "https://duckduckgo.com",
        1000000004000L
    )

    val browserHistory = listOf(historyPage1, historyPage2, historyPage3, historyPage4)
}