package com.playlab.broadenbrowser.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.playlab.broadenbrowser.ui.screens.common.BrowserState

@Composable
fun rememberBrowserState( initial: BrowserState? = null) : MutableState<BrowserState> {
    return remember{ mutableStateOf(initial ?: BrowserState()) }
}