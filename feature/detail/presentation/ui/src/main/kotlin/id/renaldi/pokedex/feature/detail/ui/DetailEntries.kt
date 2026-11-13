package id.renaldi.pokedex.feature.detail.ui

import androidx.compose.material3.SnackbarHostState
import id.renaldi.pokedex.feature.detail.navigation.DetailRoute
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.effects.CollectEffects
import io.github.renaldi2108.arus.nav.ArusEntries
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun ArusEntries.detailEntries(snackbar: SnackbarHostState) {
    screen<DetailRoute> { route ->
        val controller: DetailController =
            koinViewModel { parametersOf(route.pokemonId, navigator) }
        CollectEffects(controller) { effect ->
            when (effect) {
                is ArusEffect.Snackbar -> snackbar.showSnackbar(effect.message)
                is ArusEffect.Dialog -> snackbar.showSnackbar(effect.message)
            }
        }
        DetailScreen(controller)
    }
}
