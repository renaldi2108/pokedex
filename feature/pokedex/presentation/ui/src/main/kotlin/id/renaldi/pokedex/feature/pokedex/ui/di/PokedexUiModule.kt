package id.renaldi.pokedex.feature.pokedex.ui.di

import id.renaldi.pokedex.core.model.PokedexFilter
import id.renaldi.pokedex.feature.pokedex.ui.FilterController
import id.renaldi.pokedex.feature.pokedex.ui.PokedexController
import io.github.renaldi2108.arus.nav.Navigator
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val pokedexUiModule: Module = module {
    viewModel { (navigator: Navigator) ->
        PokedexController(
            observePokedex = get(),
            filterPokedex = get(),
            observeFavoriteIds = get(),
            toggleFavorite = get(),
            navigator = navigator,
            handle = get(),
        )
    }
    viewModel { (initial: PokedexFilter, navigator: Navigator) ->
        FilterController(initial = initial, navigator = navigator)
    }
}
