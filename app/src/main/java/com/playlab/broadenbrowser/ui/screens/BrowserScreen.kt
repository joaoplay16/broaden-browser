package com.playlab.broadenbrowser.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrowserScreen(
    modifier: Modifier = Modifier
) {
    val state = rememberWebViewState("https://m3.material.io/")

    WebView(
        modifier = modifier.fillMaxSize(),
        state = state,
        onCreated = {
            it.settings.domStorageEnabled = true
            it.settings.javaScriptEnabled = true
        }
    )
}

@Preview
@Composable
fun PreviewBrowserScreen() {
    BroadenBrowserTheme {
        Surface {
            BrowserScreen()
        }
    }
}