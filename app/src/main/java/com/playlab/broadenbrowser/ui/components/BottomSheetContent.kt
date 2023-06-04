package com.playlab.broadenbrowser.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme

@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(TabPage.OpenTabs) }

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

            // TODO: Add click action
            // TODO: Provide counter value
            SheetTabBar(
                modifier = Modifier.width(180.dp),
                openTabsCount = 0,
                tabPage = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
        Column(
            modifier = Modifier.sizeIn(
                minHeight = 400.dp,
                maxHeight = 600.dp
            )
        ) {

        }
    }
}

@Preview
@Composable
fun BottomSheetContentPreview() {
    BroadenBrowserTheme {
        Surface {
            BottomSheetContent()
        }
    }
}