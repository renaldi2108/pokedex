package id.renaldi.pokedex.feature.pokedex.ui

import id.renaldi.pokedex.core.model.PokedexFilter
import id.renaldi.pokedex.core.model.PokedexSort
import id.renaldi.pokedex.core.model.PokemonType
import io.github.renaldi2108.arus.core.ArusController
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.nav.Navigator

data class FilterState(val filter: PokedexFilter)

class FilterController(
    initial: PokedexFilter,
    private val navigator: Navigator,
) : ArusController<FilterState, ArusEffect>(FilterState(initial)) {

    fun onTypeToggled(type: PokemonType) =
        setState { copy(filter = filter.toggleType(type)) }

    fun onSortSelected(sort: PokedexSort) =
        setState { copy(filter = filter.copy(sort = sort)) }

    fun onReset() = setState { copy(filter = PokedexFilter(query = filter.query)) }

    fun onApply() = navigator.popWithResult(state.value.filter)

    fun onDismiss() {
        navigator.pop()
    }
}
