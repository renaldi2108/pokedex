package id.renaldi.pokedex.feature.detail.ui

import id.renaldi.pokedex.core.domain.PokemonRepository
import id.renaldi.pokedex.core.model.EvolutionStep
import id.renaldi.pokedex.core.model.Pokemon
import id.renaldi.pokedex.feature.detail.domain.ResolveEvolutions
import id.renaldi.pokedex.feature.detail.navigation.DetailRoute
import id.renaldi.pokedex.feature.favorite.domain.ObserveFavoriteIds
import id.renaldi.pokedex.feature.favorite.domain.ToggleFavorite
import io.github.renaldi2108.arus.core.ArusController
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.nav.Navigator
import kotlinx.coroutines.flow.collectLatest

enum class DetailTab(val label: String) {
    About("About"),
    BaseStats("Base Stats"),
    Evolution("Evolution"),
}

data class DetailState(
    val pokemon: Pokemon? = null,
    val evolutions: List<EvolutionStep> = emptyList(),
    val isFavorite: Boolean = false,
    val tab: DetailTab = DetailTab.About,
) {
    val loading: Boolean get() = pokemon == null
}

class DetailController(
    private val pokemonId: Int,
    private val repository: PokemonRepository,
    private val resolveEvolutions: ResolveEvolutions,
    private val observeFavoriteIds: ObserveFavoriteIds,
    private val toggleFavorite: ToggleFavorite,
    private val navigator: Navigator,
) : ArusController<DetailState, ArusEffect>(DetailState()) {

    init {
        launchCatching {
            repository.observeById(pokemonId).collectLatest { pokemon ->
                setState { copy(pokemon = pokemon) }
                val steps = pokemon?.let { resolveEvolutions(it) } ?: emptyList()
                setState { copy(evolutions = steps) }
            }
        }

        launchCatching {
            observeFavoriteIds().collectLatest { ids ->
                setState { copy(isFavorite = pokemonId in ids) }
            }
        }
    }

    fun onTabSelected(tab: DetailTab) = setState { copy(tab = tab) }

    fun onFavoriteToggled() = launchCatching { toggleFavorite(pokemonId) }

    fun onEvolutionClicked(step: EvolutionStep) {
        if (step.pokemon.id != pokemonId) {
            navigator.push(DetailRoute(step.pokemon.id))
        }
    }

    fun onBackClicked() {
        navigator.pop()
    }

    override fun onError(error: Throwable) {
        emit(ArusEffect.Snackbar(error.message ?: "Gagal memuat detail"))
    }
}
