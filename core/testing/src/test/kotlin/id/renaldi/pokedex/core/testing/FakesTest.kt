package id.renaldi.pokedex.core.testing

import id.renaldi.pokedex.core.common.Outcome
import id.renaldi.pokedex.core.fixtures.SamplePokemon
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test

class FakesTest {

    @Test
    fun `the pokemon fake replays whatever it was seeded with`() = runTest {
        val repository = FakePokemonRepository(listOf(SamplePokemon.pikachu))

        assertEquals(listOf(SamplePokemon.pikachu), repository.observeAll().first())
        assertEquals(SamplePokemon.pikachu, repository.observeById(25).first())
        assertNull(repository.observeById(999).first())
        assertEquals(listOf(SamplePokemon.pikachu), repository.byIds(listOf(25, 999)))
    }

    @Test
    fun `the pokemon fake re-emits on demand`() = runTest {
        val repository = FakePokemonRepository()
        repository.emit(listOf(SamplePokemon.raichu))

        assertEquals(listOf(SamplePokemon.raichu), repository.observeAll().first())
    }

    @Test
    fun `the pokemon fake counts syncs and returns the scripted outcome`() = runTest {
        val repository = FakePokemonRepository(listOf(SamplePokemon.pikachu))
        assertEquals(Outcome.Success(1), repository.sync())
        assertEquals(1, repository.syncCount)

        val boom = IllegalStateException("offline")
        repository.syncResult = Outcome.Failure(boom)
        assertSame(boom, (repository.sync() as Outcome.Failure).error)
        assertEquals(2, repository.syncCount)
    }

    @Test
    fun `the favorite fake toggles and records every call`() = runTest {
        val repository = FakeFavoriteRepository(setOf(1))

        assertEquals(setOf(1), repository.observeIds().first())
        assertEquals(setOf(1), repository.current)

        repository.toggle(25)
        assertEquals(setOf(1, 25), repository.current)

        repository.toggle(1)
        assertEquals(setOf(25), repository.current)
        assertEquals(listOf(25, 1), repository.toggled)
    }

    @Test
    fun `the favorite fake can be driven directly`() = runTest {
        val repository = FakeFavoriteRepository()
        repository.emit(setOf(7))

        assertEquals(setOf(7), repository.observeIds().first())
    }

    @Test
    fun `the test dispatcher provider hands back the dispatcher it was given`() {
        val dispatcher = UnconfinedTestDispatcher()
        val provider = TestDispatcherProvider(dispatcher)

        assertSame(dispatcher, provider.io)
        assertSame(dispatcher, provider.default)

        val defaulted = TestDispatcherProvider()
        assertSame(defaulted.io, defaulted.default)
    }
}
