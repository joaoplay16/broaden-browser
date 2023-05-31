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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme

@Composable
fun TabCounter(
    modifier: Modifier = Modifier,
    count: Int,
) {
    Text(
        modifier = modifier
            .size(18.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(3.dp)
            )
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(3.dp)
            ),
        fontSize = 12.sp,
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