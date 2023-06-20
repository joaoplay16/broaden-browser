package com.playlab.broadenbrowser.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns

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

    fun isDefaultBrowser(context: Context): Boolean{
        val defaultBrowserPkg: String? = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://google.com")
        ).resolveActivity(context.packageManager)?.packageName

        return context.packageName == defaultBrowserPkg
    }
}
