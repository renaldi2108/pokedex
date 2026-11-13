package id.renaldi.pokedex.feature.splash.ui

import androidx.compose.material3.SnackbarHostState
import id.renaldi.pokedex.feature.splash.navigation.SplashRoute
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.effects.CollectEffects
import io.github.renaldi2108.arus.nav.ArusEntries
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun ArusEntries.splashEntries(snackbar: SnackbarHostState) {
    screen<SplashRoute> {
        val controller: SplashController = koinViewModel { parametersOf(navigator) }
        CollectEffects(controller) { effect ->
            when (effect) {
                is ArusEffect.Snackbar -> snackbar.showSnackbar(effect.message)
                is ArusEffect.Dialog -> snackbar.showSnackbar(effect.message)
            }
        }
        SplashScreen(controller)
    }
}
