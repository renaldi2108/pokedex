package id.renaldi.pokedex.core.domain

import id.renaldi.pokedex.core.common.Outcome

class SyncPokedex(private val repository: PokemonRepository) {

    suspend operator fun invoke(): Outcome<Int> = repository.sync()
}
