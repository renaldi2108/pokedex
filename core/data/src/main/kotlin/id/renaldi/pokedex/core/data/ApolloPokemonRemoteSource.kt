package id.renaldi.pokedex.core.data

import com.apollographql.apollo.ApolloClient
import id.renaldi.pokedex.core.common.PokedexNetworkException
import id.renaldi.pokedex.core.data.graphql.PokemonOriginalQuery
import id.renaldi.pokedex.core.data.mapper.toPokemonList
import id.renaldi.pokedex.core.model.Pokemon

class ApolloPokemonRemoteSource(private val client: ApolloClient) : PokemonRemoteSource {

    override suspend fun fetchAll(): List<Pokemon> {
        val response = client.query(PokemonOriginalQuery()).execute()
        val data = response.data
        if (response.hasErrors() || data == null) {
            throw PokedexNetworkException("PokeAPI returned errors: ${response.errors}")
        }
        return data.toPokemonList()
    }
}
