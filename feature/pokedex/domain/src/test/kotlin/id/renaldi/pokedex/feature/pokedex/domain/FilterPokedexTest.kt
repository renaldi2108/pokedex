package id.renaldi.pokedex.feature.pokedex.domain

import id.renaldi.pokedex.core.fixtures.SamplePokemon
import id.renaldi.pokedex.core.model.PokedexFilter
import id.renaldi.pokedex.core.model.PokedexSort
import id.renaldi.pokedex.core.model.PokemonType
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterPokedexTest {

    private val filterPokedex = FilterPokedex()
    private val all = SamplePokemon.all

    @Test
    fun `an empty filter keeps everything in national order`() {
        assertEquals(
            listOf(1, 4, 7, 25, 26, 81),
            filterPokedex(all, PokedexFilter()).map { it.id },
        )
    }

    @Test
    fun `a blank query is treated as no query`() {
        assertEquals(all.size, filterPokedex(all, PokedexFilter(query = "   ")).size)
    }

    @Test
    fun `a query matches the name case insensitively`() {
        assertEquals(
            listOf(25),
            filterPokedex(all, PokedexFilter(query = "PIKA")).map { it.id },
        )
    }

    @Test
    fun `a query matches the padded national id`() {
        assertEquals(
            listOf(25),
            filterPokedex(all, PokedexFilter(query = "025")).map { it.id },
        )
    }

    @Test
    fun `a query is trimmed before matching`() {
        assertEquals(
            listOf(25),
            filterPokedex(all, PokedexFilter(query = "  pikachu  ")).map { it.id },
        )
    }

    @Test
    fun `a query matching nothing yields nothing`() {
        assertEquals(emptyList<Int>(), filterPokedex(all, PokedexFilter(query = "zzz")))
    }

    @Test
    fun `a type filter keeps any pokemon carrying that type`() {
        assertEquals(
            listOf(25, 26, 81),
            filterPokedex(all, PokedexFilter(types = setOf(PokemonType.Electric))).map { it.id },
        )
    }

    @Test
    fun `a type filter matches a secondary type too`() {
        assertEquals(
            listOf(1),
            filterPokedex(all, PokedexFilter(types = setOf(PokemonType.Poison))).map { it.id },
        )
    }

    @Test
    fun `several types widen the result`() {
        assertEquals(
            listOf(4, 7),
            filterPokedex(
                all,
                PokedexFilter(types = setOf(PokemonType.Fire, PokemonType.Water)),
            ).map { it.id },
        )
    }

    @Test
    fun `query and type filters combine`() {
        assertEquals(
            listOf(25),
            filterPokedex(
                all,
                PokedexFilter(query = "pika", types = setOf(PokemonType.Electric)),
            ).map { it.id },
        )
    }

    @Test
    fun `sorting by name is alphabetical and case insensitive`() {
        assertEquals(
            listOf("Bulbasaur", "Charmander", "Magnemite", "Pikachu", "Raichu", "Squirtle"),
            filterPokedex(all, PokedexFilter(sort = PokedexSort.Name)).map { it.name },
        )
    }

    @Test
    fun `sorting by strength puts the highest stat total first`() {
        val sorted = filterPokedex(all, PokedexFilter(sort = PokedexSort.Strongest))
        assertEquals(26, sorted.first().id)
        assertEquals(
            sorted.map { it.stats.total },
            sorted.map { it.stats.total }.sortedDescending(),
        )
    }
}
