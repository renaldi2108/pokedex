package id.renaldi.pokedex.core.domain

import id.renaldi.pokedex.core.common.Outcome
import id.renaldi.pokedex.core.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

private class StubPokemonRepository(
    private val all: List<Pokemon>,
    private val syncOutcome: Outcome<Int>,
) : PokemonRepository {
    override fun observeAll(): Flow<List<Pokemon>> = flowOf(all)
    override fun observeById(id: Int): Flow<Pokemon?> = flowOf(all.firstOrNull { it.id == id })
    override suspend fun byIds(ids: Collection<Int>): List<Pokemon> = all.filter { it.id in ids }
    override suspend fun sync(): Outcome<Int> = syncOutcome
}

class UseCaseTest {

    private val pikachu = Pokemon(id = 25, name = "Pikachu")

    @Test
    fun `ObservePokedex forwards the repository stream`() = runTest {
        val observe = ObservePokedex(StubPokemonRepository(listOf(pikachu), Outcome.Success(1)))
        assertEquals(listOf(pikachu), observe().first())
    }

    @Test
    fun `SyncPokedex forwards the repository outcome`() = runTest {
        val sync = SyncPokedex(StubPokemonRepository(emptyList(), Outcome.Success(151)))
        assertEquals(Outcome.Success(151), sync())
    }
}
