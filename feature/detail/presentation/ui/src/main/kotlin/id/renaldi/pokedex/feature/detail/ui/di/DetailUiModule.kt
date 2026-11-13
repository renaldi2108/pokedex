package id.renaldi.pokedex.feature.detail.ui.di

import id.renaldi.pokedex.feature.detail.ui.DetailController
import io.github.renaldi2108.arus.nav.Navigator
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val detailUiModule: Module = module {
    viewModel { (pokemonId: Int, navigator: Navigator) ->
        DetailController(
            pokemonId = pokemonId,
            repository = get(),
            resolveEvolutions = get(),
            observeFavoriteIds = get(),
            toggleFavorite = get(),
            navigator = navigator,
        )
    }
}
