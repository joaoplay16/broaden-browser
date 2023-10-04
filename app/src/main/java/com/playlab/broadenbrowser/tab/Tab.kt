package com.playlab.broadenbrowser.tab

import androidx.compose.runtime.mutableStateOf
import com.playlab.broadenbrowser.model.TabPage
import com.playlab.broadenbrowser.web.MyWebView

data class Tab(
    val info: TabPage,
    var view: MyWebView,
) {

    var parentTab: Tab? = null

    val urlState = mutableStateOf("")
    val canGoBackState = mutableStateOf(false)
    val canGoForwardState = mutableStateOf(false)

    fun onCloseWindow() {
        TabManager.remove(this)
    }

    fun loadUrl(url: String) {
        urlState.value = url
        view.post {
            view.loadUrl(url)
        }
    }

    val isHome
        get() = urlState.value == "about:blank"

    fun goHome() {
        view.post {
            view.loadUrl("about:blank")
        }
    }

    fun onResume() {
        view.onResume()
    }

    fun onPause() {
        view.onPause()
    }

    fun onBackPressed(): Boolean {
        if (view.canGoBack()) {
            view.goBack()
            return true
        }
        parentTab?.let {
            TabManager.remove(this)
            TabManager.active(it)
            return true
        }
        return false
    }

    fun goForward() {
        if (view.canGoForward()) {
            view.goForward()
        }
    }
}