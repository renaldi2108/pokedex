package id.renaldi.pokedex.feature.favorite.ui

import id.renaldi.pokedex.core.common.Outcome
import id.renaldi.pokedex.core.domain.FavoriteRepository
import id.renaldi.pokedex.core.domain.PokemonRepository
import id.renaldi.pokedex.core.fixtures.SamplePokemon
import id.renaldi.pokedex.core.model.Pokemon
import id.renaldi.pokedex.core.testing.FakeFavoriteRepository
import id.renaldi.pokedex.core.testing.FakePokemonRepository
import id.renaldi.pokedex.feature.detail.navigation.DetailRoute
import id.renaldi.pokedex.feature.favorite.domain.ObserveFavoritePokemon
import id.renaldi.pokedex.feature.favorite.domain.ToggleFavorite
import id.renaldi.pokedex.feature.favorite.navigation.FavoriteRoute
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.testing.FakeNavigator
import io.github.renaldi2108.arus.testing.awaitEffects
import io.github.renaldi2108.arus.testing.runControllerTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private class FailingPokemonRepository(private val error: Throwable) : PokemonRepository {
    override fun observeAll(): Flow<List<Pokemon>> = flow { throw error }
    override fun observeById(id: Int): Flow<Pokemon?> = flowOf(null)
    override suspend fun byIds(ids: Collection<Int>): List<Pokemon> = emptyList()
    override suspend fun sync(): Outcome<Int> = Outcome.Success(0)
}

private class FinitePokemonRepository(private val all: List<Pokemon>) : PokemonRepository {
    override fun observeAll(): Flow<List<Pokemon>> = flow {
        emit(all)
        yield()
    }

    override fun observeById(id: Int): Flow<Pokemon?> = flowOf(null)
    override suspend fun byIds(ids: Collection<Int>): List<Pokemon> = emptyList()
    override suspend fun sync(): Outcome<Int> = Outcome.Success(all.size)
}

private class FiniteFavoriteRepository(private val ids: Set<Int>) : FavoriteRepository {
    override fun observeIds(): Flow<Set<Int>> = flow {
        emit(ids)
        yield()
    }

    override suspend fun toggle(id: Int) = Unit
}

class FavoriteControllerTest {

    private lateinit var navigator: FakeNavigator
    private lateinit var favorites: FakeFavoriteRepository

    private fun controller(
        pokedex: PokemonRepository = FakePokemonRepository(SamplePokemon.all),
        favorited: Set<Int> = setOf(25),
    ): FavoriteController {
        navigator = FakeNavigator(FavoriteRoute)
        favorites = FakeFavoriteRepository(favorited)
        return FavoriteController(
            observeFavoritePokemon = ObserveFavoritePokemon(pokedex, favorites),
            toggleFavorite = ToggleFavorite(favorites),
            navigator = navigator,
        )
    }

    @Test
    fun `favourited pokemon land in the state in national order`() = runTest {
        runControllerTest {
            val controller = controller(favorited = setOf(26, 1))

            assertFalse(controller.state.value.loading)
            assertEquals(listOf(1, 26), controller.state.value.pokemon.map { it.id })
            assertFalse(controller.state.value.isEmpty)
        }
    }

    @Test
    fun `no favourites reports the empty state`() = runTest {
        runControllerTest {
            val controller = controller(favorited = emptySet())

            assertTrue(controller.state.value.isEmpty)
            assertEquals(emptyList<Pokemon>(), controller.state.value.pokemon)
        }
    }

    @Test
    fun `tapping a card opens its detail screen`() = runTest {
        runControllerTest {
            val controller = controller()

            controller.onPokemonClicked(SamplePokemon.pikachu)

            assertEquals(DetailRoute(25), navigator.backStack.last())
        }
    }

    @Test
    fun `un-hearting a card removes it from the list`() = runTest {
        runControllerTest {
            val controller = controller()
            assertEquals(listOf(25), controller.state.value.pokemon.map { it.id })

            controller.onFavoriteToggled(SamplePokemon.pikachu)

            assertEquals(listOf(25), favorites.toggled)
            assertEquals(emptyList<Pokemon>(), controller.state.value.pokemon)
            assertTrue(controller.state.value.isEmpty)
        }
    }

    @Test
    fun `going back pops the entry`() = runTest {
        runControllerTest {
            val controller = controller()
            navigator.push(DetailRoute(25))

            controller.onBackClicked()

            assertEquals(listOf(FavoriteRoute), navigator.backStack)
        }
    }

    @Test
    fun `a failing stream is reported as a snackbar`() = runTest {
        runControllerTest {
            val navigator = FakeNavigator(FavoriteRoute)
            val favoriteRepository = FakeFavoriteRepository(setOf(25))
            val controller = FavoriteController(
                observeFavoritePokemon = ObserveFavoritePokemon(
                    FailingPokemonRepository(IllegalStateException("rusak")),
                    favoriteRepository,
                ),
                toggleFavorite = ToggleFavorite(favoriteRepository),
                navigator = navigator,
            )

            assertEquals(
                listOf(ArusEffect.Snackbar("rusak")),
                controller.awaitEffects { },
            )
        }
    }

    @Test
    fun `an error without a message falls back to a default`() = runTest {
        runControllerTest {
            val favoriteRepository = FakeFavoriteRepository(setOf(25))
            val controller = FavoriteController(
                observeFavoritePokemon = ObserveFavoritePokemon(
                    FailingPokemonRepository(IllegalStateException()),
                    favoriteRepository,
                ),
                toggleFavorite = ToggleFavorite(favoriteRepository),
                navigator = FakeNavigator(FavoriteRoute),
            )

            assertEquals(
                listOf(ArusEffect.Snackbar("Gagal memuat favorit")),
                controller.awaitEffects { },
            )
        }
    }

    @Test
    fun `a stream that completes still settles the state`() = runTest {
        runControllerTest {
            val controller = FavoriteController(
                observeFavoritePokemon = ObserveFavoritePokemon(
                    FinitePokemonRepository(SamplePokemon.all),
                    FiniteFavoriteRepository(setOf(25)),
                ),
                toggleFavorite = ToggleFavorite(FakeFavoriteRepository()),
                navigator = FakeNavigator(FavoriteRoute),
            )

            assertFalse(controller.state.value.loading)
            assertEquals(listOf(25), controller.state.value.pokemon.map { it.id })
        }
    }

    @Test
    fun `the initial state is loading`() {
        val state = FavoriteState()
        assertTrue(state.loading)
        assertFalse(state.isEmpty)
        assertEquals(state, state.copy())
        assertEquals(state.hashCode(), state.copy().hashCode())
        assertNotEquals(state, state.copy(loading = false))
        assertTrue(state.toString().contains("loading"))
        assertEquals(emptyList<Pokemon>(), state.component1())
        assertTrue(state.component2())
    }
}
