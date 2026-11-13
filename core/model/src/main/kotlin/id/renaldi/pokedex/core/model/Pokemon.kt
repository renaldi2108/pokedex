package id.renaldi.pokedex.core.model

data class Pokemon(
    val id: Int,
    val name: String,
    val description: String = "",
    val types: List<PokemonType> = emptyList(),
    val category: String = "",
    val height: Double = 0.0,
    val weight: Double = 0.0,
    val genderRate: Int = UNKNOWN_GENDER_RATE,
    val stats: PokemonStats = PokemonStats(),
    val evolutionChain: List<Evolution> = emptyList(),
    val moves: List<PokemonMove> = emptyList(),
) {
    val primaryType: PokemonType get() = types.firstOrNull() ?: PokemonType.Normal

    val hasGenderRate: Boolean get() = genderRate >= 0

    val femaleRate: Float get() = if (hasGenderRate) genderRate / GENDER_RATE_STEPS else 0f

    val maleRate: Float get() = if (hasGenderRate) 1f - femaleRate else 0f
}

const val UNKNOWN_GENDER_RATE: Int = -1
const val GENDER_RATE_STEPS: Float = 8f
