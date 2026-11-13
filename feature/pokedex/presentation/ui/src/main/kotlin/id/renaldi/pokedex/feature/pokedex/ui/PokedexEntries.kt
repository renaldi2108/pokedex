package id.renaldi.pokedex.feature.pokedex.ui

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import id.renaldi.pokedex.core.domain.ObservePokedex
import id.renaldi.pokedex.feature.favorite.domain.ObserveFavoriteIds
import id.renaldi.pokedex.feature.favorite.domain.ToggleFavorite
import id.renaldi.pokedex.feature.pokedex.domain.FilterPokedex
import id.renaldi.pokedex.feature.pokedex.navigation.FilterRoute
import id.renaldi.pokedex.feature.pokedex.navigation.PokedexRoute
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.effects.CollectEffects
import io.github.renaldi2108.arus.nav.ArusEntries
import io.github.renaldi2108.arus.nav.BottomSheetProperties
import org.koin.compose.koinInject

fun ArusEntries.pokedexEntries(snackbar: SnackbarHostState) {
    screen<PokedexRoute> {
        val observePokedex: ObservePokedex = koinInject()
        val filterPokedex: FilterPokedex = koinInject()
        val observeFavoriteIds: ObserveFavoriteIds = koinInject()
        val toggleFavorite: ToggleFavorite = koinInject()
        val controller = viewModel {
            PokedexController(
                observePokedex = observePokedex,
                filterPokedex = filterPokedex,
                observeFavoriteIds = observeFavoriteIds,
                toggleFavorite = toggleFavorite,
                navigator = navigator,
                handle = createSavedStateHandle(),
            )
        }
        CollectEffects(controller) { effect ->
            when (effect) {
                is ArusEffect.Snackbar -> snackbar.showSnackbar(effect.message)
                is ArusEffect.Dialog -> snackbar.showSnackbar(effect.message)
            }
        }
        PokedexScreen(controller)
    }

    bottomSheetDialog<FilterRoute>(
        BottomSheetProperties(skipPartiallyExpanded = true),
    ) { route ->
        FilterSheet(
            controller = viewModel { FilterController(route.toFilter(), navigator) },
        )
    }
}
