package com.playlab.broadenbrowser.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.playlab.broadenbrowser.R
import com.playlab.broadenbrowser.ui.components.SearchBar
import com.playlab.broadenbrowser.ui.components.TabCounter
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrowserScreen(
    modifier: Modifier = Modifier
) {
    val isInEditMode = LocalInspectionMode.current
    val webViewState = rememberWebViewState("https://m3.material.io/")

    var searchBarText by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Bottom
    ) {
        if (isInEditMode) {
            Box(
                modifier = modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color.Gray)
            )
        } else {
            WebView(
                modifier = modifier.weight(1f),
                state = webViewState,
                onCreated = {
                    it.settings.domStorageEnabled = true
                    it.settings.javaScriptEnabled = true
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // TODO: Implement onSearch action
            SearchBar(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .padding(vertical = 8.dp)
                    .weight(1f),
                text = searchBarText,
                onTextChange = { searchBarText = it },
                onClearClick = { searchBarText = ""},
                onSearch = { }
            )

            Spacer(
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // TODO: Add click action
            // TODO: Provide counter value
            TabCounter(
                modifier = Modifier.clickable {  },
                count = 0
            )

            Spacer(
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // TODO: Add click action
            Icon(
                modifier = Modifier
                    .clickable { },
                imageVector = Icons.Default.Expand,
                contentDescription = stringResource(id = R.string.expand_icon_cd),
                tint = MaterialTheme.colorScheme.outline
            )

            Spacer(
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // TODO: Add click action
            // TODO: Change the temporary icon
            Icon(
                modifier = Modifier
                    .clickable { },
                painter = painterResource(id = R.drawable.dots_vertical),
                contentDescription = stringResource(id = R.string.menu_icon_cd),
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun BrowserScreenPreview() {
    BroadenBrowserTheme {
        Surface {
            BrowserScreen()
        }
    }
}

@Preview
@Composable
fun BrowserScreenDarkPreview() {
    BroadenBrowserTheme(true) {
        Surface {
            BrowserScreen()
        }
    }
}