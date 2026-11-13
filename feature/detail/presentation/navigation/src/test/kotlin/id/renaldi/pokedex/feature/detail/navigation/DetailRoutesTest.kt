package id.renaldi.pokedex.feature.detail.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DetailRoutesTest {

    @Test
    fun `the route carries the national id as a typed field`() {
        assertEquals(25, DetailRoute(25).pokemonId)
    }

    @Test
    fun `value semantics hold`() {
        val route = DetailRoute(25)
        assertEquals(route, route.copy())
        assertEquals(route.hashCode(), route.copy().hashCode())
        assertNotEquals(route, route.copy(pokemonId = 26))
        assertTrue(route.toString().contains("25"))
        assertEquals(25, route.component1())
    }
}
