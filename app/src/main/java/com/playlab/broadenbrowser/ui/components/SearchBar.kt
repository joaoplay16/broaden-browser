package com.playlab.broadenbrowser.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playlab.broadenbrowser.R
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onClearClick: () -> Unit,
    onSearch: () -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        shape = RoundedCornerShape(32.dp),
        onValueChange = onValueChange,
        placeholder = {
            Text(
                modifier = Modifier
                    .alpha(alpha = 0.5f),
                text = stringResource(id = R.string.search_bar_placeholder),
                fontSize = dimensionResource(id = R.dimen.search_bar_font_size).value.sp,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = dimensionResource(id = R.dimen.search_bar_font_size).value.sp
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() }
        ),
        colors = TextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary.copy(0.6f),
        ),
        trailingIcon = {
            if (value.text.isNotEmpty()) {
                IconButton(
                    onClick = onClearClick) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(id = R.string.clear_icon_cd)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun SearchBarPreview() {
    BroadenBrowserTheme {
        Surface {
            var textFieldValue by remember {
                mutableStateOf(TextFieldValue(""))
            }

            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                onClearClick = { textFieldValue = TextFieldValue("") }
            )
        }
    }
}