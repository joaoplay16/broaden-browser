package com.playlab.broadenbrowser

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.playlab.broadenbrowser.ui.screens.BrowserScreen
import com.playlab.broadenbrowser.ui.screens.BrowserViewModel
import com.playlab.broadenbrowser.ui.screens.SettingsScreen
import com.playlab.broadenbrowser.ui.screens.common.ScreenRoutes
import com.playlab.broadenbrowser.ui.screens.common.UiEvent
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme
import com.playlab.broadenbrowser.ui.utils.Util.isDefaultBrowser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val browserViewModel: BrowserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        var externalLink: String? = null
        if (Intent.ACTION_VIEW == intent.action && intent.data != null) {
            externalLink = intent.data.toString()
        }

        setContent {
            BroadenBrowserTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isInFullScreenMode = browserViewModel.state.isInFullscreen

                    LaunchedEffect(
                        key1 = isInFullScreenMode
                    ) {
                        if (isInFullScreenMode) {
                            enterFullScreenMode()
                        } else {
                            leaveFullScreenMode()
                        }
                    }

                    DefaultNavController(
                        browserViewModel = browserViewModel,
                        externalLink = externalLink
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        browserViewModel.onUiEvent(
            UiEvent.OnSetAsDefaultBrowser(isDefaultBrowser(this))
        )
    }

    private fun enterFullScreenMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        val windowInsetsCompat = WindowInsetsControllerCompat(
            window,
            window.decorView
        )
        windowInsetsCompat.hide(WindowInsetsCompat.Type.navigationBars())
        windowInsetsCompat.hide(WindowInsetsCompat.Type.statusBars())
        windowInsetsCompat.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    private fun leaveFullScreenMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        val windowInsetsCompat = WindowInsetsControllerCompat(
            window,
            window.decorView
        )
        windowInsetsCompat.show(WindowInsetsCompat.Type.navigationBars())
        windowInsetsCompat.show(WindowInsetsCompat.Type.statusBars())
        windowInsetsCompat.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    }
}

@Composable
fun DefaultNavController(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    browserViewModel: BrowserViewModel,
    startDestination: String = ScreenRoutes.HOME.name,
    externalLink: String? = null
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(ScreenRoutes.HOME.name) {
            BrowserScreen(
                onEvent = browserViewModel::onUiEvent,
                browserState = browserViewModel.state.copy(
                    externalLink = externalLink
                ),
                onSettingClick = {
                    navController.navigate(ScreenRoutes.SETTINGS.name)
                }
            )
        }

        composable(ScreenRoutes.SETTINGS.name){
            SettingsScreen(
                onEvent = browserViewModel::onUiEvent,
                browserState = browserViewModel.state,
                onArrowBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}