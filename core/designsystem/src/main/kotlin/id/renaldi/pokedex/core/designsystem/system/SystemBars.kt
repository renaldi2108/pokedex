package id.renaldi.pokedex.core.designsystem.system

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SystemBarIcons(
    darkStatusBarIcons: Boolean,
    darkNavigationBarIcons: Boolean = darkStatusBarIcons,
) {
    val view = LocalView.current
    if (view.isInEditMode) return
    val window = view.context.findWindow() ?: return

    SideEffect {
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = darkStatusBarIcons
            isAppearanceLightNavigationBars = darkNavigationBarIcons
        }
    }
}

private fun Context.findWindow(): Window? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context.window
        context = context.baseContext
    }
    return null
}
