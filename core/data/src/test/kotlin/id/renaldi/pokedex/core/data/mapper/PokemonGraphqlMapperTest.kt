package id.renaldi.pokedex.core.data.mapper

import id.renaldi.pokedex.core.data.graphql.PokemonOriginalQuery
import id.renaldi.pokedex.core.model.Evolution
import id.renaldi.pokedex.core.model.EvolutionTrigger
import id.renaldi.pokedex.core.model.PokemonMove
import id.renaldi.pokedex.core.model.PokemonStats
import id.renaldi.pokedex.core.model.PokemonType
import id.renaldi.pokedex.core.model.UNKNOWN_GENDER_RATE
import id.renaldi.pokedex.core.model.UNKNOWN_ITEM
import id.renaldi.pokedex.core.model.UNKNOWN_LEVEL
import org.junit.Assert.assertEquals
import org.junit.Test

private fun stat(id: Int, base: Int) =
    PokemonOriginalQuery.Stat(PokemonOriginalQuery.Stat1(id, "s$id"), base)

private fun fullStats() = listOf(
    stat(1, 35), stat(2, 55), stat(3, 40), stat(4, 50), stat(5, 50), stat(6, 90),
)

private fun detail(
    types: List<PokemonOriginalQuery.Type> = listOf(
        PokemonOriginalQuery.Type(PokemonOriginalQuery.Type1("electric")),
    ),
    stats: List<PokemonOriginalQuery.Stat> = fullStats(),
    height: Int? = 4,
    weight: Int? = 60,
    moves: List<PokemonOriginalQuery.Move> = listOf(PokemonOriginalQuery.Move(1, 84)),
) = PokemonOriginalQuery.Detail(types, stats, height, weight, moves)

private fun pokemon(
    id: Int = 25,
    name: String = "pikachu",
    description: List<PokemonOriginalQuery.Description> = listOf(
        PokemonOriginalQuery.Description("PIKACHU is quick"),
    ),
    detail: List<PokemonOriginalQuery.Detail> = listOf(detail()),
    genderRate: Int? = 4,
    evolutionChain: PokemonOriginalQuery.EvolutionChain? = null,
    species: List<PokemonOriginalQuery.Species> = listOf(
        PokemonOriginalQuery.Species("Mouse Pokemon"),
    ),
) = PokemonOriginalQuery.Pokemon(
    id, name, description, detail, genderRate, evolutionChain, species,
)

class PokemonGraphqlMapperTest {

    @Test
    fun `a complete response maps every field`() {
        val mapped = pokemon().toPokemon()

        assertEquals(25, mapped.id)
        assertEquals("Pikachu", mapped.name)
        assertEquals("Pikachu is quick", mapped.description)
        assertEquals(listOf(PokemonType.Electric), mapped.types)
        assertEquals("Mouse Pokemon", mapped.category)
        assertEquals(0.4, mapped.height, 0.001)
        assertEquals(6.0, mapped.weight, 0.001)
        assertEquals(4, mapped.genderRate)
        assertEquals(PokemonStats(35, 55, 40, 50, 50, 90), mapped.stats)
        assertEquals(listOf(PokemonMove(84, 1)), mapped.moves)
        assertEquals(emptyList<Evolution>(), mapped.evolutionChain)
    }

    @Test
    fun `toPokemonList maps every entry in the payload`() {
        val data = PokemonOriginalQuery.Data(
            info = PokemonOriginalQuery.Info(PokemonOriginalQuery.Total(2)),
            pokemon = listOf(pokemon(id = 25), pokemon(id = 26, name = "raichu")),
        )

        assertEquals(listOf(25, 26), data.toPokemonList().map { it.id })
        assertEquals(listOf("Pikachu", "Raichu"), data.toPokemonList().map { it.name })
    }

    @Test
    fun `a missing detail block leaves the derived fields empty`() {
        val mapped = pokemon(detail = emptyList()).toPokemon()

        assertEquals(emptyList<PokemonType>(), mapped.types)
        assertEquals(0.0, mapped.height, 0.0)
        assertEquals(0.0, mapped.weight, 0.0)
        assertEquals(PokemonStats(), mapped.stats)
        assertEquals(emptyList<PokemonMove>(), mapped.moves)
    }

