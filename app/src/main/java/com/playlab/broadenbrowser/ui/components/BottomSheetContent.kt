package com.playlab.broadenbrowser.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playlab.broadenbrowser.R
import com.playlab.broadenbrowser.mocks.MockTabPages
import com.playlab.broadenbrowser.model.HistoryPage
import com.playlab.broadenbrowser.model.TabPage
import com.playlab.broadenbrowser.ui.screens.common.BrowserState
import com.playlab.broadenbrowser.ui.screens.common.UiEvent
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme

@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    browserState: BrowserState,
    onUiEvent: (UiEvent) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(SheetTabBarSection.OpenTabs) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = Color.Black.copy(0.2f),
                        start = Offset(
                            0f,
                            size.height
                        ),
                        end = Offset(
                            size.width,
                            size.height
                        ),
                        strokeWidth = 2f
                    )
                }
        ) {
            SheetTabBar(
                modifier = Modifier.width(180.dp),
                openTabsCount = browserState.tabs.size,
                tabSection = selectedTab,
                onTabSectionSelected = { selectedTab = it }
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp)
                .sizeIn(
                    minHeight = 400.dp,
                    maxHeight = 450.dp
                )
        ) {
            Box(Modifier.fillMaxSize()) {
                when (selectedTab) {
                    SheetTabBarSection.OpenTabs -> {
                        TabsSection(
                            modifier = Modifier
                                .align(Alignment.TopCenter),
                            currentTabPage = browserState.currentTab,
                            tabs = browserState.tabs,
                            onCloseTabs = {
                                onUiEvent(UiEvent.OnCloseTabs(it))
                            },
                            onTabClick = {
                                onUiEvent(UiEvent.OnTabChange(tabPage = it))
                            }
                        )
                        FloatingActionButton(
                            modifier = Modifier
                                .padding(
                                    bottom = 36.dp,
                                    end = 8.dp
                                )
                                .align(alignment = Alignment.BottomEnd),
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary,
                            onClick = {
                                onUiEvent(UiEvent.OnTabChange(null))
                            }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(id = R.string.icon_add_cd)
                            )
                        }
                    }

                    SheetTabBarSection.Favorites -> {
                        // TODO: Add favorites section
                    }

                    SheetTabBarSection.History -> {
                        HistorySection(
                            modifier = Modifier.fillMaxSize(),
                            history = browserState.history,
                            onHistoryPageClick = { TODO("Implement click on history page") },
                            onDeleteHistoryPages = { onUiEvent(UiEvent.OnDeleteHistoryPages(it))},
                            onDeleteAllHistoryClick = { onUiEvent(UiEvent.OnDeleteAllHistoryPages) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsSection(
    tabs: List<TabPage>,
    currentTabPage: TabPage?,
    onCloseTabs: (List<TabPage>) -> Unit,
    modifier: Modifier = Modifier,
    onTabClick: (TabPage) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        items(
            items = tabs,
            key = { it.id }) { page ->
            PageListItem(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .animateItemPlacement()
                    .clickable {
                        onTabClick(page)
                    },
                title = page.title,
                url = page.url,
                border =
                if (page == currentTabPage) BorderStroke(
                    2.dp,
                    MaterialTheme.colorScheme.primary
                )
                else BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline
                ),
                buttonSlot = {
                    IconButton(onClick = { onCloseTabs(listOf(page)) }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.close_icon_cd)
                        )
                    }
                }
            )
        }
        item {
            Spacer(modifier = Modifier.padding(64.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistorySection(
    history: List<HistoryPage>,
    modifier: Modifier = Modifier,
    onHistoryPageClick: (HistoryPage) -> Unit,
    onDeleteHistoryPages: (List<HistoryPage>) -> Unit,
    onDeleteAllHistoryClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (history.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onDeleteAllHistoryClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.ic_delete_all_cd)
                    )
                }
            }
        }

        if (history.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.Top
            ) {
                items(
                    items = history,
                    key = { it.id }) { page ->
                    PageListItem(
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .animateItemPlacement()
                            .clickable {
                                onHistoryPageClick(page)
                            },
                        title = page.title,
                        border = BorderStroke(
                            0.dp,
                            Color.Transparent
                        ),
                        url = page.url,
                        buttonSlot = {
                            IconButton(onClick = { onDeleteHistoryPages(listOf(page)) }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(id = R.string.ic_delete_cd)
                                )
                            }
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.padding(16.dp))
                }
            }
        } else {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.no_history_here),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HistorySectionPreview() {
    BroadenBrowserTheme {
        Surface {
            var state by rememberBrowserState(
                BrowserState(history = emptyList())
            )
            HistorySection(
                modifier = Modifier.fillMaxSize(),
                history = state.history,
                onHistoryPageClick = { },
                onDeleteHistoryPages = {
                    state = state.copy(history = state.history.minus(it.toSet()))
                },
                onDeleteAllHistoryClick = {state = state.copy(history = emptyList()) }
            )
        }
    }
}

@Preview
@Composable
fun BottomSheetContentPreview() {
    BroadenBrowserTheme {
        Surface {
            var state by rememberBrowserState(
                BrowserState(tabs = MockTabPages.tabList)
            )
            BottomSheetContent(
                browserState = state,
                onUiEvent = { event ->
                    when (event) {
                        is UiEvent.OnCloseTabs -> {
                            state = state.copy(
                                tabs = state.tabs.minus(event.tabPages.toSet())
                            )
                        }

                        else -> {}
                    }
                }
            )
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun BottomSheetContentDarkPreview() {
    BroadenBrowserTheme {
        Surface {
            var state by rememberBrowserState(
                BrowserState(tabs = MockTabPages.tabList)
            )
            BottomSheetContent(
                browserState = state,
                onUiEvent = { event ->
                    when (event) {
                        is UiEvent.OnCloseTabs -> {
                            state = state.copy(
                                tabs = state.tabs.minus(event.tabPages.toSet())
                            )
                        }

                        else -> {}
                    }
                }
            )
        }
    }
}