package id.renaldi.pokedex.feature.detail.domain.di

import id.renaldi.pokedex.feature.detail.domain.ResolveEvolutions
import org.koin.core.module.Module
import org.koin.dsl.module

val detailDomainModule: Module = module {
    factory { ResolveEvolutions(get()) }
}
