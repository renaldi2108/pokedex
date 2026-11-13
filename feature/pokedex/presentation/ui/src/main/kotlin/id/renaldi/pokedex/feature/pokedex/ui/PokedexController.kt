package id.renaldi.pokedex.feature.pokedex.ui

import androidx.lifecycle.SavedStateHandle
import id.renaldi.pokedex.core.domain.ObservePokedex
import id.renaldi.pokedex.core.model.PokedexFilter
import id.renaldi.pokedex.core.model.PokedexSort
import id.renaldi.pokedex.core.model.Pokemon
import id.renaldi.pokedex.core.model.PokemonType
import id.renaldi.pokedex.feature.detail.navigation.DetailRoute
import id.renaldi.pokedex.feature.favorite.domain.ObserveFavoriteIds
import id.renaldi.pokedex.feature.favorite.domain.ToggleFavorite
import id.renaldi.pokedex.feature.favorite.navigation.FavoriteRoute
import id.renaldi.pokedex.feature.pokedex.domain.FilterPokedex
import id.renaldi.pokedex.feature.pokedex.navigation.FilterRoute
import io.github.renaldi2108.arus.core.Ars
import io.github.renaldi2108.arus.core.ArusController
import io.github.renaldi2108.arus.core.ars
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.nav.NavResult
import io.github.renaldi2108.arus.nav.Navigator
import kotlinx.coroutines.flow.collectLatest

data class PokedexState(
    val pokemon: List<Pokemon> = emptyList(),
    val filter: PokedexFilter = PokedexFilter(),
    val favorites: Set<Int> = emptySet(),
    val loading: Boolean = true,
) {
    val isEmpty: Boolean get() = !loading && pokemon.isEmpty()
}

class PokedexController(
    private val observePokedex: ObservePokedex,
    private val filterPokedex: FilterPokedex,
    private val observeFavoriteIds: ObserveFavoriteIds,
    private val toggleFavorite: ToggleFavorite,
    private val navigator: Navigator,
    handle: SavedStateHandle,
) : ArusController<PokedexState, ArusEffect>(PokedexState(), savedStateHandle = handle) {

    private var all: List<Pokemon> = emptyList()

    private var persistedQuery: String by savedState(KEY_QUERY, "")
    private var persistedSort: String by savedState(KEY_SORT, PokedexSort.Number.name)
    private var persistedTypes: String by savedState(KEY_TYPES, "")

    val draftQuery: Ars<String> = ars("")

    init {
        val restored = PokedexFilter(
            query = persistedQuery,
            types = decodeTypes(persistedTypes),
            sort = PokedexSort.entries.firstOrNull { it.name == persistedSort } ?: PokedexSort.Number,
        )
        draftQuery.value = restored.query
        setState { copy(filter = restored) }

        launchCatching {
            observePokedex().collectLatest { values ->
                all = values
                setState { copy(loading = false) }
                recompute()
            }
        }

        launchCatching {
            observeFavoriteIds().collectLatest { ids ->
                setState { copy(favorites = ids) }
            }
        }
    }

    fun onQueryChanged(query: String) {
        draftQuery.value = query
        persistedQuery = query
        updateFilter { copy(query = query) }
    }

    fun onFilterClicked() = launchCatching {
        val current = state.value.filter
        when (val chosen = navigator.pushForResult(FilterRoute.from(current))) {
            is NavResult.Completed -> applyFilter(chosen.value)
            NavResult.Cancelled -> Unit
        }
    }

    fun onTypeToggled(type: PokemonType) = applyFilter(state.value.filter.toggleType(type))

    fun onSortChanged(sort: PokedexSort) = applyFilter(state.value.filter.copy(sort = sort))

    fun onPokemonClicked(pokemon: Pokemon) = navigator.push(DetailRoute(pokemon.id))

    fun onFavoritesClicked() = navigator.push(FavoriteRoute)

    fun onFavoriteToggled(pokemon: Pokemon) = launchCatching {
        toggleFavorite(pokemon.id)
    }

    private fun applyFilter(filter: PokedexFilter) {
        draftQuery.value = filter.query
        persistedQuery = filter.query
        persistedSort = filter.sort.name
        persistedTypes = encodeTypes(filter.types)
        setState { copy(filter = filter) }
        recompute()
    }

    private fun updateFilter(reducer: PokedexFilter.() -> PokedexFilter) {
        val next = state.value.filter.reducer()
        persistedSort = next.sort.name
        persistedTypes = encodeTypes(next.types)
        setState { copy(filter = next) }
        recompute()
    }

    private fun recompute() {
        val visible = filterPokedex(all, state.value.filter)
        setState { copy(pokemon = visible) }
    }

    override fun onError(error: Throwable) {
        emit(ArusEffect.Snackbar(error.message ?: "Terjadi kesalahan"))
    }

    private companion object {
        const val KEY_QUERY = "pokedex.query"
        const val KEY_SORT = "pokedex.sort"
        const val KEY_TYPES = "pokedex.types"
        const val TYPE_SEPARATOR = ","

        fun encodeTypes(types: Set<PokemonType>): String =
            types.joinToString(TYPE_SEPARATOR) { it.label }

        fun decodeTypes(raw: String): Set<PokemonType> = raw
            .split(TYPE_SEPARATOR)
            .filter { it.isNotBlank() }
            .map { PokemonType.fromName(it) }
            .toSet()
    }
}
