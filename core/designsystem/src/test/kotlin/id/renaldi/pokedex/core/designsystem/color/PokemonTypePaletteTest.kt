package id.renaldi.pokedex.core.designsystem.color

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import id.renaldi.pokedex.core.model.PokemonType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PokemonTypePaletteTest {

    @Test
    fun `every type has a distinct seed colour`() {
        val seeds = PokemonType.entries.map { pokemonTypeSeed(it) }
        assertEquals(PokemonType.entries.size, seeds.size)
        assertEquals(seeds.size, seeds.toSet().size)
    }

    @Test
    fun `a palette is derived for every type in both modes`() {
        PokemonType.entries.forEach { type ->
            listOf(true, false).forEach { dark ->
                val palette = pokemonTypePalette(type, dark)
                assertEquals(pokemonTypeSeed(type), palette.seed)
                assertNotEquals(palette.seed, palette.container)
                assertNotEquals(palette.seed, palette.onContainer)
                assertNotEquals(palette.seed, palette.accent)
            }
        }
    }

    @Test
    fun `the light container is lighter than the dark container`() {
        PokemonType.entries.forEach { type ->
            val light = pokemonTypePalette(type, dark = false)
            val dark = pokemonTypePalette(type, dark = true)
            assertTrue(
                "${type.label} light container should out-shine the dark one",
                light.container.luminance() > dark.container.luminance(),
            )
        }
    }

    @Test
    fun `towards blends fully at both ends`() {
        val red = Color(0xFFFF0000)
        assertEquals(red, red.towards(Color.White, 0f))
        assertEquals(Color.White, red.towards(Color.White, 1f))
    }

    @Test
    fun `readableOn picks ink on bright seeds and white on dark ones`() {
        assertEquals(Color(0xFF14131A), Color.White.readableOn())
        assertEquals(Color.White, Color.Black.readableOn())
    }

    @Test
    fun `onSeed stays readable against its own seed for every type`() {
        PokemonType.entries.forEach { type ->
            val palette = pokemonTypePalette(type, dark = false)
            val contrast = palette.seed.luminance() - palette.onSeed.luminance()
            assertTrue("${type.label} needs contrast", kotlin.math.abs(contrast) > 0.1f)
        }
    }

    @Test
    fun `prefersDarkIcons follows the ink chosen for the seed`() {
        PokemonType.entries.forEach { type ->
            val palette = pokemonTypePalette(type, dark = false)
            assertEquals(
                "${type.label} icon hint should match its onSeed ink",
                palette.onSeed != Color.White,
                palette.prefersDarkIcons,
            )
        }
    }

    @Test
    fun `a bright type asks for dark icons and a dark type asks for light ones`() {
        assertTrue(pokemonTypePalette(PokemonType.Electric, dark = false).prefersDarkIcons)
        assertFalse(pokemonTypePalette(PokemonType.Dragon, dark = false).prefersDarkIcons)
    }

    @Test
    fun `blending nothing yields nothing`() {
        assertNull(pokemonTypeContainerBlend(emptyList(), dark = false))
        assertNull(pokemonTypeSeedBlend(emptyList(), dark = false))
    }

    @Test
    fun `blending one type returns that type's own colour`() {
        val palette = pokemonTypePalette(PokemonType.Water, dark = false)
        assertEquals(
            palette.container,
            pokemonTypeContainerBlend(listOf(PokemonType.Water), dark = false),
        )
        assertEquals(palette.seed, pokemonTypeSeedBlend(listOf(PokemonType.Water), dark = false))
    }

    @Test
    fun `blending two types lands between them without matching either`() {
        val fire = pokemonTypePalette(PokemonType.Fire, dark = false).seed
        val water = pokemonTypePalette(PokemonType.Water, dark = false).seed
        val blended = pokemonTypeSeedBlend(listOf(PokemonType.Fire, PokemonType.Water), false)!!

        assertNotEquals(fire, blended)
        assertNotEquals(water, blended)
        assertTrue(blended.red < fire.red && blended.red > water.red)
        assertTrue(blended.blue > fire.blue && blended.blue < water.blue)
    }

    @Test
    fun `blending is an equal-weight average whatever the order`() {
        val types = listOf(PokemonType.Fire, PokemonType.Water, PokemonType.Grass)
        val forward = pokemonTypeSeedBlend(types, dark = false)!!
        val reversed = pokemonTypeSeedBlend(types.reversed(), dark = false)!!

        assertEquals(forward.red, reversed.red, 0.01f)
        assertEquals(forward.green, reversed.green, 0.01f)
        assertEquals(forward.blue, reversed.blue, 0.01f)
    }

    @Test
    fun `blending respects the theme it was asked for`() {
        val light = pokemonTypeContainerBlend(listOf(PokemonType.Ice), dark = false)!!
        val dark = pokemonTypeContainerBlend(listOf(PokemonType.Ice), dark = true)!!

        assertTrue(light.luminance() > dark.luminance())
    }

    @Test
    fun `value semantics hold`() {
        val palette = pokemonTypePalette(PokemonType.Electric, dark = false)
        assertEquals(palette, palette.copy())
        assertEquals(palette.hashCode(), palette.copy().hashCode())
        assertNotEquals(palette, palette.copy(seed = Color.Black))
        assertTrue(palette.toString().contains("PokemonTypePalette"))
        assertEquals(palette.seed, palette.component1())
        assertEquals(palette.container, palette.component2())
        assertEquals(palette.onContainer, palette.component3())
        assertEquals(palette.accent, palette.component4())
        assertEquals(palette.onSeed, palette.component5())
    }
}
