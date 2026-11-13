package id.renaldi.pokedex.feature.pokedex.ui

import androidx.lifecycle.SavedStateHandle
import id.renaldi.pokedex.core.domain.ObservePokedex
import id.renaldi.pokedex.core.fixtures.SamplePokemon
import id.renaldi.pokedex.core.model.PokedexFilter
import id.renaldi.pokedex.core.model.PokedexSort
import id.renaldi.pokedex.core.model.PokemonType
import id.renaldi.pokedex.core.testing.FakeFavoriteRepository
import id.renaldi.pokedex.core.testing.FakePokemonRepository
import id.renaldi.pokedex.feature.detail.navigation.DetailRoute
import id.renaldi.pokedex.feature.favorite.domain.ObserveFavoriteIds
import id.renaldi.pokedex.feature.favorite.domain.ToggleFavorite
import id.renaldi.pokedex.feature.favorite.navigation.FavoriteRoute
import id.renaldi.pokedex.feature.pokedex.domain.FilterPokedex
import id.renaldi.pokedex.feature.pokedex.navigation.FilterRoute
import id.renaldi.pokedex.feature.pokedex.navigation.PokedexRoute
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.testing.FakeNavigator
import io.github.renaldi2108.arus.testing.NavOperation
import io.github.renaldi2108.arus.testing.awaitEffects
import io.github.renaldi2108.arus.testing.runControllerTest
import id.renaldi.pokedex.core.common.Outcome
import id.renaldi.pokedex.core.domain.FavoriteRepository
import id.renaldi.pokedex.core.domain.PokemonRepository
import id.renaldi.pokedex.core.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.yield
import io.github.renaldi2108.arus.nav.NavResult
import io.github.renaldi2108.arus.nav.Navigator
import io.github.renaldi2108.arus.nav.ResultRoute
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private class FiniteFavoriteRepository(private val ids: Set<Int>) : FavoriteRepository {
    override fun observeIds(): Flow<Set<Int>> = flow {
        emit(ids)
        yield()
    }

    override suspend fun toggle(id: Int) = Unit
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

private class SuspendingNavigator(
    private val delegate: FakeNavigator,
    private val result: Any?,
) : Navigator by delegate {
    override suspend fun <R : Any> pushForResult(route: ResultRoute<R>): NavResult<R> {
        delegate.push(route)
        yield()
        delegate.pop()
        @Suppress("UNCHECKED_CAST")
        return result?.let { NavResult.Completed(it as R) } ?: NavResult.Cancelled
    }
}

private class FailingPokemonRepository(private val error: Throwable) : PokemonRepository {
    override fun observeAll(): Flow<List<Pokemon>> = flow { throw error }
    override fun observeById(id: Int): Flow<Pokemon?> = flowOf(null)
    override suspend fun byIds(ids: Collection<Int>): List<Pokemon> = emptyList()
    override suspend fun sync(): Outcome<Int> = Outcome.Success(0)
}

class PokedexControllerTest {

    private lateinit var navigator: FakeNavigator
    private lateinit var pokemon: FakePokemonRepository
    private lateinit var favorites: FakeFavoriteRepository

    private fun controller(handle: SavedStateHandle = SavedStateHandle()): PokedexController {
        navigator = FakeNavigator(PokedexRoute)
        pokemon = FakePokemonRepository(SamplePokemon.all)
        favorites = FakeFavoriteRepository()
        return PokedexController(
            observePokedex = ObservePokedex(pokemon),
            filterPokedex = FilterPokedex(),
            observeFavoriteIds = ObserveFavoriteIds(favorites),
            toggleFavorite = ToggleFavorite(favorites),
            navigator = navigator,
            handle = handle,
        )
    }

    @Test
    fun `the pokedex stream fills the list and clears the loading flag`() = runTest {
        runControllerTest {
            val controller = controller()

            assertFalse(controller.state.value.loading)
            assertEquals(SamplePokemon.all.map { it.id }, controller.state.value.pokemon.map { it.id })
            assertFalse(controller.state.value.isEmpty)
        }
    }

    @Test
    fun `an empty pokedex reports the empty state`() = runTest {
        runControllerTest {
            val controller = controller()
            pokemon.emit(emptyList())

            assertTrue(controller.state.value.isEmpty)
        }
    }

    @Test
    fun `favourite ids are mirrored into the state`() = runTest {
        runControllerTest {
            val controller = controller()
            favorites.emit(setOf(25))

            assertEquals(setOf(25), controller.state.value.favorites)
        }
    }

    @Test
    fun `typing filters the list and updates the draft`() = runTest {
        runControllerTest {
            val controller = controller()

            controller.onQueryChanged("pika")

            assertEquals("pika", controller.draftQuery.value)
            assertEquals("pika", controller.state.value.filter.query)
            assertEquals(listOf(25), controller.state.value.pokemon.map { it.id })
        }
    }

    @Test
    fun `toggling a type narrows then widens the list`() = runTest {
        runControllerTest {
            val controller = controller()

            controller.onTypeToggled(PokemonType.Fire)
            assertEquals(listOf(4), controller.state.value.pokemon.map { it.id })

            controller.onTypeToggled(PokemonType.Fire)
            assertEquals(SamplePokemon.all.size, controller.state.value.pokemon.size)
        }
    }

    @Test
    fun `changing the sort reorders the list`() = runTest {
        runControllerTest {
            val controller = controller()

            controller.onSortChanged(PokedexSort.Strongest)

            assertEquals(PokedexSort.Strongest, controller.state.value.filter.sort)
            assertEquals(26, controller.state.value.pokemon.first().id)
        }
    }

    @Test
    fun `the filter sheet is opened for a result and a completed result is applied`() = runTest {
        runControllerTest {
            val controller = controller()
            val chosen = PokedexFilter(
                query = "rai",
                types = setOf(PokemonType.Electric),
                sort = PokedexSort.Name,
            )
            navigator.nextResult = chosen

            controller.onFilterClicked()

            assertEquals(chosen, controller.state.value.filter)
            assertEquals("rai", controller.draftQuery.value)
            assertEquals(listOf(26), controller.state.value.pokemon.map { it.id })
            assertTrue(
                navigator.operations.any {
                    it is NavOperation.PushForResult && it.route is FilterRoute
                },
            )
        }
    }

    @Test
    fun `a cancelled filter sheet leaves the filter untouched`() = runTest {
        runControllerTest {
            val controller = controller()
            controller.onQueryChanged("pika")
            navigator.nextResult = null

            controller.onFilterClicked()

            assertEquals("pika", controller.state.value.filter.query)
            assertEquals(listOf(25), controller.state.value.pokemon.map { it.id })
        }
    }

    @Test
    fun `tapping a pokemon pushes its detail route`() = runTest {
        runControllerTest {
            val controller = controller()

            controller.onPokemonClicked(SamplePokemon.pikachu)

            assertEquals(DetailRoute(25), navigator.backStack.last())
        }
    }

    @Test
    fun `tapping favorites pushes the favorites route`() = runTest {
        runControllerTest {
            val controller = controller()

            controller.onFavoritesClicked()

            assertEquals(FavoriteRoute, navigator.backStack.last())
        }
    }

    @Test
    fun `tapping the heart toggles the favourite through the repository`() = runTest {
        runControllerTest {
            val controller = controller()

            controller.onFavoriteToggled(SamplePokemon.pikachu)

            assertEquals(listOf(25), favorites.toggled)
            assertEquals(setOf(25), controller.state.value.favorites)
        }
    }

    @Test
    fun `the query sort and types survive a process death`() = runTest {
        runControllerTest {
            val handle = SavedStateHandle()
            val first = controller(handle)
            first.onQueryChanged("pika")
            first.onSortChanged(PokedexSort.Name)
            first.onTypeToggled(PokemonType.Electric)

            val restored = controller(handle)

            assertEquals("pika", restored.state.value.filter.query)
            assertEquals("pika", restored.draftQuery.value)
            assertEquals(PokedexSort.Name, restored.state.value.filter.sort)
            assertEquals(setOf(PokemonType.Electric), restored.state.value.filter.types)
        }
    }

    @Test
    fun `an unknown stored sort falls back to Number`() = runTest {
        runControllerTest {
            val handle = SavedStateHandle(mapOf("pokedex.sort" to "Nonsense"))

            assertEquals(PokedexSort.Number, controller(handle).state.value.filter.sort)
        }
    }

    private fun failingController(error: Throwable): PokedexController {
        val favoriteRepository = FakeFavoriteRepository()
        return PokedexController(
            observePokedex = ObservePokedex(FailingPokemonRepository(error)),
            filterPokedex = FilterPokedex(),
            observeFavoriteIds = ObserveFavoriteIds(favoriteRepository),
            toggleFavorite = ToggleFavorite(favoriteRepository),
            navigator = FakeNavigator(PokedexRoute),
            handle = SavedStateHandle(),
        )
    }

    @Test
    fun `a failing pokedex stream is reported as a snackbar`() = runTest {
        runControllerTest {
            val effects = failingController(IllegalStateException("gagal"))
                .awaitEffects { }

            assertEquals(listOf(ArusEffect.Snackbar("gagal")), effects)
        }
    }

    @Test
    fun `an error without a message falls back to a default`() = runTest {
        runControllerTest {
            val effects = failingController(IllegalStateException()).awaitEffects { }

            assertEquals(listOf(ArusEffect.Snackbar("Terjadi kesalahan")), effects)
        }
    }

    @Test
    fun `a stream that completes still leaves the controller settled`() = runTest {
        runControllerTest {
            val favoriteRepository = FiniteFavoriteRepository(setOf(25))
            val controller = PokedexController(
                observePokedex = ObservePokedex(FinitePokemonRepository(SamplePokemon.all)),
                filterPokedex = FilterPokedex(),
                observeFavoriteIds = ObserveFavoriteIds(favoriteRepository),
                toggleFavorite = ToggleFavorite(favoriteRepository),
                navigator = FakeNavigator(PokedexRoute),
                handle = SavedStateHandle(),
            )

            assertFalse(controller.state.value.loading)
            assertEquals(SamplePokemon.all.size, controller.state.value.pokemon.size)
            assertEquals(setOf(25), controller.state.value.favorites)
        }
    }

    @Test
    fun `a filter sheet that genuinely suspends still applies its result`() = runTest {
        runControllerTest {
            val favoriteRepository = FakeFavoriteRepository()
            val chosen = PokedexFilter(sort = PokedexSort.Name)
            val controller = PokedexController(
                observePokedex = ObservePokedex(FakePokemonRepository(SamplePokemon.all)),
                filterPokedex = FilterPokedex(),
                observeFavoriteIds = ObserveFavoriteIds(favoriteRepository),
                toggleFavorite = ToggleFavorite(favoriteRepository),
                navigator = SuspendingNavigator(FakeNavigator(PokedexRoute), chosen),
                handle = SavedStateHandle(),
            )

            controller.onFilterClicked()

            assertEquals(chosen, controller.state.value.filter)
        }
    }

    @Test
    fun `a filter sheet that suspends and is cancelled changes nothing`() = runTest {
        runControllerTest {
            val favoriteRepository = FakeFavoriteRepository()
            val controller = PokedexController(
                observePokedex = ObservePokedex(FakePokemonRepository(SamplePokemon.all)),
                filterPokedex = FilterPokedex(),
                observeFavoriteIds = ObserveFavoriteIds(favoriteRepository),
                toggleFavorite = ToggleFavorite(favoriteRepository),
                navigator = SuspendingNavigator(FakeNavigator(PokedexRoute), null),
                handle = SavedStateHandle(),
            )

            controller.onFilterClicked()

            assertEquals(PokedexFilter(), controller.state.value.filter)
        }
    }

    @Test
    fun `the initial state is loading and empty`() {
        val state = PokedexState()
        assertTrue(state.loading)
        assertFalse(state.isEmpty)
        assertEquals(state, state.copy())
        assertEquals(state.hashCode(), state.copy().hashCode())
        assertTrue(state.toString().contains("loading"))
        assertEquals(emptyList<Any>(), state.component1())
        assertEquals(PokedexFilter(), state.component2())
        assertEquals(emptySet<Int>(), state.component3())
        assertTrue(state.component4())
    }
}
