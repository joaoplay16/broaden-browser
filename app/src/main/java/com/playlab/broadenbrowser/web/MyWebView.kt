package com.playlab.broadenbrowser.web

import android.content.Context
import android.view.ViewGroup
import android.webkit.WebView

class MyWebView(context: Context) : WebView(context) {
    override fun onPause() {
        super.onPause()
        evaluateJavascript(
            "if(window.localStream){window.localStream.stop();}",
            null
        )
    }

    fun onDestroy() {
        val parent = parent as? ViewGroup
        parent?.removeView(this)
        stopLoading()
        onPause()
        removeAllViews()
        destroy()
    }
}