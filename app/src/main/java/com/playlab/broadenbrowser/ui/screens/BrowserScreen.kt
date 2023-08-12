package com.playlab.broadenbrowser.ui.screens

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberSaveableWebViewState
import com.google.accompanist.web.rememberWebViewNavigator
import com.playlab.broadenbrowser.R
import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.model.TabPage
import com.playlab.broadenbrowser.ui.components.BottomSheetContent
import com.playlab.broadenbrowser.ui.components.SearchBar
import com.playlab.broadenbrowser.ui.components.TabCounter
import com.playlab.broadenbrowser.ui.screens.common.BrowserState
import com.playlab.broadenbrowser.ui.screens.common.UiEvent
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme
import com.playlab.broadenbrowser.ui.utils.Constants
import com.playlab.broadenbrowser.ui.utils.Util.isUrl
import com.playlab.broadenbrowser.ui.utils.Util.setDesktopSite
import com.playlab.broadenbrowser.ui.utils.Util.toSearchMechanismUrl
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
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
        _, _,
        currentTab
    ) = browserState

    val isInEditMode = LocalInspectionMode.current

    val webViewState = rememberSaveableWebViewState()

    var searchBarValue by remember { mutableStateOf(TextFieldValue("")) }

    var browserOptionsMenuExpanded by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipHiddenState = false,
            skipPartiallyExpanded = false
        )
    )

    val navigator = rememberWebViewNavigator()

    val context = LocalContext.current

    var isDesktopSite by remember { mutableStateOf(false) }

    val webViewInstance = remember { WebView(context) }

    var isSearchBarFocused by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    val customTextSelectionColors = TextSelectionColors(
        handleColor =  if(searchBarValue.selection == TextRange(0, searchBarValue.text.length))
            Transparent
        else LocalTextSelectionColors.current.handleColor,
        backgroundColor = LocalTextSelectionColors.current.backgroundColor
    )

    LaunchedEffect(
        externalLink
    ) {
        if (externalLink != null) {
            onEvent(
                UiEvent.OnSaveTab(
                    TabPage(
                        title = externalLink,
                        url = externalLink,
                        timestamp = System.currentTimeMillis()
                    )
                )
            )
            navigator.loadUrl(externalLink)
        }
    }

    LaunchedEffect(
        key1 = webViewState.isLoading,
        block = {
            with(webViewState) {
                lastLoadedUrl?.let {
                    searchBarValue = TextFieldValue(text = it)
                }

                if (isLoading.not()) {
                    currentTab?.let { tab ->
                        val title = pageTitle ?: tab.title
                        val url = lastLoadedUrl ?: tab.url
                        val timestamp = System.currentTimeMillis()

                        onEvent(
                            UiEvent.OnEditTab(
                                tab.copy(
                                    title = title,
                                    url = url,
                                    timestamp = timestamp
                                )
                            )
                        )

                        onEvent(
                            UiEvent.OnSaveHistoryPage(
                                HistoryPage(
                                    title = title,
                                    url = url,
                                    timestamp = timestamp
                                )
                            )
                        )
                    }

                    // Applies the desktop viewport after the page has been loaded
                    if(isDesktopSite){
                        webViewInstance.evaluateJavascript(
                            Constants.SET_DESKTOP_VIEWPORT_SCRIPT,
                            null
                        )
                    }
                }
            }
        }
    )

    LaunchedEffect(
        key1 = isSearchBarFocused,
        block = {
            if(isSearchBarFocused){
                searchBarValue = searchBarValue.copy(
                    selection = TextRange(0, searchBarValue.text.length)
                )
            }
        }
    )

    LaunchedEffect(
        key1 = currentTab,
        block = {
            if(currentTab == null) {
                webViewInstance.visibility = View.GONE
            }else{
                webViewInstance.visibility = View.VISIBLE
            }
        }
    )

    /* clear the focus of the SearchBar on pressing system back button */
    BackHandler(enabled = isSearchBarFocused) {
        focusManager.clearFocus()
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            BottomSheetContent(
                browserState = browserState,
                onUiEvent = { event ->
                    onEvent(event)
                    if (event is UiEvent.OnNewTab) {
                        searchBarValue = TextFieldValue()

                        // Load selected tab from tab list
                        event.tabPage?.let {
                            navigator.loadUrl(it.url)
                        }

                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.hide()
                        }
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
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
                            webViewInstance
                        }
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
            }
            if (isInFullscreenMode.not()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (isSearchBarFocused.not()) {
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
                    }
                    CompositionLocalProvider(
                        LocalTextSelectionColors provides customTextSelectionColors,
                    ) {
                        SearchBar(
                            modifier = Modifier
                                .then(
                                    if (isSearchBarFocused) {
                                        Modifier.padding(
                                            start = dimensionResource(id = R.dimen.search_bar_item_hr_padding),
                                            end = dimensionResource(
                                                id = R.dimen
                                                    .search_bar_item_hr_padding
                                            )
                                        )
                                    } else {
                                        Modifier
                                    }
                                )
                                .padding(vertical = dimensionResource(id = R.dimen.search_bar_item_hr_padding))
                                .weight(1f)
                                .animateContentSize()
                                .onFocusChanged {
                                    isSearchBarFocused = it.isFocused
                                },
                            value = searchBarValue,
                            onValueChange = { searchBarValue = it },
                            onClearClick = { searchBarValue = TextFieldValue("") },
                            onSearch = {
                                val url = searchBarValue.let {
                                    if (it.text.isUrl()) it.text
                                    else it.text.toSearchMechanismUrl(searchMechanism)
                                }
                                navigator.loadUrl(url)

                                searchBarValue = searchBarValue.copy(text = url)

                                if (currentTab == null) {
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

                                focusManager.clearFocus()
                            }
                        )
                    }
                    if (isSearchBarFocused.not()) {

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
                                isDesktopSite = isDesktopSite,
                                onNewTabClick = {
                                    searchBarValue = TextFieldValue()
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
                                    isDesktopSite = !isDesktopSite
                                    webViewInstance.setDesktopSite(isDesktopSite)
                                    navigator.reload()
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
                                    if (currentTab != null) navigator.reload()
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
}

@Composable
private fun BrowserOptionsMenu(
    expanded: Boolean,
    enableArrowLeft: Boolean,
    enableArrowRight: Boolean,
    isDesktopSite: Boolean,
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
                    contentDescription = null,
                    tint = if (isDesktopSite) MaterialTheme.colorScheme.tertiary else
                        LocalContentColor.current
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
                isDesktopSite = true,
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