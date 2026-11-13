package id.renaldi.pokedex.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class PokemonTypeTest {

    @Test
    fun `fromName resolves every label ignoring case and padding`() {
        PokemonType.entries.forEach { type ->
            assertEquals(type, PokemonType.fromName(type.label))
            assertEquals(type, PokemonType.fromName(type.label.uppercase()))
            assertEquals(type, PokemonType.fromName(type.label.lowercase()))
            assertEquals(type, PokemonType.fromName("  ${type.label}  "))
        }
    }

    @Test
    fun `fromName falls back to Normal for anything unknown`() {
        assertEquals(PokemonType.Normal, PokemonType.fromName("Sound"))
        assertEquals(PokemonType.Normal, PokemonType.fromName(""))
    }

    @Test
    fun `label is exposed for every entry`() {
        assertEquals("Electric", PokemonType.Electric.label)
        assertEquals(18, PokemonType.entries.size)
    }
}
