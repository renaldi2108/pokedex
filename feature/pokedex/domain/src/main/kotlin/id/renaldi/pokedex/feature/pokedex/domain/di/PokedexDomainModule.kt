package id.renaldi.pokedex.feature.pokedex.domain.di

import id.renaldi.pokedex.feature.pokedex.domain.FilterPokedex
import org.koin.core.module.Module
import org.koin.dsl.module

val pokedexDomainModule: Module = module {
    factory { FilterPokedex() }
}
