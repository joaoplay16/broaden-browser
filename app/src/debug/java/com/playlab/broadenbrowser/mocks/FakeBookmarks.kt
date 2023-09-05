package com.playlab.broadenbrowser.mocks

import com.playlab.broadenbrowser.model.Bookmark

object FakeBookmarks {
    val bookmark1 = Bookmark(
        1,
        "Google",
        "https://google.com",
        1000000001000L
    )
    val bookmark2 = Bookmark(
        2,
        "Bing",
        "https://bing.com",
        1000000002000L
    )
    val bookmark3 = Bookmark(
        3,
        "Yahoo",
        "https://yahoo.com",
        1000000003000L
    )
    val bookmark4 = Bookmark(
        4,
        "Duck Duck Go",
        "https://duckduckgo.com",
        1000000004000L
    )

    val fakeBookmarks = listOf(bookmark1, bookmark2, bookmark3, bookmark4)
}