package id.renaldi.pokedex.core.model

enum class EvolutionTrigger(val value: Int) {
    LevelUp(1),
    UseItem(2),
    Trade(3),
    ;

    companion object {
        fun fromValue(value: Int): EvolutionTrigger =
            entries.firstOrNull { it.value == value } ?: LevelUp
    }
}

data class Evolution(
    val id: Int,
    val targetLevel: Int = UNKNOWN_LEVEL,
    val trigger: EvolutionTrigger = EvolutionTrigger.LevelUp,
    val itemId: Int = UNKNOWN_ITEM,
) {
    val hasTargetLevel: Boolean get() = targetLevel > 0
}

const val UNKNOWN_LEVEL: Int = -1
const val UNKNOWN_ITEM: Int = -1

data class EvolutionStep(
    val pokemon: Pokemon,
    val trigger: EvolutionTrigger,
    val targetLevel: Int,
)
