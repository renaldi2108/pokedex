package id.renaldi.pokedex.core.domain

import id.renaldi.pokedex.core.model.Pokemon
import kotlinx.coroutines.flow.Flow

class ObservePokedex(private val repository: PokemonRepository) {

    operator fun invoke(): Flow<List<Pokemon>> = repository.observeAll()
}
