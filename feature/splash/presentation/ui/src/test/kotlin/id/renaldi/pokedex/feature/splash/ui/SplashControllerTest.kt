package id.renaldi.pokedex.feature.splash.ui

import id.renaldi.pokedex.core.common.Outcome
import id.renaldi.pokedex.core.domain.PokemonRepository
import id.renaldi.pokedex.core.domain.SyncPokedex
import id.renaldi.pokedex.core.fixtures.SamplePokemon
import id.renaldi.pokedex.core.model.Pokemon
import id.renaldi.pokedex.core.testing.FakePokemonRepository
import id.renaldi.pokedex.feature.pokedex.navigation.PokedexRoute
import id.renaldi.pokedex.feature.splash.navigation.SplashRoute
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.testing.FakeNavigator
import io.github.renaldi2108.arus.testing.NavOperation
import io.github.renaldi2108.arus.testing.awaitEffects
import io.github.renaldi2108.arus.testing.runControllerTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

private class ThrowingRepository(private val error: Throwable) : PokemonRepository {
    override fun observeAll(): Flow<List<Pokemon>> = flowOf(emptyList())
    override fun observeById(id: Int): Flow<Pokemon?> = flowOf(null)
    override suspend fun byIds(ids: Collection<Int>): List<Pokemon> = emptyList()
    override suspend fun sync(): Outcome<Int> = throw error
}

class SplashControllerTest {

    private fun controllerFor(repository: PokemonRepository, navigator: FakeNavigator) =
        SplashController(SyncPokedex(repository), navigator)

    @Test
    fun `a successful sync restarts the stack on the pokedex`() = runTest {
        runControllerTest {
            val navigator = FakeNavigator(SplashRoute)
            controllerFor(FakePokemonRepository(SamplePokemon.all), navigator)

            assertEquals(listOf(PokedexRoute), navigator.backStack)
            assertEquals(listOf(NavOperation.ResetTo(PokedexRoute)), navigator.operations)
        }
    }

    @Test
    fun `a failed sync surfaces the message and stops the spinner`() = runTest {
        runControllerTest {
            val navigator = FakeNavigator(SplashRoute)
            val repository = FakePokemonRepository().apply {
                syncResult = Outcome.Failure(IllegalStateException("offline"))
            }

            val controller = controllerFor(repository, navigator)

            assertFalse(controller.state.value.syncing)
            assertEquals("offline", controller.state.value.error)
            assertEquals(listOf(SplashRoute), navigator.backStack)
        }
    }

    @Test
    fun `a failed sync emits a snackbar`() = runTest {
        runControllerTest {
            val navigator = FakeNavigator(SplashRoute)
            val repository = FakePokemonRepository().apply {
                syncResult = Outcome.Failure(IllegalStateException("offline"))
            }

            val controller = SplashController(SyncPokedex(repository), navigator)
            val effects = controller.awaitEffects { }

            assertEquals(listOf(ArusEffect.Snackbar("offline")), effects)
        }
    }

    @Test
    fun `a failure without a message falls back to a default`() = runTest {
        runControllerTest {
            val repository = FakePokemonRepository().apply {
                syncResult = Outcome.Failure(IllegalStateException())
            }

            val controller = controllerFor(repository, FakeNavigator(SplashRoute))

            assertEquals("Gagal memuat Pokedex", controller.state.value.error)
        }
    }

    @Test
    fun `a thrown sync is caught and reported`() = runTest {
        runControllerTest {
            val controller = controllerFor(
                ThrowingRepository(IllegalStateException("exploded")),
                FakeNavigator(SplashRoute),
            )

            assertFalse(controller.state.value.syncing)
            assertEquals("exploded", controller.state.value.error)
        }
    }

    @Test
    fun `retrying clears the error and navigates once the sync succeeds`() = runTest {
        runControllerTest {
            val navigator = FakeNavigator(SplashRoute)
            val repository = FakePokemonRepository().apply {
                syncResult = Outcome.Failure(IllegalStateException("offline"))
            }
            val controller = controllerFor(repository, navigator)
            assertEquals("offline", controller.state.value.error)

            repository.syncResult = Outcome.Success(151)
            controller.onRetry()

            assertNull(controller.state.value.error)
            assertTrue(controller.state.value.syncing)
            assertEquals(listOf(PokedexRoute), navigator.backStack)
            assertEquals(2, repository.syncCount)
        }
    }

    @Test
    fun `the initial state is a running sync`() {
        val state = SplashState()
        assertTrue(state.syncing)
        assertNull(state.error)
        assertEquals(state, state.copy())
        assertEquals(state.hashCode(), state.copy().hashCode())
        assertTrue(state.toString().contains("syncing"))
        assertTrue(state.component1())
        assertNull(state.component2())
    }
}
