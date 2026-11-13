package id.renaldi.pokedex.feature.favorite.ui.di

import id.renaldi.pokedex.feature.favorite.ui.FavoriteController
import io.github.renaldi2108.arus.nav.Navigator
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val favoriteUiModule: Module = module {
    viewModel { (navigator: Navigator) ->
        FavoriteController(
            observeFavoritePokemon = get(),
            toggleFavorite = get(),
            navigator = navigator,
        )
    }
}
