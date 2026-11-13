package id.renaldi.pokedex.core.database.mapper

import id.renaldi.pokedex.core.database.entity.PokemonEntity
import id.renaldi.pokedex.core.model.Evolution
import id.renaldi.pokedex.core.model.EvolutionTrigger
import id.renaldi.pokedex.core.model.Pokemon
import id.renaldi.pokedex.core.model.PokemonMove
import id.renaldi.pokedex.core.model.PokemonStats
import id.renaldi.pokedex.core.model.PokemonType

private const val LIST_SEPARATOR = ","
private const val RECORD_SEPARATOR = "|"
private const val FIELD_SEPARATOR = ","

fun PokemonEntity.toModel(): Pokemon = Pokemon(
    id = id,
    name = name,
    description = description,
    types = decodeTypes(types),
    category = category,
    height = height,
    weight = weight,
    genderRate = genderRate,
    stats = PokemonStats(
        hp = hp,
        attack = attack,
        defense = defense,
        specialAttack = specialAttack,
        specialDefense = specialDefense,
        speed = speed,
    ),
    evolutionChain = decodeEvolutions(evolutions),
    moves = decodeMoves(moves),
)

fun Pokemon.toEntity(): PokemonEntity = PokemonEntity(
    id = id,
    name = name,
    description = description,
    types = encodeTypes(types),
    category = category,
    height = height,
    weight = weight,
    genderRate = genderRate,
    hp = stats.hp,
    attack = stats.attack,
    defense = stats.defense,
    specialAttack = stats.specialAttack,
    specialDefense = stats.specialDefense,
    speed = stats.speed,
    evolutions = encodeEvolutions(evolutionChain),
    moves = encodeMoves(moves),
)

fun encodeTypes(types: List<PokemonType>): String =
    types.joinToString(LIST_SEPARATOR) { it.label }

fun decodeTypes(raw: String): List<PokemonType> = raw
    .split(LIST_SEPARATOR)
    .filter { it.isNotBlank() }
    .map { PokemonType.fromName(it) }

fun encodeEvolutions(evolutions: List<Evolution>): String =
    evolutions.joinToString(RECORD_SEPARATOR) { evolution ->
        listOf(
            evolution.id,
            evolution.targetLevel,
            evolution.trigger.value,
            evolution.itemId,
        ).joinToString(FIELD_SEPARATOR)
    }

fun decodeEvolutions(raw: String): List<Evolution> = raw
    .split(RECORD_SEPARATOR)
    .filter { it.isNotBlank() }
    .map { record ->
        val fields = record.split(FIELD_SEPARATOR)
        Evolution(
            id = fields[0].toInt(),
            targetLevel = fields[1].toInt(),
            trigger = EvolutionTrigger.fromValue(fields[2].toInt()),
            itemId = fields[3].toInt(),
        )
    }

fun encodeMoves(moves: List<PokemonMove>): String =
    moves.joinToString(RECORD_SEPARATOR) { move ->
        listOf(move.id, move.targetLevel).joinToString(FIELD_SEPARATOR)
    }

fun decodeMoves(raw: String): List<PokemonMove> = raw
    .split(RECORD_SEPARATOR)
    .filter { it.isNotBlank() }
    .map { record ->
        val fields = record.split(FIELD_SEPARATOR)
        PokemonMove(id = fields[0].toInt(), targetLevel = fields[1].toInt())
    }
