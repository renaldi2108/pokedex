package id.renaldi.pokedex.feature.favorite.ui

import androidx.compose.material3.SnackbarHostState
import id.renaldi.pokedex.feature.favorite.navigation.FavoriteRoute
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.effects.CollectEffects
import io.github.renaldi2108.arus.nav.ArusEntries
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun ArusEntries.favoriteEntries(snackbar: SnackbarHostState) {
    screen<FavoriteRoute> {
        val controller: FavoriteController = koinViewModel { parametersOf(navigator) }
        CollectEffects(controller) { effect ->
            when (effect) {
                is ArusEffect.Snackbar -> snackbar.showSnackbar(effect.message)
                is ArusEffect.Dialog -> snackbar.showSnackbar(effect.message)
            }
        }
        FavoriteScreen(controller)
    }
}
