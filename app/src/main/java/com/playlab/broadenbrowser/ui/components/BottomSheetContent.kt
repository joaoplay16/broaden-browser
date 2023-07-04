package com.playlab.broadenbrowser.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playlab.broadenbrowser.R
import com.playlab.broadenbrowser.mocks.MockTabPages
import com.playlab.broadenbrowser.model.TabPage
import com.playlab.broadenbrowser.ui.screens.common.BrowserState
import com.playlab.broadenbrowser.ui.screens.common.UiEvent
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme

@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    browserState: BrowserState = rememberBrowserState().value,
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
                    maxHeight = 600.dp
                )
        ) {
            when(selectedTab){
                SheetTabBarSection.OpenTabs -> {
                    TabsSection(
                        tabs = browserState.tabs,
                        onCloseTabs = {
                            onUiEvent(UiEvent.OnCloseTabs(it))
                        }
                    )
                }
                SheetTabBarSection.Favorites -> {
                    // TODO: Add favorites section
                }
                SheetTabBarSection.History -> {
                    // TODO: Add history section
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsSection(
    tabs: List<TabPage>,
    onCloseTabs: (List<TabPage>) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(
            items = tabs,
            key = { it.id }) { page ->
            PageListItem(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .animateItemPlacement(),
                title = page.title,
                url = page.url,
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