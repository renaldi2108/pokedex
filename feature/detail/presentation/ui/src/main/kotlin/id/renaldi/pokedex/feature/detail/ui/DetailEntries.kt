package id.renaldi.pokedex.feature.detail.ui

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.viewmodel.compose.viewModel
import id.renaldi.pokedex.core.domain.PokemonRepository
import id.renaldi.pokedex.feature.detail.domain.ResolveEvolutions
import id.renaldi.pokedex.feature.detail.navigation.DetailRoute
import id.renaldi.pokedex.feature.favorite.domain.ObserveFavoriteIds
import id.renaldi.pokedex.feature.favorite.domain.ToggleFavorite
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.effects.CollectEffects
import io.github.renaldi2108.arus.nav.ArusEntries
import org.koin.compose.koinInject

fun ArusEntries.detailEntries(snackbar: SnackbarHostState) {
    screen<DetailRoute> { route ->
        val repository: PokemonRepository = koinInject()
        val resolveEvolutions: ResolveEvolutions = koinInject()
        val observeFavoriteIds: ObserveFavoriteIds = koinInject()
        val toggleFavorite: ToggleFavorite = koinInject()
        val controller = viewModel(key = "detail-${route.pokemonId}") {
            DetailController(
                pokemonId = route.pokemonId,
                repository = repository,
                resolveEvolutions = resolveEvolutions,
                observeFavoriteIds = observeFavoriteIds,
                toggleFavorite = toggleFavorite,
                navigator = navigator,
            )
        }
        CollectEffects(controller) { effect ->
            when (effect) {
                is ArusEffect.Snackbar -> snackbar.showSnackbar(effect.message)
                is ArusEffect.Dialog -> snackbar.showSnackbar(effect.message)
            }
        }
        DetailScreen(controller)
    }
}
