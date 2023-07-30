package com.playlab.broadenbrowser.mocks

import com.playlab.broadenbrowser.model.TabPage

object MockTabPages {
    val tab1 = TabPage(
        id = 1,
        title = "Google",
        url = "https://google.com",
        timestamp = 1000000001000L
    )
    val tab2 = TabPage(
        id = 2,
        title = "Bing",
        url = "https://bing.com",
        timestamp = 1000000002000L
    )
    val tab3 = TabPage(
        id = 3,
        title = "Yahoo",
        url = "https://yahoo.com",
        timestamp = 1000000003000L
    )
    val tab4 = TabPage(
        id = 4,
        title = "Duck Duck Go",
        url = "https://duckduckgo.com",
        timestamp = 1000000004000L
    )

    val tabList = listOf(tab1, tab2, tab3, tab4)
}