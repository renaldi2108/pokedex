package id.renaldi.pokedex.core.common

import org.junit.Assert.assertEquals
import org.junit.Test

private const val FORM_FEED = '\u000C'

class PokemonFormatTest {

    @Test
    fun `paddedId pads to three digits and leaves longer ids alone`() {
        assertEquals("001", PokemonFormat.paddedId(1))
        assertEquals("025", PokemonFormat.paddedId(25))
        assertEquals("151", PokemonFormat.paddedId(151))
        assertEquals("1024", PokemonFormat.paddedId(1024))
    }

    @Test
    fun `displayId prefixes with a hash`() {
        assertEquals("#025", PokemonFormat.displayId(25))
    }

    @Test
    fun `artworkUrl points at the padded official artwork`() {
        assertEquals(
            "https://assets.pokemon.com/assets/cms2/img/pokedex/full/025.png",
            PokemonFormat.artworkUrl(25),
        )
    }

    @Test
    fun `displayName capitalises the first character only`() {
        assertEquals("Pikachu", PokemonFormat.displayName("pikachu"))
        assertEquals("Pikachu", PokemonFormat.displayName("Pikachu"))
        assertEquals("", PokemonFormat.displayName(""))
    }

    @Test
    fun `height and weight render one decimal with a unit`() {
        assertEquals("0.4 m", PokemonFormat.height(0.4))
        assertEquals("30.0 kg", PokemonFormat.weight(30.0))
        assertEquals("6.9 kg", PokemonFormat.weight(6.9))
        assertEquals("0.0 m", PokemonFormat.height(0.0))
    }

    @Test
    fun `flavorText flattens newlines and form feeds`() {
        assertEquals("a b c", PokemonFormat.flavorText("a\nb${FORM_FEED}c"))
    }

    @Test
    fun `flavorText rewrites the shouted franchise name`() {
        assertEquals("a pokemon b", PokemonFormat.flavorText("a POKéMON b"))
    }

    @Test
    fun `flavorText replaces a shouted pokemon name when one is supplied`() {
        assertEquals(
            "Pikachu is quick",
            PokemonFormat.flavorText("PIKACHU is quick", "pikachu"),
        )
    }

    @Test
    fun `flavorText leaves the text alone when the name is blank`() {
        assertEquals("PIKACHU is quick", PokemonFormat.flavorText("PIKACHU is quick", "  "))
    }
}
