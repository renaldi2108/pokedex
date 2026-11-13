package id.renaldi.pokedex.core.data

import id.renaldi.pokedex.core.common.Outcome
import id.renaldi.pokedex.core.database.dao.PokemonDao
import id.renaldi.pokedex.core.database.entity.PokemonEntity
import id.renaldi.pokedex.core.database.mapper.toEntity
import id.renaldi.pokedex.core.fixtures.SamplePokemon
import id.renaldi.pokedex.core.model.Pokemon
import id.renaldi.pokedex.core.testing.TestDispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

private class FakePokemonDao(initial: List<PokemonEntity> = emptyList()) : PokemonDao {

    private val rows = MutableStateFlow(initial)

    var deleteAllCount: Int = 0
        private set

    override fun observeAll(): Flow<List<PokemonEntity>> = rows

    override fun observeById(id: Int): Flow<PokemonEntity?> =
        rows.map { all -> all.firstOrNull { it.id == id } }

    override suspend fun findByIds(ids: Collection<Int>): List<PokemonEntity> =
        rows.value.filter { it.id in ids }

    override suspend fun count(): Int = rows.value.size

    override suspend fun upsertAll(pokemon: List<PokemonEntity>) {
        rows.value = rows.value.filterNot { row -> pokemon.any { it.id == row.id } } + pokemon
    }

    override suspend fun deleteAll() {
        deleteAllCount++
        rows.value = emptyList()
    }
}

private class FakeRemoteSource(
    private val result: List<Pokemon> = emptyList(),
    private val error: Throwable? = null,
) : PokemonRemoteSource {

    var calls: Int = 0
        private set

    override suspend fun fetchAll(): List<Pokemon> {
        calls++
        error?.let { throw it }
        return result
    }
}

class DefaultPokemonRepositoryTest {

    private val dispatchers = TestDispatcherProvider()

    @Test
    fun `observeAll maps stored rows back into models`() = runTest {
        val dao = FakePokemonDao(listOf(SamplePokemon.pikachu.toEntity()))
        val repository = DefaultPokemonRepository(dao, FakeRemoteSource(), dispatchers)

        assertEquals(listOf(SamplePokemon.pikachu), repository.observeAll().first())
    }

    @Test
    fun `observeById maps the matching row and yields null when absent`() = runTest {
        val dao = FakePokemonDao(listOf(SamplePokemon.pikachu.toEntity()))
        val repository = DefaultPokemonRepository(dao, FakeRemoteSource(), dispatchers)

        assertEquals(SamplePokemon.pikachu, repository.observeById(25).first())
        assertNull(repository.observeById(999).first())
    }

    @Test
    fun `byIds returns only the requested models`() = runTest {
        val dao = FakePokemonDao(
            listOf(SamplePokemon.pikachu.toEntity(), SamplePokemon.raichu.toEntity()),
        )
        val repository = DefaultPokemonRepository(dao, FakeRemoteSource(), dispatchers)

        assertEquals(listOf(SamplePokemon.raichu), repository.byIds(listOf(26)))
    }

    @Test
    fun `sync reports the cached count without hitting the network`() = runTest {
        val dao = FakePokemonDao(listOf(SamplePokemon.pikachu.toEntity()))
        val remote = FakeRemoteSource()
        val repository = DefaultPokemonRepository(dao, remote, dispatchers)

        assertEquals(Outcome.Success(1), repository.sync())
        assertEquals(0, remote.calls)
    }

    @Test
    fun `sync fetches and stores when the cache is empty`() = runTest {
        val dao = FakePokemonDao()
        val remote = FakeRemoteSource(listOf(SamplePokemon.pikachu, SamplePokemon.raichu))
        val repository = DefaultPokemonRepository(dao, remote, dispatchers)

        assertEquals(Outcome.Success(2), repository.sync())
        assertEquals(1, remote.calls)
        assertEquals(
            listOf(SamplePokemon.pikachu, SamplePokemon.raichu),
            repository.observeAll().first(),
        )
    }

    @Test
    fun `sync reports a failure when the network throws`() = runTest {
        val boom = IllegalStateException("offline")
        val repository = DefaultPokemonRepository(
            FakePokemonDao(),
            FakeRemoteSource(error = boom),
            dispatchers,
        )

        val outcome = repository.sync()
        assertTrue(outcome is Outcome.Failure)
        assertSame(boom, (outcome as Outcome.Failure).error)
    }
}
