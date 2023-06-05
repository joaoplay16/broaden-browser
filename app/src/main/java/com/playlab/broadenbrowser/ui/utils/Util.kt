package com.playlab.broadenbrowser.ui.utils

import android.util.Patterns

object Util {

    fun String.isUrl(): Boolean {
        return Patterns.WEB_URL.matcher(this).matches()
    }
}
