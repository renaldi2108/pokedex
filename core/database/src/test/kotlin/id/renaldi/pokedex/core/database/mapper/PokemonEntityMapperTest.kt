package id.renaldi.pokedex.core.database.mapper

import id.renaldi.pokedex.core.database.entity.PokemonEntity
import id.renaldi.pokedex.core.model.Evolution
import id.renaldi.pokedex.core.model.EvolutionTrigger
import id.renaldi.pokedex.core.model.Pokemon
import id.renaldi.pokedex.core.model.PokemonMove
import id.renaldi.pokedex.core.model.PokemonStats
import id.renaldi.pokedex.core.model.PokemonType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PokemonEntityMapperTest {

    private val pikachu = Pokemon(
        id = 25,
        name = "Pikachu",
        description = "Electric mouse",
        types = listOf(PokemonType.Electric, PokemonType.Flying),
        category = "Mouse Pokemon",
        height = 0.4,
        weight = 6.0,
        genderRate = 4,
        stats = PokemonStats(35, 55, 40, 50, 50, 90),
        evolutionChain = listOf(
            Evolution(25),
            Evolution(26, 16, EvolutionTrigger.UseItem, 83),
        ),
        moves = listOf(PokemonMove(84, 1), PokemonMove(85, 26)),
    )

    @Test
    fun `a model survives a round trip through the entity`() {
        assertEquals(pikachu, pikachu.toEntity().toModel())
    }

    @Test
    fun `toEntity flattens every collection into its stored encoding`() {
        val entity = pikachu.toEntity()
        assertEquals(25, entity.id)
        assertEquals("Pikachu", entity.name)
        assertEquals("Electric mouse", entity.description)
        assertEquals("Electric,Flying", entity.types)
        assertEquals("Mouse Pokemon", entity.category)
        assertEquals(0.4, entity.height, 0.0)
        assertEquals(6.0, entity.weight, 0.0)
        assertEquals(4, entity.genderRate)
        assertEquals(35, entity.hp)
        assertEquals(55, entity.attack)
        assertEquals(40, entity.defense)
        assertEquals(50, entity.specialAttack)
        assertEquals(50, entity.specialDefense)
        assertEquals(90, entity.speed)
        assertEquals("25,-1,1,-1|26,16,2,83", entity.evolutions)
        assertEquals("84,1|85,26", entity.moves)
    }

    @Test
    fun `encoding and decoding types is symmetric`() {
        val types = listOf(PokemonType.Grass, PokemonType.Poison)
        assertEquals("Grass,Poison", encodeTypes(types))
        assertEquals(types, decodeTypes("Grass,Poison"))
    }

    @Test
    fun `decoding drops blank type segments`() {
        assertEquals(emptyList<PokemonType>(), decodeTypes(""))
        assertEquals(listOf(PokemonType.Fire), decodeTypes("Fire,,"))
    }

    @Test
    fun `encoding and decoding evolutions is symmetric`() {
        val evolutions = listOf(Evolution(1), Evolution(2, 16, EvolutionTrigger.Trade, 7))
        assertEquals("1,-1,1,-1|2,16,3,7", encodeEvolutions(evolutions))
        assertEquals(evolutions, decodeEvolutions("1,-1,1,-1|2,16,3,7"))
    }

    @Test
    fun `decoding drops blank evolution records`() {
        assertEquals(emptyList<Evolution>(), decodeEvolutions(""))
        assertEquals(listOf(Evolution(1)), decodeEvolutions("1,-1,1,-1|"))
    }

    @Test
    fun `encoding and decoding moves is symmetric`() {
        val moves = listOf(PokemonMove(33, 1))
        assertEquals("33,1", encodeMoves(moves))
        assertEquals(moves, decodeMoves("33,1"))
    }

    @Test
    fun `decoding drops blank move records`() {
        assertEquals(emptyList<PokemonMove>(), decodeMoves(""))
        assertEquals(listOf(PokemonMove(33, 1)), decodeMoves("33,1|"))
    }

    @Test
    fun `an empty pokemon stores empty encodings`() {
        val entity = Pokemon(id = 1, name = "Bulbasaur").toEntity()
        assertEquals("", entity.types)
        assertEquals("", entity.evolutions)
        assertEquals("", entity.moves)
        assertEquals(Pokemon(id = 1, name = "Bulbasaur"), entity.toModel())
    }

    @Test
    fun `entity value semantics hold`() {
        val entity = pikachu.toEntity()
        assertEquals(entity, entity.copy())
        assertEquals(entity.hashCode(), entity.copy().hashCode())
        assertNotEquals(entity, entity.copy(id = 26))
        assertTrue(entity.toString().contains("Pikachu"))
        assertEquals(25, entity.component1())
    }
}
