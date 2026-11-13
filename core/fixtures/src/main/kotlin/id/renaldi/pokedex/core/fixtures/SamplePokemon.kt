package id.renaldi.pokedex.core.fixtures

import id.renaldi.pokedex.core.model.Evolution
import id.renaldi.pokedex.core.model.EvolutionTrigger
import id.renaldi.pokedex.core.model.Pokemon
import id.renaldi.pokedex.core.model.PokemonMove
import id.renaldi.pokedex.core.model.PokemonStats
import id.renaldi.pokedex.core.model.PokemonType

object SamplePokemon {

    val bulbasaur: Pokemon = Pokemon(
        id = 1,
        name = "Bulbasaur",
        description = "A strange seed was planted on its back at birth.",
        types = listOf(PokemonType.Grass, PokemonType.Poison),
        category = "Seed Pokemon",
        height = 0.7,
        weight = 6.9,
        genderRate = 1,
        stats = PokemonStats(45, 49, 49, 65, 65, 45),
        evolutionChain = listOf(
            Evolution(1),
            Evolution(2, 16, EvolutionTrigger.LevelUp),
            Evolution(3, 32, EvolutionTrigger.LevelUp),
        ),
        moves = listOf(PokemonMove(33, 1), PokemonMove(45, 3)),
    )

    val charmander: Pokemon = Pokemon(
        id = 4,
        name = "Charmander",
        description = "It has a preference for hot things.",
        types = listOf(PokemonType.Fire),
        category = "Lizard Pokemon",
        height = 0.6,
        weight = 8.5,
        genderRate = 1,
        stats = PokemonStats(39, 52, 43, 60, 50, 65),
        evolutionChain = listOf(Evolution(4), Evolution(5, 16)),
        moves = listOf(PokemonMove(10, 1)),
    )

    val squirtle: Pokemon = Pokemon(
        id = 7,
        name = "Squirtle",
        description = "When it retracts its long neck into its shell, it squirts out water.",
        types = listOf(PokemonType.Water),
        category = "Tiny Turtle Pokemon",
        height = 0.5,
        weight = 9.0,
        genderRate = 1,
        stats = PokemonStats(44, 48, 65, 50, 64, 43),
        evolutionChain = listOf(Evolution(7), Evolution(8, 16)),
        moves = listOf(PokemonMove(33, 1)),
    )

    val pikachu: Pokemon = Pokemon(
        id = 25,
        name = "Pikachu",
        description = "It keeps its tail raised to monitor its surroundings.",
        types = listOf(PokemonType.Electric),
        category = "Mouse Pokemon",
        height = 0.4,
        weight = 6.0,
        genderRate = 4,
        stats = PokemonStats(35, 55, 40, 50, 50, 90),
        evolutionChain = listOf(
            Evolution(25),
            Evolution(26, targetLevel = -1, trigger = EvolutionTrigger.UseItem, itemId = 83),
        ),
        moves = listOf(PokemonMove(84, 1), PokemonMove(85, 26)),
    )

    val raichu: Pokemon = Pokemon(
        id = 26,
        name = "Raichu",
        description = "Its long tail discharges electricity into the ground.",
        types = listOf(PokemonType.Electric),
        category = "Mouse Pokemon",
        height = 0.8,
        weight = 30.0,
        genderRate = 4,
        stats = PokemonStats(60, 90, 55, 90, 80, 110),
        evolutionChain = listOf(
            Evolution(25),
            Evolution(26, targetLevel = -1, trigger = EvolutionTrigger.UseItem, itemId = 83),
        ),
        moves = listOf(PokemonMove(85, 1)),
    )

    val genderless: Pokemon = Pokemon(
        id = 81,
        name = "Magnemite",
        description = "It is born with the ability to defy gravity.",
        types = listOf(PokemonType.Electric, PokemonType.Steel),
        category = "Magnet Pokemon",
        height = 0.3,
        weight = 6.0,
        genderRate = -1,
        stats = PokemonStats(25, 35, 70, 95, 55, 45),
    )

    val untyped: Pokemon = Pokemon(id = 999, name = "Missingno")

    val all: List<Pokemon> = listOf(
        bulbasaur, charmander, squirtle, pikachu, raichu, genderless,
    )
}
