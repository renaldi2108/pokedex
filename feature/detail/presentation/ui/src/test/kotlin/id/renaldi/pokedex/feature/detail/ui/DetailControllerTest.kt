package id.renaldi.pokedex.feature.detail.ui

import id.renaldi.pokedex.core.common.Outcome
import id.renaldi.pokedex.core.domain.FavoriteRepository
import id.renaldi.pokedex.core.domain.PokemonRepository
import id.renaldi.pokedex.core.fixtures.SamplePokemon
import id.renaldi.pokedex.core.model.EvolutionStep
import id.renaldi.pokedex.core.model.EvolutionTrigger
import id.renaldi.pokedex.core.model.Pokemon
import id.renaldi.pokedex.core.testing.FakeFavoriteRepository
import id.renaldi.pokedex.core.testing.FakePokemonRepository
import id.renaldi.pokedex.feature.detail.domain.ResolveEvolutions
import id.renaldi.pokedex.feature.detail.navigation.DetailRoute
import id.renaldi.pokedex.feature.favorite.domain.ObserveFavoriteIds
import id.renaldi.pokedex.feature.favorite.domain.ToggleFavorite
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.testing.FakeNavigator
import io.github.renaldi2108.arus.testing.NavOperation
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

private class FailingPokemonRepository(private val error: Throwable) : PokemonRepository {
    override fun observeAll(): Flow<List<Pokemon>> = flowOf(emptyList())
    override fun observeById(id: Int): Flow<Pokemon?> = flow { throw error }
    override suspend fun byIds(ids: Collection<Int>): List<Pokemon> = emptyList()
    override suspend fun sync(): Outcome<Int> = Outcome.Success(0)
}

private class FinitePokemonRepository(private val all: List<Pokemon>) : PokemonRepository {
    override fun observeAll(): Flow<List<Pokemon>> = flowOf(all)

    override fun observeById(id: Int): Flow<Pokemon?> = flow {
        emit(all.firstOrNull { it.id == id })
        yield()
    }

    override suspend fun byIds(ids: Collection<Int>): List<Pokemon> = all.filter { it.id in ids }
    override suspend fun sync(): Outcome<Int> = Outcome.Success(all.size)
}

private class FiniteFavoriteRepository(private val ids: Set<Int>) : FavoriteRepository {
    override fun observeIds(): Flow<Set<Int>> = flow {
        emit(ids)
        yield()
    }

    override suspend fun toggle(id: Int) = Unit
}

class DetailControllerTest {

    private lateinit var navigator: FakeNavigator
    private lateinit var favorites: FakeFavoriteRepository

    private fun controller(
        pokemonId: Int = 25,
        repository: PokemonRepository = FakePokemonRepository(SamplePokemon.all),
    ): DetailController {
        navigator = FakeNavigator(DetailRoute(pokemonId))
        favorites = FakeFavoriteRepository()
        return DetailController(
            pokemonId = pokemonId,
            repository = repository,
            resolveEvolutions = ResolveEvolutions(repository),
            observeFavoriteIds = ObserveFavoriteIds(favorites),
            toggleFavorite = ToggleFavorite(favorites),
            navigator = navigator,
        )
    }

    @Test
    fun `the observed pokemon and its evolution chain land in the state`() = runTest {
        runControllerTest {
            val controller = controller()

            assertEquals(SamplePokemon.pikachu, controller.state.value.pokemon)
            assertFalse(controller.state.value.loading)
            assertEquals(listOf(25, 26), controller.state.value.evolutions.map { it.pokemon.id })
        }
    }

    @Test
    fun `an unknown id leaves the screen loading with no evolutions`() = runTest {
        runControllerTest {
            val controller = controller(pokemonId = 999)

            assertNull(controller.state.value.pokemon)
            assertTrue(controller.state.value.loading)
            assertEquals(emptyList<EvolutionStep>(), controller.state.value.evolutions)
        }
    }

    @Test
    fun `the favourite flag tracks the stored ids`() = runTest {
        runControllerTest {
            val controller = controller()
            assertFalse(controller.state.value.isFavorite)

            favorites.emit(setOf(25))
            assertTrue(controller.state.value.isFavorite)

            favorites.emit(setOf(26))
            assertFalse(controller.state.value.isFavorite)
        }
    }