    @Test
    fun `null height and weight fall back to zero`() {
        val mapped = pokemon(detail = listOf(detail(height = null, weight = null))).toPokemon()

        assertEquals(0.0, mapped.height, 0.0)
        assertEquals(0.0, mapped.weight, 0.0)
    }

    @Test
    fun `a null gender rate is reported as unknown`() {
        assertEquals(UNKNOWN_GENDER_RATE, pokemon(genderRate = null).toPokemon().genderRate)
    }

    @Test
    fun `missing description and species map to empty strings`() {
        val mapped = pokemon(description = emptyList(), species = emptyList()).toPokemon()

        assertEquals("", mapped.description)
        assertEquals("", mapped.category)
    }

    @Test
    fun `a type without a resolved relationship is dropped`() {
        val mapped = pokemon(
            detail = listOf(detail(types = listOf(PokemonOriginalQuery.Type(null)))),
        ).toPokemon()

        assertEquals(emptyList<PokemonType>(), mapped.types)
    }

    @Test
    fun `stats missing from the payload default to zero`() {
        val mapped = pokemon(detail = listOf(detail(stats = listOf(stat(1, 35))))).toPokemon()

        assertEquals(PokemonStats(hp = 35), mapped.stats)
    }

    @Test
    fun `a stat without a resolved relationship contributes nothing`() {
        val mapped = pokemon(
            detail = listOf(
                detail(stats = listOf(PokemonOriginalQuery.Stat(null, 99))),
            ),
        ).toPokemon()

        assertEquals(PokemonStats(), mapped.stats)
    }

    @Test
    fun `toStats on a null list yields empty stats`() {
        assertEquals(PokemonStats(), null.toStats())
    }

    @Test
    fun `a move without an id is dropped`() {
        val mapped = pokemon(
            detail = listOf(
                detail(
                    moves = listOf(
                        PokemonOriginalQuery.Move(1, null),
                        PokemonOriginalQuery.Move(7, 85),
                    ),
                ),
            ),
        ).toPokemon()

        assertEquals(listOf(PokemonMove(85, 7)), mapped.moves)
    }

    @Test
    fun `an evolution without target levels is a base form`() {
        val chain = PokemonOriginalQuery.EvolutionChain(
            listOf(PokemonOriginalQuery.Evolution(25, emptyList())),
        )

        assertEquals(
            listOf(Evolution(25, UNKNOWN_LEVEL, EvolutionTrigger.LevelUp, UNKNOWN_ITEM)),
            pokemon(evolutionChain = chain).toPokemon().evolutionChain,
        )
    }

    @Test
    fun `trigger codes map to their evolution triggers`() {
        fun chainFor(trigger: Int?) = PokemonOriginalQuery.EvolutionChain(
            listOf(
                PokemonOriginalQuery.Evolution(
                    26,
                    listOf(PokemonOriginalQuery.TargetLevel(16, trigger, 83)),
                ),
            ),
        )

        fun triggerFor(code: Int?) =
            pokemon(evolutionChain = chainFor(code)).toPokemon().evolutionChain.first().trigger

        assertEquals(EvolutionTrigger.UseItem, triggerFor(3))
        assertEquals(EvolutionTrigger.Trade, triggerFor(2))
        assertEquals(EvolutionTrigger.LevelUp, triggerFor(1))
        assertEquals(EvolutionTrigger.LevelUp, triggerFor(null))
    }

    @Test
    fun `a target level carries its level and item`() {
        val chain = PokemonOriginalQuery.EvolutionChain(
            listOf(
                PokemonOriginalQuery.Evolution(
                    26,
                    listOf(PokemonOriginalQuery.TargetLevel(16, 1, 83)),
                ),
            ),
        )

        assertEquals(
            listOf(Evolution(26, 16, EvolutionTrigger.LevelUp, 83)),
            pokemon(evolutionChain = chain).toPokemon().evolutionChain,
        )
    }

    @Test
    fun `a null level and item are reported as unknown`() {
        val chain = PokemonOriginalQuery.EvolutionChain(
            listOf(
                PokemonOriginalQuery.Evolution(
                    26,
                    listOf(PokemonOriginalQuery.TargetLevel(null, 1, null)),
                ),
            ),
        )

        assertEquals(
            listOf(Evolution(26, UNKNOWN_LEVEL, EvolutionTrigger.LevelUp, UNKNOWN_ITEM)),
            pokemon(evolutionChain = chain).toPokemon().evolutionChain,
        )
    }
}
