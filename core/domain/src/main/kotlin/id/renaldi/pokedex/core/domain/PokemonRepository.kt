package id.renaldi.pokedex.core.domain

import id.renaldi.pokedex.core.common.Outcome
import id.renaldi.pokedex.core.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    fun observeAll(): Flow<List<Pokemon>>

    fun observeById(id: Int): Flow<Pokemon?>

    suspend fun byIds(ids: Collection<Int>): List<Pokemon>

    suspend fun sync(): Outcome<Int>
}
