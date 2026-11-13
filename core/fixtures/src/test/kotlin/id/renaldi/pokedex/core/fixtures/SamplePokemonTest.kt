package id.renaldi.pokedex.core.fixtures

import id.renaldi.pokedex.core.model.PokemonType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SamplePokemonTest {

    @Test
    fun `all holds the six canonical fixtures in national order`() {
        assertEquals(listOf(1, 4, 7, 25, 26, 81), SamplePokemon.all.map { it.id })
    }

    @Test
    fun `every fixture in all is named and typed`() {
        SamplePokemon.all.forEach { pokemon ->
            assertTrue(pokemon.name.isNotBlank())
            assertTrue(pokemon.types.isNotEmpty())
            assertTrue(pokemon.stats.total > 0)
        }
    }

    @Test
    fun `the dual type fixture exposes both of its types`() {
        assertEquals(
            listOf(PokemonType.Grass, PokemonType.Poison),
            SamplePokemon.bulbasaur.types,
        )
    }

    @Test
    fun `named fixtures cover the shapes the screens need`() {
        assertEquals(4, SamplePokemon.charmander.id)
        assertEquals(7, SamplePokemon.squirtle.id)
        assertEquals(25, SamplePokemon.pikachu.id)
        assertEquals(26, SamplePokemon.raichu.id)
        assertFalse(SamplePokemon.genderless.hasGenderRate)
        assertEquals(PokemonType.Normal, SamplePokemon.untyped.primaryType)
    }
}
