package id.renaldi.pokedex.core.model

enum class PokemonType(val label: String) {
    Normal("Normal"),
    Fire("Fire"),
    Fighting("Fighting"),
    Water("Water"),
    Flying("Flying"),
    Grass("Grass"),
    Poison("Poison"),
    Electric("Electric"),
    Ground("Ground"),
    Psychic("Psychic"),
    Rock("Rock"),
    Ice("Ice"),
    Bug("Bug"),
    Dragon("Dragon"),
    Ghost("Ghost"),
    Dark("Dark"),
    Steel("Steel"),
    Fairy("Fairy"),
    ;

    companion object {
        fun fromName(raw: String): PokemonType =
            entries.firstOrNull { it.label.equals(raw.trim(), ignoreCase = true) } ?: Normal
    }
}
