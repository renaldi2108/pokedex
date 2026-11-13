package id.renaldi.pokedex.feature.favorite.data.di

import id.renaldi.pokedex.core.domain.FavoriteRepository
import id.renaldi.pokedex.feature.favorite.data.DataStoreFavoriteRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val favoriteDataModule: Module = module {
    single<FavoriteRepository> { DataStoreFavoriteRepository(get()) }
}
