package id.renaldi.pokedex.feature.pokedex.ui

import id.renaldi.pokedex.core.model.PokedexFilter
import id.renaldi.pokedex.core.model.PokedexSort
import id.renaldi.pokedex.core.model.PokemonType
import id.renaldi.pokedex.feature.pokedex.navigation.FilterRoute
import id.renaldi.pokedex.feature.pokedex.navigation.PokedexRoute
import io.github.renaldi2108.arus.testing.FakeNavigator
import io.github.renaldi2108.arus.testing.NavOperation
import io.github.renaldi2108.arus.testing.runControllerTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FilterControllerTest {

    private val initial = PokedexFilter(
        query = "pika",
        types = setOf(PokemonType.Electric),
        sort = PokedexSort.Number,
    )

    private fun navigator() = FakeNavigator(PokedexRoute).also {
        it.push(FilterRoute.from(initial))
    }

    @Test
    fun `the sheet opens on the filter it was given`() = runTest {
        runControllerTest {
            assertEquals(initial, FilterController(initial, navigator()).state.value.filter)
        }
    }

    @Test
    fun `toggling a type adds it then removes it`() = runTest {
        runControllerTest {
            val controller = FilterController(PokedexFilter(), navigator())

            controller.onTypeToggled(PokemonType.Water)
            assertEquals(setOf(PokemonType.Water), controller.state.value.filter.types)

            controller.onTypeToggled(PokemonType.Water)
            assertEquals(emptySet<PokemonType>(), controller.state.value.filter.types)
        }
    }

    @Test
    fun `selecting a sort replaces the previous one`() = runTest {
        runControllerTest {
            val controller = FilterController(initial, navigator())

            controller.onSortSelected(PokedexSort.Strongest)

            assertEquals(PokedexSort.Strongest, controller.state.value.filter.sort)
        }
    }

    @Test
    fun `reset clears types and sort but keeps what was typed`() = runTest {
        runControllerTest {
            val controller = FilterController(
                initial.copy(sort = PokedexSort.Name),
                navigator(),
            )

            controller.onReset()

            assertEquals(PokedexFilter(query = "pika"), controller.state.value.filter)
        }
    }

    @Test
    fun `applying hands the filter back to the caller`() = runTest {
        runControllerTest {
            val navigator = navigator()
            val controller = FilterController(initial, navigator)

            controller.onSortSelected(PokedexSort.Name)
            controller.onApply()

            assertEquals(
                NavOperation.PopWithResult(initial.copy(sort = PokedexSort.Name)),
                navigator.operations.last { it is NavOperation.PopWithResult },
            )
            assertEquals(PokedexRoute, navigator.currentRoute.value)
        }
    }

    @Test
    fun `dismissing pops without a result`() = runTest {
        runControllerTest {
            val navigator = navigator()

            FilterController(initial, navigator).onDismiss()

            assertEquals(PokedexRoute, navigator.currentRoute.value)
            assertTrue(navigator.operations.none { it is NavOperation.PopWithResult })
        }
    }

    @Test
    fun `value semantics hold`() = runTest {
        runControllerTest {
            val state = FilterState(initial)
            assertEquals(state, state.copy())
            assertEquals(state.hashCode(), state.copy().hashCode())
            assertNotEquals(state, state.copy(filter = PokedexFilter()))
            assertTrue(state.toString().contains("pika"))
            assertEquals(initial, state.component1())
        }
    }
}
