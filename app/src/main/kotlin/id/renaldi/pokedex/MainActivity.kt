package id.renaldi.pokedex

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import id.renaldi.pokedex.core.designsystem.theme.PokedexTheme
import id.renaldi.pokedex.feature.detail.ui.detailEntries
import id.renaldi.pokedex.feature.favorite.ui.favoriteEntries
import id.renaldi.pokedex.feature.pokedex.navigation.PokedexRoute
import id.renaldi.pokedex.feature.pokedex.ui.pokedexEntries
import id.renaldi.pokedex.feature.splash.navigation.SplashRoute
import id.renaldi.pokedex.feature.splash.ui.splashEntries
import io.github.renaldi2108.arus.nav.ArusHost
import io.github.renaldi2108.arus.nav.openStack
import io.github.renaldi2108.arus.nav.rememberArusGraph
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        setContent {
            PokedexRoot(deepLink = intent?.data?.toString())
        }
    }
}

@Composable
private fun PokedexRoot(deepLink: String? = null) {
    PokedexTheme {
        val snackbar = remember { SnackbarHostState() }

        val graph = rememberArusGraph(start = SplashRoute) {
            splashEntries(snackbar)
            pokedexEntries(snackbar)
            detailEntries(snackbar)
            favoriteEntries(snackbar)
        }

        LaunchedEffect(graph, deepLink) {
            val target = deepLink?.let { pokedexDeepLinks.resolve(it) }.orEmpty()
            if (target.isEmpty()) return@LaunchedEffect
            graph.navigator.currentRoute.first { it == PokedexRoute }
            graph.navigator.openStack(target)
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .semantics { testTagsAsResourceId = true },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = {
                SnackbarHost(snackbar, modifier = Modifier.navigationBarsPadding())
            },
        ) { _ ->
            ArusHost(graph)
        }
    }
}
