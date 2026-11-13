package id.renaldi.pokedex.feature.favorite.ui

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.viewmodel.compose.viewModel
import id.renaldi.pokedex.feature.favorite.domain.ObserveFavoritePokemon
import id.renaldi.pokedex.feature.favorite.domain.ToggleFavorite
import id.renaldi.pokedex.feature.favorite.navigation.FavoriteRoute
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.effects.CollectEffects
import io.github.renaldi2108.arus.nav.ArusEntries
import org.koin.compose.koinInject

fun ArusEntries.favoriteEntries(snackbar: SnackbarHostState) {
    screen<FavoriteRoute> {
        val observe: ObserveFavoritePokemon = koinInject()
        val toggle: ToggleFavorite = koinInject()
        val controller = viewModel { FavoriteController(observe, toggle, navigator) }
        CollectEffects(controller) { effect ->
            when (effect) {
                is ArusEffect.Snackbar -> snackbar.showSnackbar(effect.message)
                is ArusEffect.Dialog -> snackbar.showSnackbar(effect.message)
            }
        }
        FavoriteScreen(controller)
    }
}
