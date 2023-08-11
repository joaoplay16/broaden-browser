package com.playlab.broadenbrowser.ui.utils

object Constants {
    const val BROWSER_DATABASE = "browser_database"
    const val TABS_TABLE = "tabs"
    const val HISTORY_TABLE = "history"
    const val TAB_HISTORY = "tab_history"

    const val DEFAULT_USER_AGENT_STRING =
        "Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/113.0"

        const val SET_DESKTOP_VIEWPORT_SCRIPT = """
        document.querySelector('meta[name=\"viewport\"]')
            .setAttribute(
                'content',
                'width=1024px, initial-scale=' + (document.documentElement.clientWidth / 1024)
                );
                """
}