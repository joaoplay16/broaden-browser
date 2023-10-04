package com.playlab.broadenbrowser.ext

import android.view.View
import android.view.ViewGroup

fun View.removeFromParent() {
    val parent = parent as? ViewGroup
    parent?.removeView(this)
}