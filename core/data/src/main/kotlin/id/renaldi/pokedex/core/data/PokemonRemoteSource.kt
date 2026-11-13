package id.renaldi.pokedex.core.data

import id.renaldi.pokedex.core.model.Pokemon

interface PokemonRemoteSource {

    suspend fun fetchAll(): List<Pokemon>
}
