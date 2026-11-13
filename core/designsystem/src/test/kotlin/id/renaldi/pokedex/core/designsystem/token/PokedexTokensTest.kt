package id.renaldi.pokedex.core.designsystem.token

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PokedexTokensTest {

    @Test
    fun `spacing climbs monotonically from none to xxl`() {
        val scale = listOf(
            Spacing.none, Spacing.xs, Spacing.sm, Spacing.md,
            Spacing.lg, Spacing.xl, Spacing.xxl,
        )
        scale.zipWithNext { smaller, larger ->
            assertTrue("$smaller should be below $larger", smaller < larger)
        }
        assertEquals(0.dp, Spacing.none)
        assertEquals(20.dp, Spacing.gutter)
    }

    @Test
    fun `expressive radii are generous`() {
        assertEquals(28.dp, Radius.chip)
        assertEquals(24.dp, Radius.card)
        assertEquals(32.dp, Radius.sheet)
        assertEquals(40.dp, Radius.hero)
        assertEquals(100.dp, Radius.pill)
    }

    @Test
    fun `artwork sizing grows from list card to hero`() {
        assertTrue(Sizing.artworkEvolution < Sizing.artworkCard)
        assertTrue(Sizing.artworkCard < Sizing.artworkHero)
        assertEquals(10.dp, Sizing.statBarHeight)
        assertEquals(8.dp, Sizing.typeDot)
        assertEquals(240.dp, Sizing.backdrop)
    }
}
