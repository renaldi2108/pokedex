package id.renaldi.pokedex.core.model

enum class StatKind(val label: String, val max: Int) {
    Hp("HP", MAX_BASE_STAT),
    Attack("Attack", MAX_BASE_STAT),
    Defense("Defense", MAX_BASE_STAT),
    SpecialAttack("Sp. Atk", MAX_BASE_STAT),
    SpecialDefense("Sp. Def", MAX_BASE_STAT),
    Speed("Speed", MAX_BASE_STAT),
}

const val MAX_BASE_STAT: Int = 255

data class StatEntry(val kind: StatKind, val value: Int) {
    val ratio: Float get() = (value.toFloat() / kind.max).coerceIn(0f, 1f)
}

data class PokemonStats(
    val hp: Int = 0,
    val attack: Int = 0,
    val defense: Int = 0,
    val specialAttack: Int = 0,
    val specialDefense: Int = 0,
    val speed: Int = 0,
) {
    val total: Int get() = hp + attack + defense + specialAttack + specialDefense + speed

    fun entries(): List<StatEntry> = listOf(
        StatEntry(StatKind.Hp, hp),
        StatEntry(StatKind.Attack, attack),
        StatEntry(StatKind.Defense, defense),
        StatEntry(StatKind.SpecialAttack, specialAttack),
        StatEntry(StatKind.SpecialDefense, specialDefense),
        StatEntry(StatKind.Speed, speed),
    )
}
