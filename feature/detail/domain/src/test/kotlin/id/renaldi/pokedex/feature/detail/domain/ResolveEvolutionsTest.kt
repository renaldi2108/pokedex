package id.renaldi.pokedex.feature.detail.domain

import id.renaldi.pokedex.core.fixtures.SamplePokemon
import id.renaldi.pokedex.core.model.Evolution
import id.renaldi.pokedex.core.model.EvolutionTrigger
import id.renaldi.pokedex.core.testing.FakePokemonRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ResolveEvolutionsTest {

    @Test
    fun `a pokemon without a chain resolves to nothing`() = runTest {
        val resolve = ResolveEvolutions(FakePokemonRepository(SamplePokemon.all))

        assertEquals(emptyList<Any>(), resolve(SamplePokemon.genderless))
    }

    @Test
    fun `each chain entry is paired with its stored pokemon`() = runTest {
        val resolve = ResolveEvolutions(FakePokemonRepository(SamplePokemon.all))

        val steps = resolve(SamplePokemon.pikachu)

        assertEquals(listOf(25, 26), steps.map { it.pokemon.id })
        assertEquals(EvolutionTrigger.LevelUp, steps[0].trigger)
        assertEquals(EvolutionTrigger.UseItem, steps[1].trigger)
        assertEquals(listOf(-1, -1), steps.map { it.targetLevel })
    }

    @Test
    fun `chain entries missing from the database are skipped`() = runTest {
        val resolve = ResolveEvolutions(FakePokemonRepository(listOf(SamplePokemon.pikachu)))

        assertEquals(listOf(25), resolve(SamplePokemon.pikachu).map { it.pokemon.id })
    }

    @Test
    fun `steps come back in national order whatever the chain order`() = runTest {
        val scrambled = SamplePokemon.pikachu.copy(
            evolutionChain = listOf(
                Evolution(26, trigger = EvolutionTrigger.UseItem),
                Evolution(25),
            ),
        )
        val resolve = ResolveEvolutions(FakePokemonRepository(SamplePokemon.all))

        assertEquals(listOf(25, 26), resolve(scrambled).map { it.pokemon.id })
    }

    @Test
    fun `the target level is carried through`() = runTest {
        val leveller = SamplePokemon.bulbasaur
        val resolve = ResolveEvolutions(
            FakePokemonRepository(
                listOf(SamplePokemon.bulbasaur, SamplePokemon.charmander, SamplePokemon.squirtle),
            ),
        )

        val steps = resolve(leveller)
        assertEquals(listOf(1), steps.map { it.pokemon.id })
        assertEquals(listOf(-1), steps.map { it.targetLevel })
    }
}
