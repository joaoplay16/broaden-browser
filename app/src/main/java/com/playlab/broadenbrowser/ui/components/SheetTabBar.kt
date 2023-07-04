package com.playlab.broadenbrowser.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playlab.broadenbrowser.R
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme

enum class SheetTabBarSection {
    OpenTabs, Favorites, History
}

@Composable
fun SheetTabBar(
    modifier: Modifier = Modifier,
    openTabsCount: Int,
    tabSection: SheetTabBarSection,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    onTabSectionSelected: (tabSection: SheetTabBarSection) -> Unit,
) {
    TabRow(
        modifier = modifier,
        selectedTabIndex = tabSection.ordinal,
        containerColor = backgroundColor,
        divider = {},
        indicator = { tabPositions ->
            HomeTabIndicator(
                tabPositions,
                tabSection
            )
        }
    ) {

        val selectionColor: @Composable (SheetTabBarSection) -> Color = { page ->
            if (page == tabSection)
                MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline
        }

        IconButton(
            modifier = Modifier,
            onClick = { onTabSectionSelected(SheetTabBarSection.OpenTabs) }
        ) {
            TabCounter(
                colors = TabCounterDefaults.tabCounterColors().copy(
                    borderColor = selectionColor(SheetTabBarSection.OpenTabs),
                    textColor = selectionColor(SheetTabBarSection.OpenTabs)
                    ),
                count = openTabsCount
            )
        }

        IconButton(onClick = { onTabSectionSelected(SheetTabBarSection.Favorites) }) {
            Icon(
                painter = painterResource(id = R.drawable.star_shooting_outline),
                contentDescription = null,
                tint = selectionColor(SheetTabBarSection.Favorites)
            )
        }

        IconButton(onClick = { onTabSectionSelected(SheetTabBarSection.History) }) {
            Icon(
                painter = painterResource(id = R.drawable.clock_outline),
                contentDescription = null,
                tint = selectionColor(SheetTabBarSection.History)
            )
        }
    }
}

@Composable
private fun HomeTabIndicator(
    tabPositions: List<TabPosition>,
    tabSection: SheetTabBarSection
) {
    val transition = updateTransition(
        tabSection,
        label = "Tab indicator"
    )

    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            spring(stiffness = Spring.StiffnessMedium)
        },
        label = "Indicator left"
    ) { section ->
        tabPositions[section.ordinal].left
    }
    val indicatorRight by transition.animateDp(
        transitionSpec = {
            spring(stiffness = Spring.StiffnessMedium)
        },
        label = "Indicator right"
    ) { section ->
        tabPositions[section.ordinal].right
    }
    val color = MaterialTheme.colorScheme.primary

    Box(
        Modifier
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .drawBehind {
                drawLine(
                    color = color,
                    start = Offset(
                        0f,
                        size.height
                    ),
                    end = Offset(
                        size.width,
                        size.height
                    ),
                    strokeWidth = 18f
                )
            }
    )
}


@Preview(showBackground = true)
@Composable
fun HomeTabPreview() {
    BroadenBrowserTheme(false) {
        Surface {

            var selectedTabSection by remember { mutableStateOf(SheetTabBarSection.OpenTabs) }

            SheetTabBar(
                modifier = Modifier.width(140.dp),
                tabSection = selectedTabSection,
                openTabsCount = 0,
                onTabSectionSelected = { selectedTabSection = it }
            )
        }
    }
}