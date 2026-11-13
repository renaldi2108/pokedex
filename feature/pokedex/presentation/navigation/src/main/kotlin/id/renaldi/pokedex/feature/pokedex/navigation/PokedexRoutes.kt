package id.renaldi.pokedex.feature.pokedex.navigation

import id.renaldi.pokedex.core.model.PokedexFilter
import id.renaldi.pokedex.core.model.PokedexSort
import id.renaldi.pokedex.core.model.PokemonType
import id.renaldi.pokedex.core.navigation.AppRoute
import io.github.renaldi2108.arus.nav.ResultRoute
import kotlinx.serialization.Serializable

@Serializable
data object PokedexRoute : AppRoute

@Serializable
data class FilterRoute(
    val query: String,
    val types: List<String>,
    val sort: String,
) : AppRoute, ResultRoute<PokedexFilter> {

    fun toFilter(): PokedexFilter = PokedexFilter(
        query = query,
        types = types.map { PokemonType.fromName(it) }.toSet(),
        sort = PokedexSort.entries.firstOrNull { it.name == sort } ?: PokedexSort.Number,
    )

    companion object {
        fun from(filter: PokedexFilter): FilterRoute = FilterRoute(
            query = filter.query,
            types = filter.types.map { it.label },
            sort = filter.sort.name,
        )
    }
}
