package id.renaldi.pokedex.core.model

enum class PokedexSort(val label: String) {
    Number("Number"),
    Name("Name"),
    Strongest("Strongest"),
}

data class PokedexFilter(
    val query: String = "",
    val types: Set<PokemonType> = emptySet(),
    val sort: PokedexSort = PokedexSort.Number,
) {
    val isActive: Boolean get() = query.isNotBlank() || types.isNotEmpty() || sort != PokedexSort.Number

    fun toggleType(type: PokemonType): PokedexFilter =
        copy(types = if (type in types) types - type else types + type)
}
