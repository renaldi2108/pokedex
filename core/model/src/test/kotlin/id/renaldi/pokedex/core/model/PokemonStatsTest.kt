package id.renaldi.pokedex.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PokemonStatsTest {

    private val stats = PokemonStats(35, 55, 40, 50, 50, 90)

    @Test
    fun `total sums the six base stats`() {
        assertEquals(320, stats.total)
        assertEquals(0, PokemonStats().total)
    }

    @Test
    fun `entries lists every stat kind in canonical order`() {
        val entries = stats.entries()
        assertEquals(StatKind.entries.toList(), entries.map { it.kind })
        assertEquals(listOf(35, 55, 40, 50, 50, 90), entries.map { it.value })
    }

    @Test
    fun `ratio divides by the stat maximum`() {
        assertEquals(0.5f, StatEntry(StatKind.Hp, MAX_BASE_STAT / 2).ratio, 0.01f)
    }

    @Test
    fun `ratio is clamped into zero to one`() {
        assertEquals(1f, StatEntry(StatKind.Attack, MAX_BASE_STAT * 3).ratio, 0.0f)
        assertEquals(0f, StatEntry(StatKind.Speed, -40).ratio, 0.0f)
    }

    @Test
    fun `stat kinds carry a label and a maximum`() {
        StatKind.entries.forEach { kind ->
            assertTrue(kind.label.isNotBlank())
            assertEquals(MAX_BASE_STAT, kind.max)
        }
    }

    @Test
    fun `value semantics hold for stats and entries`() {
        assertEquals(stats, stats.copy())
        assertEquals(stats.hashCode(), stats.copy().hashCode())
        assertNotEquals(stats, stats.copy(hp = 1))
        assertTrue(stats.toString().contains("35"))
        assertEquals(stats.hp, stats.component1())
        assertEquals(35, stats.hp)
        assertEquals(55, stats.attack)
        assertEquals(40, stats.defense)
        assertEquals(50, stats.specialAttack)
        assertEquals(50, stats.specialDefense)
        assertEquals(90, stats.speed)

        val entry = StatEntry(StatKind.Hp, 10)
        assertEquals(entry, entry.copy())
        assertEquals(entry.hashCode(), entry.copy().hashCode())
        assertNotEquals(entry, entry.copy(value = 11))
        assertTrue(entry.toString().contains("Hp"))
        assertEquals(StatKind.Hp, entry.component1())
        assertEquals(StatKind.Hp, entry.kind)
        assertEquals(10, entry.value)
        assertEquals(10, entry.component2())
    }
}
