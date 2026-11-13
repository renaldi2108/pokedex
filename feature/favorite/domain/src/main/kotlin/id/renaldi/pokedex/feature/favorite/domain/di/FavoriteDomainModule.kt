package id.renaldi.pokedex.feature.favorite.domain.di

import id.renaldi.pokedex.feature.favorite.domain.ObserveFavoriteIds
import id.renaldi.pokedex.feature.favorite.domain.ObserveFavoritePokemon
import id.renaldi.pokedex.feature.favorite.domain.ToggleFavorite
import org.koin.core.module.Module
import org.koin.dsl.module

val favoriteDomainModule: Module = module {
    factory { ObserveFavoriteIds(get()) }
    factory { ToggleFavorite(get()) }
    factory { ObserveFavoritePokemon(get(), get()) }
}
