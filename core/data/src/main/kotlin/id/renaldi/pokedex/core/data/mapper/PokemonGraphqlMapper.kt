package id.renaldi.pokedex.core.data.mapper

import id.renaldi.pokedex.core.common.PokemonFormat
import id.renaldi.pokedex.core.data.graphql.PokemonOriginalQuery
import id.renaldi.pokedex.core.model.Evolution
import id.renaldi.pokedex.core.model.EvolutionTrigger
import id.renaldi.pokedex.core.model.Pokemon
import id.renaldi.pokedex.core.model.PokemonMove
import id.renaldi.pokedex.core.model.PokemonStats
import id.renaldi.pokedex.core.model.PokemonType
import id.renaldi.pokedex.core.model.UNKNOWN_GENDER_RATE
import id.renaldi.pokedex.core.model.UNKNOWN_ITEM
import id.renaldi.pokedex.core.model.UNKNOWN_LEVEL

private const val DECIMETRE_TO_METRE = 10.0
private const val HECTOGRAM_TO_KILOGRAM = 10.0

private const val STAT_HP = 1
private const val STAT_ATTACK = 2
private const val STAT_DEFENSE = 3
private const val STAT_SPECIAL_ATTACK = 4
private const val STAT_SPECIAL_DEFENSE = 5
private const val STAT_SPEED = 6

private const val TRIGGER_TRADE = 2
private const val TRIGGER_USE_ITEM = 3

fun PokemonOriginalQuery.Data.toPokemonList(): List<Pokemon> = pokemon.map { it.toPokemon() }

fun PokemonOriginalQuery.Pokemon.toPokemon(): Pokemon {
    val detail = detail.firstOrNull()
    return Pokemon(
        id = id,
        name = PokemonFormat.displayName(name),
        description = description.firstOrNull()
            ?.let { PokemonFormat.flavorText(it.flavorText, name) }
            .orEmpty(),
        types = detail?.types?.mapNotNull { it.type?.name }
            ?.map { PokemonType.fromName(it) }
            .orEmpty(),
        category = species.firstOrNull()?.genus.orEmpty(),
        height = (detail?.height ?: 0) / DECIMETRE_TO_METRE,
        weight = (detail?.weight ?: 0) / HECTOGRAM_TO_KILOGRAM,
        genderRate = genderRate ?: UNKNOWN_GENDER_RATE,
        stats = detail?.stats.toStats(),
        evolutionChain = evolutionChain?.evolutions?.map { it.toEvolution() }.orEmpty(),
        moves = detail?.moves?.toMoves().orEmpty(),
    )
}

fun List<PokemonOriginalQuery.Stat>?.toStats(): PokemonStats {
    if (this == null) return PokemonStats()
    val byId = associate { it.stat?.id to it.baseStat }
    return PokemonStats(
        hp = byId[STAT_HP] ?: 0,
        attack = byId[STAT_ATTACK] ?: 0,
        defense = byId[STAT_DEFENSE] ?: 0,
        specialAttack = byId[STAT_SPECIAL_ATTACK] ?: 0,
        specialDefense = byId[STAT_SPECIAL_DEFENSE] ?: 0,
        speed = byId[STAT_SPEED] ?: 0,
    )
}

fun PokemonOriginalQuery.Evolution.toEvolution(): Evolution {
    val target = targetLevels.firstOrNull() ?: return Evolution(id = id)
    return Evolution(
        id = id,
        targetLevel = target.level ?: UNKNOWN_LEVEL,
        trigger = when (target.triggerType) {
            TRIGGER_USE_ITEM -> EvolutionTrigger.UseItem
            TRIGGER_TRADE -> EvolutionTrigger.Trade
            else -> EvolutionTrigger.LevelUp
        },
        itemId = target.itemId ?: UNKNOWN_ITEM,
    )
}

fun List<PokemonOriginalQuery.Move>.toMoves(): List<PokemonMove> =
    mapNotNull { move -> move.id?.let { PokemonMove(id = it, targetLevel = move.level) } }
