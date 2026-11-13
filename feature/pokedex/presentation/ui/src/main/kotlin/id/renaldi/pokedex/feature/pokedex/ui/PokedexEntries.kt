package id.renaldi.pokedex.feature.pokedex.ui

import androidx.compose.material3.SnackbarHostState
import id.renaldi.pokedex.feature.pokedex.navigation.FilterRoute
import id.renaldi.pokedex.feature.pokedex.navigation.PokedexRoute
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.effects.CollectEffects
import io.github.renaldi2108.arus.nav.ArusEntries
import io.github.renaldi2108.arus.nav.BottomSheetProperties
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun ArusEntries.pokedexEntries(snackbar: SnackbarHostState) {
    screen<PokedexRoute> {
        val controller: PokedexController = koinViewModel { parametersOf(navigator) }
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
        val controller: FilterController =
            koinViewModel { parametersOf(route.toFilter(), navigator) }
        FilterSheet(controller)
    }
}
