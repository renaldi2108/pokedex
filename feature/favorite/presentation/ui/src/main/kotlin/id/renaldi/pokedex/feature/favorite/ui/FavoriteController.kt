package id.renaldi.pokedex.feature.favorite.ui

import id.renaldi.pokedex.core.model.Pokemon
import id.renaldi.pokedex.feature.detail.navigation.DetailRoute
import id.renaldi.pokedex.feature.favorite.domain.ObserveFavoritePokemon
import id.renaldi.pokedex.feature.favorite.domain.ToggleFavorite
import io.github.renaldi2108.arus.core.ArusController
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.nav.Navigator
import kotlinx.coroutines.flow.collectLatest

data class FavoriteState(
    val pokemon: List<Pokemon> = emptyList(),
    val loading: Boolean = true,
) {
    val isEmpty: Boolean get() = !loading && pokemon.isEmpty()
}

class FavoriteController(
    private val observeFavoritePokemon: ObserveFavoritePokemon,
    private val toggleFavorite: ToggleFavorite,
    private val navigator: Navigator,
) : ArusController<FavoriteState, ArusEffect>(FavoriteState()) {

    init {
        launchCatching {
            observeFavoritePokemon().collectLatest { values ->
                setState { copy(pokemon = values, loading = false) }
            }
        }
    }

    fun onPokemonClicked(pokemon: Pokemon) = navigator.push(DetailRoute(pokemon.id))

    fun onFavoriteToggled(pokemon: Pokemon) = launchCatching { toggleFavorite(pokemon.id) }

    fun onBackClicked() {
        navigator.pop()
    }

    override fun onError(error: Throwable) {
        emit(ArusEffect.Snackbar(error.message ?: "Gagal memuat favorit"))
    }
}
