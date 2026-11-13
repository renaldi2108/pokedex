package id.renaldi.pokedex.core.testing

import id.renaldi.pokedex.core.common.Outcome
import id.renaldi.pokedex.core.domain.PokemonRepository
import id.renaldi.pokedex.core.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakePokemonRepository(
    initial: List<Pokemon> = emptyList(),
) : PokemonRepository {

    private val pokemon = MutableStateFlow(initial)

    var syncResult: Outcome<Int> = Outcome.Success(initial.size)

    var syncCount: Int = 0
        private set

    fun emit(values: List<Pokemon>) {
        pokemon.value = values
    }

    override fun observeAll(): Flow<List<Pokemon>> = pokemon

    override fun observeById(id: Int): Flow<Pokemon?> =
        pokemon.map { values -> values.firstOrNull { it.id == id } }

    override suspend fun byIds(ids: Collection<Int>): List<Pokemon> =
        pokemon.value.filter { it.id in ids }

    override suspend fun sync(): Outcome<Int> {
        syncCount++
        return syncResult
    }
}
