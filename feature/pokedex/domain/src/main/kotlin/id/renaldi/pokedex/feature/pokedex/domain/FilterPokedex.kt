package id.renaldi.pokedex.feature.pokedex.domain

import id.renaldi.pokedex.core.model.PokedexFilter
import id.renaldi.pokedex.core.model.PokedexSort
import id.renaldi.pokedex.core.model.Pokemon

class FilterPokedex {

    operator fun invoke(all: List<Pokemon>, filter: PokedexFilter): List<Pokemon> {
        val matched = all.filter { pokemon ->
            pokemon.matchesQuery(filter.query) && pokemon.matchesTypes(filter)
        }
        return when (filter.sort) {
            PokedexSort.Number -> matched.sortedBy { it.id }
            PokedexSort.Name -> matched.sortedBy { it.name.lowercase() }
            PokedexSort.Strongest -> matched.sortedByDescending { it.stats.total }
        }
    }

    private fun Pokemon.matchesQuery(query: String): Boolean {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return true
        return name.contains(trimmed, ignoreCase = true) ||
            id.toString().padStart(3, '0').contains(trimmed)
    }

    private fun Pokemon.matchesTypes(filter: PokedexFilter): Boolean =
        filter.types.isEmpty() || types.any { it in filter.types }
}
