package id.renaldi.pokedex.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EvolutionTest {

    @Test
    fun `fromValue maps every declared trigger`() {
        EvolutionTrigger.entries.forEach { trigger ->
            assertEquals(trigger, EvolutionTrigger.fromValue(trigger.value))
        }
    }

    @Test
    fun `fromValue falls back to LevelUp for unknown codes`() {
        assertEquals(EvolutionTrigger.LevelUp, EvolutionTrigger.fromValue(99))
        assertEquals(EvolutionTrigger.LevelUp, EvolutionTrigger.fromValue(-1))
    }

    @Test
    fun `hasTargetLevel is true only for a positive level`() {
        assertTrue(Evolution(2, targetLevel = 16).hasTargetLevel)
        assertFalse(Evolution(1).hasTargetLevel)
        assertFalse(Evolution(1, targetLevel = 0).hasTargetLevel)
    }

    @Test
    fun `defaults mark level and item as unknown`() {
        val base = Evolution(1)
        assertEquals(UNKNOWN_LEVEL, base.targetLevel)
        assertEquals(UNKNOWN_ITEM, base.itemId)
        assertEquals(EvolutionTrigger.LevelUp, base.trigger)
    }

    @Test
    fun `value semantics hold for evolution and step`() {
        val evolution = Evolution(2, 16, EvolutionTrigger.Trade, 83)
        assertEquals(evolution, evolution.copy())
        assertEquals(evolution.hashCode(), evolution.copy().hashCode())
        assertNotEquals(evolution, evolution.copy(id = 3))
        assertTrue(evolution.toString().contains("16"))
        assertEquals(2, evolution.component1())
        assertEquals(2, evolution.id)
        assertEquals(16, evolution.targetLevel)
        assertEquals(EvolutionTrigger.Trade, evolution.trigger)
        assertEquals(83, evolution.itemId)
        assertEquals(16, evolution.component2())
        assertEquals(EvolutionTrigger.Trade, evolution.component3())
        assertEquals(83, evolution.component4())

        val pokemon = Pokemon(id = 2, name = "Ivysaur")
        val step = EvolutionStep(pokemon, EvolutionTrigger.LevelUp, 16)
        assertEquals(step, step.copy())
        assertEquals(step.hashCode(), step.copy().hashCode())
        assertNotEquals(step, step.copy(targetLevel = 17))
        assertTrue(step.toString().contains("Ivysaur"))
        assertEquals(pokemon, step.component1())
        assertEquals(pokemon, step.pokemon)
        assertEquals(EvolutionTrigger.LevelUp, step.trigger)
        assertEquals(16, step.targetLevel)
        assertEquals(EvolutionTrigger.LevelUp, step.component2())
        assertEquals(16, step.component3())
    }

    @Test
    fun `moves carry an id and the level they are learnt at`() {
        val move = PokemonMove(33, 1)
        assertEquals(33, move.id)
        assertEquals(1, move.targetLevel)
        assertEquals(move, move.copy())
        assertEquals(move.hashCode(), move.copy().hashCode())
        assertNotEquals(move, move.copy(id = 34))
        assertTrue(move.toString().contains("33"))
        assertEquals(33, move.component1())
        assertEquals(1, move.component2())
    }
}