    @Test
    fun `selecting a tab swaps the visible section`() = runTest {
        runControllerTest {
            val controller = controller()
            assertEquals(DetailTab.About, controller.state.value.tab)

            controller.onTabSelected(DetailTab.BaseStats)
            assertEquals(DetailTab.BaseStats, controller.state.value.tab)

            controller.onTabSelected(DetailTab.Evolution)
            assertEquals(DetailTab.Evolution, controller.state.value.tab)
        }
    }

    @Test
    fun `tapping the heart toggles this pokemon`() = runTest {
        runControllerTest {
            val controller = controller()

            controller.onFavoriteToggled()

            assertEquals(listOf(25), favorites.toggled)
            assertTrue(controller.state.value.isFavorite)
        }
    }

    @Test
    fun `tapping another evolution opens its detail screen`() = runTest {
        runControllerTest {
            val controller = controller()
            val raichu = EvolutionStep(SamplePokemon.raichu, EvolutionTrigger.UseItem, -1)

            controller.onEvolutionClicked(raichu)

            assertEquals(DetailRoute(26), navigator.backStack.last())
        }
    }

    @Test
    fun `tapping the pokemon already on screen navigates nowhere`() = runTest {
        runControllerTest {
            val controller = controller()
            val self = EvolutionStep(SamplePokemon.pikachu, EvolutionTrigger.LevelUp, -1)

            controller.onEvolutionClicked(self)

            assertEquals(listOf(DetailRoute(25)), navigator.backStack)
            assertEquals(emptyList<NavOperation>(), navigator.operations)
        }
    }

    @Test
    fun `going back pops the entry`() = runTest {
        runControllerTest {
            navigator = FakeNavigator(DetailRoute(25))
            favorites = FakeFavoriteRepository()
            val repository = FakePokemonRepository(SamplePokemon.all)
            val controller = DetailController(
                pokemonId = 25,
                repository = repository,
                resolveEvolutions = ResolveEvolutions(repository),
                observeFavoriteIds = ObserveFavoriteIds(favorites),
                toggleFavorite = ToggleFavorite(favorites),
                navigator = navigator,
            )
            navigator.push(DetailRoute(26))

            controller.onBackClicked()

            assertEquals(listOf(DetailRoute(25)), navigator.backStack)
        }
    }

    @Test
    fun `a failing stream is reported as a snackbar`() = runTest {
        runControllerTest {
            val effects = controller(
                repository = FailingPokemonRepository(IllegalStateException("rusak")),
            ).awaitEffects { }

            assertEquals(listOf(ArusEffect.Snackbar("rusak")), effects)
        }
    }

    @Test
    fun `an error without a message falls back to a default`() = runTest {
        runControllerTest {
            val effects = controller(
                repository = FailingPokemonRepository(IllegalStateException()),
            ).awaitEffects { }

            assertEquals(listOf(ArusEffect.Snackbar("Gagal memuat detail")), effects)
        }
    }

    @Test
    fun `streams that complete still settle the state`() = runTest {
        runControllerTest {
            val repository = FinitePokemonRepository(SamplePokemon.all)
            val controller = DetailController(
                pokemonId = 25,
                repository = repository,
                resolveEvolutions = ResolveEvolutions(repository),
                observeFavoriteIds = ObserveFavoriteIds(FiniteFavoriteRepository(setOf(25))),
                toggleFavorite = ToggleFavorite(FakeFavoriteRepository()),
                navigator = FakeNavigator(DetailRoute(25)),
            )

            assertEquals(SamplePokemon.pikachu, controller.state.value.pokemon)
            assertTrue(controller.state.value.isFavorite)
        }
    }

    @Test
    fun `tabs carry a label for the tab row`() {
        assertEquals(listOf("About", "Base Stats", "Evolution"), DetailTab.entries.map { it.label })
    }

    @Test
    fun `the initial state is loading`() {
        val state = DetailState()
        assertTrue(state.loading)
        assertNull(state.pokemon)
        assertEquals(emptyList<EvolutionStep>(), state.evolutions)
        assertFalse(state.isFavorite)
        assertEquals(DetailTab.About, state.tab)
        assertEquals(state, state.copy())
        assertEquals(state.hashCode(), state.copy().hashCode())
        assertNotEquals(state, state.copy(isFavorite = true))
        assertTrue(state.toString().contains("tab"))
        assertNull(state.component1())
        assertEquals(emptyList<EvolutionStep>(), state.component2())
        assertFalse(state.component3())
        assertEquals(DetailTab.About, state.component4())
    }
}
