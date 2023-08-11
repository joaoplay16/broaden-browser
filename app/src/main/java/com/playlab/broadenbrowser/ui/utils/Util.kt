package com.playlab.broadenbrowser.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.webkit.WebView
import java.util.Calendar

object Util {

    fun String.isUrl(): Boolean {
        return Patterns.WEB_URL.matcher(this).matches()
    }

    fun String.toSearchMechanismUrl(
        searchMechanism: SearchMechanism = SearchMechanism.GOOGLE
    ): String {
        return when (searchMechanism) {
            SearchMechanism.BING -> "https://www.bing.com/search?q=$this"
            SearchMechanism.YAHOO -> "https://search.yahoo.com/search?p=$this"
            SearchMechanism.BAIDU -> "https://www.baidu.com/s?wd=$this"
            SearchMechanism.DUCK_DUCK_GO -> "https://duckduckgo.com/html/?q=$this"
            else -> "https://www.google.com/search?q=$this"
        }
    }

    fun isDefaultBrowser(context: Context): Boolean {
        val defaultBrowserPkg: String? = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://google.com")
        ).resolveActivity(context.packageManager)?.packageName

        return context.packageName == defaultBrowserPkg
    }

    fun isDateToday(timestamp: Long): Boolean {

        val calendar = Calendar.getInstance()

        calendar.timeInMillis = timestamp

        val today = Calendar.getInstance()

        return (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH))
    }

    fun WebView.setDesktopSite(isDesktopMode: Boolean){
        if (isDesktopMode) {
            settings.userAgentString = Constants.DEFAULT_USER_AGENT_STRING
            /* Sets whether the WebView should enable support for the
               "viewport" HTML meta tag or should use a wide viewport.*/
            settings.useWideViewPort = true
            /* Sets whether the WebView loads pages in overview mode, that
               is, zooms out the content to fit on screen by width. */
            settings.loadWithOverviewMode = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
        } else {
            // set mobile mode
            settings.userAgentString = null
            settings.useWideViewPort = false
            settings.loadWithOverviewMode = false
        }
    }
}
