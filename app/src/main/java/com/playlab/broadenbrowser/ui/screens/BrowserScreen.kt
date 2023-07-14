package com.playlab.broadenbrowser.ui.screens

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.webkit.WebSettings
import android.webkit.WebView
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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberSaveableWebViewState
import com.google.accompanist.web.rememberWebViewNavigator
import com.playlab.broadenbrowser.R
import com.playlab.broadenbrowser.model.TabPage
import com.playlab.broadenbrowser.ui.components.BottomSheetContent
import com.playlab.broadenbrowser.ui.components.SearchBar
import com.playlab.broadenbrowser.ui.components.TabCounter
import com.playlab.broadenbrowser.ui.screens.common.BrowserState
import com.playlab.broadenbrowser.ui.screens.common.UiEvent
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme
import com.playlab.broadenbrowser.ui.utils.Util.isUrl
import com.playlab.broadenbrowser.ui.utils.Util.toSearchMechanismUrl
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    modifier: Modifier = Modifier,
    browserState: BrowserState,
    onEvent: (UiEvent) -> Unit,
    onSettingClick: () -> Unit
) {

    val (
        isInFullscreenMode,
        _,
        isJavascriptAllowed,
        _,
        searchMechanism,
        externalLink,
        _,_,
        currentTab
    ) = browserState

    val isInEditMode = LocalInspectionMode.current

    val webViewState = rememberSaveableWebViewState()

    var searchBarText by remember { mutableStateOf("") }

    var browserOptionsMenuExpanded by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    val navigator = rememberWebViewNavigator()

    val context = LocalContext.current

    var isDesktopSite by remember { mutableStateOf(false) }

    val webViewInstance: WebView? by remember(currentTab) {
        derivedStateOf { if (currentTab == null) null else WebView(context) }
    }

    LaunchedEffect(
        externalLink
    ) {
        if (externalLink != null) {
            navigator.loadUrl(externalLink)
        }
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            BottomSheetContent(
                browserState = browserState,
                onUiEvent = onEvent
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom
        ) {
            if (isInEditMode) {
                Box(
                    modifier = modifier
                        .weight(1f),
                    Alignment.TopCenter,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.TopEnd)
                            .background(Color.Gray)
                    )
                    if (isInFullscreenMode) {
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.TopEnd),
                            onClick = { onEvent(UiEvent.OnEnableFullscreen(false)) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.fullscreen_exit),
                                contentDescription = stringResource(
                                    id = R.string.ic_exit_full_description
                                ),
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = modifier.weight(1f),
                    Alignment.TopCenter
                ) {
                    if( currentTab != null) {
                        WebView(
                            modifier = Modifier.fillMaxSize(),
                            navigator = navigator,
                            state = webViewState,
                            onCreated = {
                                it.settings.domStorageEnabled = true
                                it.settings.javaScriptEnabled = isJavascriptAllowed
                                it.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                            },
                            factory = {
                                  webViewInstance!!
                            }
                        )
                    }
                    if (isInFullscreenMode) {
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.TopEnd),
                            onClick = { onEvent(UiEvent.OnEnableFullscreen(false)) }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.fullscreen_exit),
                                contentDescription = stringResource(
                                    id = R.string.ic_exit_full_description
                                ),
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }
            if (isInFullscreenMode.not()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    SearchBar(
                        modifier = Modifier
                            .padding(start = dimensionResource(id = R.dimen.search_bar_item_hr_padding))
                            .padding(vertical = dimensionResource(id = R.dimen.search_bar_item_hr_padding))
                            .weight(1f),
                        text = searchBarText,
                        onTextChange = { searchBarText = it },
                        onClearClick = { searchBarText = "" },
                        onSearch = {
                            val url = if (searchBarText.isUrl()) searchBarText
                            else searchBarText.toSearchMechanismUrl(searchMechanism)
                            navigator.loadUrl(url)

                            searchBarText = url

                            if(currentTab == null){
                                onEvent(
                                    UiEvent.OnSaveTab(
                                        TabPage(
                                            title = url,
                                            url = url,
                                            timestamp = System.currentTimeMillis()
                                        )
                                    )
                                )
                            }
                        }
                    )

                    Spacer(
                        modifier = Modifier.padding(
                            horizontal = dimensionResource(id = R.dimen.search_bar_item_hr_padding)
                        )
                    )

                    TabCounter(
                        modifier = Modifier.clickable {
                            coroutineScope.launch {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        },
                        count = browserState.tabs.size
                    )

                    Spacer(
                        modifier = Modifier.padding(
                            horizontal = dimensionResource(id = R.dimen.search_bar_item_hr_padding)
                        )
                    )

                    Icon(
                        modifier = Modifier
                            .clickable {
                                onEvent(UiEvent.OnEnableFullscreen(true))
                            },
                        imageVector = Icons.Default.Expand,
                        contentDescription = stringResource(id = R.string.expand_icon_cd),
                        tint = MaterialTheme.colorScheme.outline
                    )

                    Spacer(
                        modifier = Modifier.padding(
                            horizontal = dimensionResource(id = R.dimen.search_bar_item_hr_padding)
                        )
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
                            onDismissRequest = { browserOptionsMenuExpanded = false },
                            enableArrowLeft = navigator.canGoBack,
                            enableArrowRight = navigator.canGoForward,
                            onNewTabClick = {
                                onEvent(UiEvent.OnNewTab(null))
                            },
                            onBookmarksClick = {
                                /*TODO: implement bookmarks click action*/
                            },
                            onAddBookmarksClick = {
                                /*TODO: implement add bookmark click action*/
                            },
                            onHistoryClick = {
                                /*TODO implement history click action*/
                            },
                            onDesktopSiteClick = {
                                webViewInstance?.apply {
                                    if (isDesktopSite) {
                                        // set desktop mode
                                        settings.userAgentString =
                                            "Mozilla/5.0 (X11; Linux x86_64; rv:109.0) Gecko/20100101 Firefox/113.0"
                                        /* Sets whether the WebView should enable support for the
                                        "viewport" HTML meta tag or should use a wide viewport.*/
                                        settings.useWideViewPort = true
                                        /* Sets whether the WebView loads pages in overview mode, that
                                        is, zooms out the content to fit on screen by width. */
                                        settings.loadWithOverviewMode = true
                                    } else {
                                        // set mobile mode
                                        settings.userAgentString = null
                                        settings.useWideViewPort = false
                                        settings.loadWithOverviewMode = false
                                    }
                                }
                                navigator.reload()
                                isDesktopSite = !isDesktopSite
                            },
                            onSettingClick = onSettingClick,
                            onArrowLeftClick = {
                                navigator.navigateBack()
                            },
                            onArrowRightClick = {
                                navigator.navigateForward()
                            },
                            onReloadClick = {
                                // check if the current tab has a loaded page
                                if(currentTab != null) navigator.reload()
                            },
                            onShareClick = {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        webViewState.lastLoadedUrl
                                    )
                                }
                                context.startActivity(intent)
                            }
                        )
                    }
                    Spacer(
                        modifier = Modifier.padding(
                            horizontal = dimensionResource(id = R.dimen.search_bar_item_hr_padding)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun BrowserOptionsMenu(
    expanded: Boolean,
    enableArrowLeft: Boolean,
    enableArrowRight: Boolean,
    onDismissRequest: () -> Unit = {},
    onNewTabClick: () -> Unit,
    onBookmarksClick: () -> Unit,
    onAddBookmarksClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onDesktopSiteClick: () -> Unit,
    onSettingClick: () -> Unit,
    onArrowLeftClick: () -> Unit,
    onArrowRightClick: () -> Unit,
    onShareClick: () -> Unit,
    onReloadClick: () -> Unit
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
            onClick = {
                onNewTabClick()
                onDismissRequest()
            },
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
                        onAddBookmarksClick()
                        onDismissRequest()
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
            onClick = {
                onBookmarksClick()
                onDismissRequest()
            },
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
            onClick = {
                onHistoryClick()
                onDismissRequest()
            },
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
            onClick = {
                onDesktopSiteClick()
                onDismissRequest()
            },
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
            onClick = {
                onSettingClick()
                onDismissRequest()
            },
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
            IconButton(onClick = onArrowLeftClick) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = stringResource(id = R.string.ic_arrow_left_cd),
                    tint = if (enableArrowLeft) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(0.5f)
                )
            }
            IconButton(onClick = onArrowRightClick) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_right),
                    contentDescription = stringResource(id = R.string.ic_arrow_right_cd),
                    tint = if (enableArrowRight) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(0.5f)
                )
            }
            IconButton(onClick = {
                onShareClick()
                onDismissRequest()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.share_variant_outline),
                    contentDescription = stringResource(id = R.string.ic_share_cd)
                )
            }
            IconButton(onClick = {
                onReloadClick()
                onDismissRequest()
            }) {
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
            BrowserOptionsMenu(
                expanded = false,
                enableArrowLeft = false,
                enableArrowRight = true,
                onDismissRequest = {},
                onNewTabClick = {},
                onBookmarksClick = {},
                onAddBookmarksClick = {},
                onHistoryClick = {},
                onDesktopSiteClick = {},
                onSettingClick = {},
                onReloadClick = {},
                onArrowLeftClick = {},
                onArrowRightClick = {},
                onShareClick = {}
            )
        }
    }
}

@Preview
@Composable
fun BrowserScreenPreview() {
    BroadenBrowserTheme {
        Surface {
            val browserState = remember { BrowserState() }

            BrowserScreen(
                onEvent = {},
                browserState = browserState,
                onSettingClick = {}
            )
        }
    }
}

@Preview
@Composable
fun BrowserScreenDarkPreview() {
    BroadenBrowserTheme(true) {
        Surface {
            val browserState = remember { BrowserState() }

            BrowserScreen(
                onEvent = {},
                browserState = browserState,
                onSettingClick = {}
            )
        }
    }
}