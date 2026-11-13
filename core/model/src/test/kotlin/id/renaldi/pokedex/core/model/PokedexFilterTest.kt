package id.renaldi.pokedex.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PokedexFilterTest {

    @Test
    fun `a default filter is inactive`() {
        assertFalse(PokedexFilter().isActive)
    }

    @Test
    fun `any of query types or sort makes the filter active`() {
        assertTrue(PokedexFilter(query = "pika").isActive)
        assertTrue(PokedexFilter(types = setOf(PokemonType.Fire)).isActive)
        assertTrue(PokedexFilter(sort = PokedexSort.Name).isActive)
    }

    @Test
    fun `a blank query alone does not activate the filter`() {
        assertFalse(PokedexFilter(query = "   ").isActive)
    }

    @Test
    fun `toggleType adds a missing type and removes a present one`() {
        val added = PokedexFilter().toggleType(PokemonType.Water)
        assertEquals(setOf(PokemonType.Water), added.types)

        val removed = added.toggleType(PokemonType.Water)
        assertEquals(emptySet<PokemonType>(), removed.types)
    }

    @Test
    fun `sort labels are exposed for every entry`() {
        assertEquals(listOf("Number", "Name", "Strongest"), PokedexSort.entries.map { it.label })
    }

    @Test
    fun `value semantics hold`() {
        val filter = PokedexFilter("pika", setOf(PokemonType.Electric), PokedexSort.Name)
        assertEquals(filter, filter.copy())
        assertEquals(filter.hashCode(), filter.copy().hashCode())
        assertNotEquals(filter, filter.copy(query = "rai"))
        assertTrue(filter.toString().contains("pika"))
        assertEquals("pika", filter.component1())
        assertEquals("pika", filter.query)
        assertEquals(setOf(PokemonType.Electric), filter.types)
        assertEquals(PokedexSort.Name, filter.sort)
        assertEquals(setOf(PokemonType.Electric), filter.component2())
        assertEquals(PokedexSort.Name, filter.component3())
    }
}
