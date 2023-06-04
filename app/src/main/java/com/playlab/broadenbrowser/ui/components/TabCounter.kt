package com.playlab.broadenbrowser.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playlab.broadenbrowser.ui.components.TabCounterDefaults.tabCounterColors
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme


data class TabCounterColors(
    val containerColor: Color,
    val borderColor: Color,
    val textColor: Color,
)

object TabCounterDefaults {
    @Composable
    fun tabCounterColors(): TabCounterColors {
        return TabCounterColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            borderColor = MaterialTheme.colorScheme.outline,
            textColor = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun TabCounter(
    modifier: Modifier = Modifier,
    count: Int,
    colors: TabCounterColors = tabCounterColors()
) {
    Text(
        modifier = modifier
            .size(18.dp)
            .background(
                color = colors.containerColor,
                shape = RoundedCornerShape(3.dp)
            )
            .border(
                1.5.dp,
                colors.borderColor,
                RoundedCornerShape(3.dp)
            ),
        fontSize = 12.sp,
        color = colors.textColor,
        textAlign = TextAlign.Center,
        text = count.toString()
    )
}

@Preview
@Composable
fun TabCounterPreview() {
    BroadenBrowserTheme {
        Surface {
            Column {
                TabCounter(count = 2)
            }
        }
    }
}

@Preview
@Composable
fun TabCounterDarkPreview() {
    BroadenBrowserTheme(true) {
        Surface {
            TabCounter(
                modifier = Modifier.clickable {  },
                count = 2
            )
        }
    }
}