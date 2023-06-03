package com.playlab.broadenbrowser.ui.screens

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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

    var browserOptionsMenuExpanded by remember { mutableStateOf(false) }

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
                onClearClick = { searchBarText = "" },
                onSearch = { }
            )

            Spacer(
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // TODO: Add click action
            // TODO: Provide counter value
            TabCounter(
                modifier = Modifier.clickable { },
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

            Box {
                Icon(
                    modifier = Modifier.clickable { browserOptionsMenuExpanded = true },
                    painter = painterResource(id = R.drawable.dots_vertical),
                    contentDescription = stringResource(id = R.string.menu_icon_cd),
                    tint = MaterialTheme.colorScheme.outline
                )
                BrowserOptionsMenu(
                    expanded = browserOptionsMenuExpanded,
                    onDismissRequest = { browserOptionsMenuExpanded = false }
                )
            }
            Spacer(
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun BrowserOptionsMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit = {}
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = null
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.new_tab)
                )
            },
            onClick = { /*TODO implement new tab click action*/ },
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 22.dp
            )
        )

        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.star_shooting_outline),
                    contentDescription = null
                )
            },
            trailingIcon = {
                Row(
                    modifier = Modifier.clickable {
                        // TODO: implement bookmark click action
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .scale(0.8f)
                            .padding(end = 6.dp),
                        painter = painterResource(id = R.drawable.star_outline),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = stringResource(id = R.string.add),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            },
            text = {
                Text(
                    text = stringResource(id = R.string.bookmarks),
                )
            },
            onClick = { /*TODO implement bookmarks click action*/ },
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 22.dp
            )
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.clock_outline),
                    contentDescription = null
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.history)
                )
            },
            onClick = { /*TODO implement history click action*/ },
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 22.dp
            )
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.monitor),
                    contentDescription = null
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.desktop_site)
                )
            },
            onClick = { /*TODO implement desktop site click action*/ },
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 22.dp
            )
        )
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.tune_vertical_variant),
                    contentDescription = null
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.settings)
                )
            },
            onClick = { /*TODO implement settings click action*/ },
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 22.dp
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outline.copy(0.2f))
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = { /*TODO* implement arrow left click action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = stringResource(id = R.string.ic_arrow_left_cd)
                )
            }
            IconButton(onClick = { /*TODO* implement arrow right click action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = stringResource(id = R.string.ic_arrow_right_cd)
                )
            }
            IconButton(onClick = { /*TODO* implement share click action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.share_variant_outline),
                    contentDescription = stringResource(id = R.string.ic_share_cd)
                )
            }
            IconButton(onClick = { /*TODO* implement reload click action */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.reload),
                    contentDescription = stringResource(id = R.string.ic_reload_cd)
                )
            }
        }
    }
}

@Preview(
    uiMode = UI_MODE_NIGHT_MASK,
    showBackground = true
)
@Composable
fun DropdownPreview() {
    BroadenBrowserTheme(true) {
        Surface {
            BrowserOptionsMenu(true)
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