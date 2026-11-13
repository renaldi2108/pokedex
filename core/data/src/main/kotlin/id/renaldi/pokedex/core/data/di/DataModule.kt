package id.renaldi.pokedex.core.data.di

import com.apollographql.apollo.ApolloClient
import id.renaldi.pokedex.core.common.DefaultDispatcherProvider
import id.renaldi.pokedex.core.common.DispatcherProvider
import id.renaldi.pokedex.core.data.ApolloPokemonRemoteSource
import id.renaldi.pokedex.core.data.DefaultPokemonRepository
import id.renaldi.pokedex.core.data.PokemonRemoteSource
import id.renaldi.pokedex.core.domain.ObservePokedex
import id.renaldi.pokedex.core.domain.PokemonRepository
import id.renaldi.pokedex.core.domain.SyncPokedex
import org.koin.core.module.Module
import org.koin.dsl.module

private const val POKEAPI_URL = "https://graphql.pokeapi.co/v1beta2"

val dataModule: Module = module {
    single { ApolloClient.Builder().serverUrl(POKEAPI_URL).build() }
    single<DispatcherProvider> { DefaultDispatcherProvider() }
    single<PokemonRemoteSource> { ApolloPokemonRemoteSource(get()) }
    single<PokemonRepository> { DefaultPokemonRepository(get(), get(), get()) }
    factory { ObservePokedex(get()) }
    factory { SyncPokedex(get()) }
}
