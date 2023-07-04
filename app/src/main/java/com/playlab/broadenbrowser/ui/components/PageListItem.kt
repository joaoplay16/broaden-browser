package com.playlab.broadenbrowser.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playlab.broadenbrowser.mocks.MockTabPages
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme

@Composable
fun PageListItem(
    modifier: Modifier = Modifier,
    title: String,
    url: String,
    buttonSlot: @Composable () -> Unit = {}
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier
                .padding(bottom = 4.dp)
                .weight(1f)) {
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    text = title
                )
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth(),
                    text = url,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            buttonSlot()
        }
    }
}

@Preview
@Composable
fun PagePreview() {
    BroadenBrowserTheme {
        Surface {
            Column (
                modifier = Modifier.padding(8.dp),
            ){
                PageListItem(
                    modifier = Modifier.padding(bottom = 4.dp),
                    title = "Lorem ipsum dolor",
                    url = "https://loremipsum.com.br",
                )
                PageListItem(
                    modifier = Modifier.padding(bottom = 4.dp),
                    title = "Lorem ipsum dolor",
                    url = "https://loremipsum.com.br",
                )
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PagePreviewDark() {
    BroadenBrowserTheme {
        Surface {
            Column (
                modifier = Modifier.padding(8.dp),
            ){
                PageListItem(
                    modifier = Modifier.padding(bottom = 4.dp),
                    title = MockTabPages.tab1.title,
                    url =  MockTabPages.tab1.url,
                    buttonSlot = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = ""
                            )
                        }
                    }
                )
                PageListItem(
                    modifier = Modifier.padding(bottom = 4.dp),
                    title = MockTabPages.tab2.title,
                    url = MockTabPages.tab2.url,
                    buttonSlot = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = ""
                            )
                        }
                    }
                )
            }
        }
    }
}