package id.renaldi.pokedex.feature.pokedex.navigation

import id.renaldi.pokedex.core.model.PokedexFilter
import id.renaldi.pokedex.core.model.PokedexSort
import id.renaldi.pokedex.core.model.PokemonType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PokedexRoutesTest {

    private val filter = PokedexFilter(
        query = "pika",
        types = setOf(PokemonType.Electric),
        sort = PokedexSort.Name,
    )

    @Test
    fun `a filter survives a round trip through the route`() {
        assertEquals(filter, FilterRoute.from(filter).toFilter())
    }

    @Test
    fun `from flattens the filter into serialisable primitives`() {
        val route = FilterRoute.from(filter)

        assertEquals("pika", route.query)
        assertEquals(listOf("Electric"), route.types)
        assertEquals("Name", route.sort)
    }

    @Test
    fun `toFilter falls back to Number for an unknown sort`() {
        assertEquals(
            PokedexSort.Number,
            FilterRoute("", emptyList(), "Nonsense").toFilter().sort,
        )
    }

    @Test
    fun `toFilter falls back to Normal for an unknown type`() {
        assertEquals(
            setOf(PokemonType.Normal),
            FilterRoute("", listOf("Sound"), "Number").toFilter().types,
        )
    }

    @Test
    fun `an empty route maps to a default filter`() {
        assertEquals(PokedexFilter(), FilterRoute("", emptyList(), "Number").toFilter())
    }

    @Test
    fun `the list route is a singleton destination`() {
        assertEquals(PokedexRoute, PokedexRoute)
        assertTrue(PokedexRoute.toString().contains("PokedexRoute"))
    }

    @Test
    fun `value semantics hold`() {
        val route = FilterRoute.from(filter)
        assertEquals(route, route.copy())
        assertEquals(route.hashCode(), route.copy().hashCode())
        assertNotEquals(route, route.copy(query = "rai"))
        assertTrue(route.toString().contains("pika"))
        assertEquals("pika", route.component1())
        assertEquals(listOf("Electric"), route.component2())
        assertEquals("Name", route.component3())
    }
}
