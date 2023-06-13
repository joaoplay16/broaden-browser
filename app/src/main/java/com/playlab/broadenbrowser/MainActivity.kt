package com.playlab.broadenbrowser

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.playlab.broadenbrowser.ui.screens.BrowserScreen
import com.playlab.broadenbrowser.ui.theme.BroadenBrowserTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BroadenBrowserTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isInFullScreenMode by remember { mutableStateOf(false) }

                    BrowserScreen(
                        isInFullscreenMode = isInFullScreenMode,
                        onEnterFullScreenClick = {
                            enterFullScreenMode()
                            isInFullScreenMode = true
                        },
                        onExitFullScreenClick = {
                            leaveFullScreenMode()
                            isInFullScreenMode = false
                        }
                    )
                }
            }
        }
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