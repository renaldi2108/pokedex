package id.renaldi.pokedex.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PokemonTest {

    private val pikachu = Pokemon(
        id = 25,
        name = "Pikachu",
        types = listOf(PokemonType.Electric),
        genderRate = 4,
        stats = PokemonStats(35, 55, 40, 50, 50, 90),
    )

    @Test
    fun `primaryType is the first declared type`() {
        assertEquals(PokemonType.Electric, pikachu.primaryType)
    }

    @Test
    fun `primaryType falls back to Normal when no type is known`() {
        assertEquals(PokemonType.Normal, Pokemon(id = 0, name = "?").primaryType)
    }

    @Test
    fun `gender rate splits evenly at four of eight`() {
        assertTrue(pikachu.hasGenderRate)
        assertEquals(0.5f, pikachu.femaleRate, 0.001f)
        assertEquals(0.5f, pikachu.maleRate, 0.001f)
    }

    @Test
    fun `an unknown gender rate reports zero for both sides`() {
        val genderless = pikachu.copy(genderRate = UNKNOWN_GENDER_RATE)
        assertFalse(genderless.hasGenderRate)
        assertEquals(0f, genderless.femaleRate, 0.0f)
        assertEquals(0f, genderless.maleRate, 0.0f)
    }

    @Test
    fun `a gender rate of zero is still known`() {
        val maleOnly = pikachu.copy(genderRate = 0)
        assertTrue(maleOnly.hasGenderRate)
        assertEquals(0f, maleOnly.femaleRate, 0.0f)
        assertEquals(1f, maleOnly.maleRate, 0.0f)
    }

    @Test
    fun `defaults leave every optional field empty`() {
        val bare = Pokemon(id = 1, name = "Bulbasaur")
        assertEquals("", bare.description)
        assertEquals(emptyList<PokemonType>(), bare.types)
        assertEquals("", bare.category)
        assertEquals(0.0, bare.height, 0.0)
        assertEquals(0.0, bare.weight, 0.0)
        assertEquals(PokemonStats(), bare.stats)
        assertEquals(emptyList<Evolution>(), bare.evolutionChain)
        assertEquals(emptyList<PokemonMove>(), bare.moves)
        assertEquals(GENDER_RATE_STEPS, 8f, 0.0f)
    }

    @Test
    fun `value semantics hold`() {
        assertEquals(pikachu, pikachu.copy())
        assertEquals(pikachu.hashCode(), pikachu.copy().hashCode())
        assertNotEquals(pikachu, pikachu.copy(id = 26))
        assertTrue(pikachu.toString().contains("Pikachu"))
        assertEquals(25, pikachu.component1())
        assertEquals(25, pikachu.id)
        assertEquals("Pikachu", pikachu.name)
        assertEquals(4, pikachu.genderRate)
        assertEquals("Pikachu", pikachu.component2())
    }
}
