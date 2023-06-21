package com.playlab.broadenbrowser.ui.screens

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playlab.broadenbrowser.R
import com.playlab.broadenbrowser.ui.screens.common.BrowserState
import com.playlab.broadenbrowser.ui.screens.common.DevicesPreviews
import com.playlab.broadenbrowser.ui.screens.common.UiEvent
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme
import com.playlab.broadenbrowser.ui.utils.SearchMechanisms

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    browserState: BrowserState,
    onEvent: (UiEvent) -> Unit,
    onArrowBackPressed: () -> Unit,
) {
    Scaffold(topBar = {
        Row(
            Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(16.dp).clickable {
                        onArrowBackPressed()
                    },
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.arrow_back_icon_content_description)
                )
                Text(
                    text = stringResource(id = R.string.settings_screen_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = dimensionResource(id = R.dimen.screen_title_font_size).value.sp
                )
            }
        }
    }) { paddingValues ->
        Column(
            modifier = modifier.scrollable(
                rememberScrollState(),
                Orientation.Vertical
            ).padding(paddingValues).padding(horizontal = 16.dp)
        ) {


            val (
                _,
                isStartInFullscreenEnabled,
                isJavascriptAllowed,
                isDarkThemeEnabled,
                searchMechanism,
                _,
            ) = browserState

            val searchMechanismName = SearchMechanisms.getValue(searchMechanism)

            var searchDropDownExpanded by remember { mutableStateOf(false) }

            val actForResult = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = {}
            )

            Text(
                text = stringResource(id = R.string.settings_category_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Column(
                modifier = Modifier
                    .clickable { searchDropDownExpanded = true }
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(id = R.string.search_mechanism),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = searchMechanismName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light
                )
                DropdownMenu(
                    expanded = searchDropDownExpanded,
                    onDismissRequest = { searchDropDownExpanded = false }) {

                    SearchMechanisms.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.value) },
                            onClick = {
                                onEvent(UiEvent.OnSetSearchMechanism(it.key))
                                searchDropDownExpanded = false
                            }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.enable_dark_theme),
                    style = MaterialTheme.typography.titleMedium,
                )

                Switch(
                    modifier = Modifier.scale(0.7f),
                    checked = isDarkThemeEnabled,
                    onCheckedChange = {
                        onEvent(UiEvent.OnEnableDarkTheme(it))
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.start_in_fullscreen),
                    style = MaterialTheme.typography.titleMedium,
                )
                Switch(
                    modifier = Modifier.scale(0.7f),
                    checked = isStartInFullscreenEnabled,
                    onCheckedChange = {
                        onEvent(UiEvent.OnEnableStartInFullscreen(it))
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.enable_javascript),
                    style = MaterialTheme.typography.titleMedium,
                )
                Switch(
                    modifier = Modifier.scale(0.7f),
                    checked = isJavascriptAllowed,
                    onCheckedChange = { onEvent(UiEvent.OnAllowJavascript(it)) }
                )
            }
             /*Don't show when versions below nougat as there is no way to redirect
              the user to the default browser chooser.*/
            if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
                Row(
                    modifier = Modifier
                        .clickable {
                            val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                            actForResult.launch(intent)
                        }
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.set_as_default_browser),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Switch(
                        modifier = Modifier.scale(0.7f),
                        checked = browserState.isDefaultBrowser,
                        onCheckedChange = {

                        }
                    )
                }
            }

            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = stringResource(id = R.string.settings_about_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                modifier = Modifier
                    // TODO: implement Rate on Google Play click
                    .clickable { }.fillMaxWidth().padding(vertical = 10.dp),
                text = stringResource(id = R.string.rate_on_google_play),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                modifier = Modifier
                    // TODO: implement Privacy Policy click
                    .clickable { }.fillMaxWidth().padding(vertical = 10.dp),
                text = stringResource(id = R.string.privacy_policy),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    BroadenBrowserTheme {
        Surface {
            var browserState by remember {
                mutableStateOf(BrowserState())
            }
            SettingsScreen(
                browserState = browserState,
                onEvent = { uiEvent ->
                    when (uiEvent) {
                        is UiEvent.OnAllowJavascript -> {
                            browserState = browserState.copy(isJavascriptAllowed = uiEvent.allowed)
                        }

                        is UiEvent.OnEnableFullscreen -> {
                            browserState = browserState.copy(isInFullscreen = uiEvent.enabled)
                        }

                        is UiEvent.OnEnableStartInFullscreen -> {
                            browserState =
                                browserState.copy(isStartInFullscreenEnabled = uiEvent.enabled)
                        }

                        is UiEvent.OnEnableDarkTheme -> {
                            browserState = browserState.copy(isDarkThemeEnabled = uiEvent.enabled)
                        }

                        is UiEvent.OnSetSearchMechanism -> {
                            browserState =
                                browserState.copy(searchMechanism = uiEvent.searchMechanism)
                        }

                        is UiEvent.OnSetAsDefaultBrowser -> {
                            browserState =
                                browserState.copy(isDefaultBrowser = uiEvent.isDefaultBrowser)
                        }
                    }
                },
                onArrowBackPressed = {}
            )
        }
    }
}


@DevicesPreviews
@Composable
fun SettingsScreenPreviewDark() {
    BroadenBrowserTheme(true) {
        Surface {
            var browserState by remember {
                mutableStateOf(BrowserState())
            }
            SettingsScreen(
                browserState = browserState,
                onEvent = { uiEvent ->
                    when (uiEvent) {
                        is UiEvent.OnAllowJavascript -> {
                            browserState = browserState.copy(isJavascriptAllowed = uiEvent.allowed)
                        }

                        is UiEvent.OnEnableFullscreen -> {
                            browserState = browserState.copy(isInFullscreen = uiEvent.enabled)
                        }

                        is UiEvent.OnEnableStartInFullscreen -> {
                            browserState =
                                browserState.copy(isStartInFullscreenEnabled = uiEvent.enabled)
                        }

                        is UiEvent.OnEnableDarkTheme -> {
                            browserState = browserState.copy(isDarkThemeEnabled = uiEvent.enabled)
                        }

                        is UiEvent.OnSetSearchMechanism -> {
                            browserState =
                                browserState.copy(searchMechanism = uiEvent.searchMechanism)
                        }

                        is UiEvent.OnSetAsDefaultBrowser -> {
                            browserState =
                                browserState.copy(isDefaultBrowser = uiEvent.isDefaultBrowser)
                        }
                    }
                },
                onArrowBackPressed = {}
            )
        }
    }
}