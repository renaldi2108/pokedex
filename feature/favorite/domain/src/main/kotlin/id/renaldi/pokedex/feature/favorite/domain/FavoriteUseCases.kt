package id.renaldi.pokedex.feature.favorite.domain

import id.renaldi.pokedex.core.domain.FavoriteRepository
import id.renaldi.pokedex.core.domain.PokemonRepository
import id.renaldi.pokedex.core.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObserveFavoriteIds(private val repository: FavoriteRepository) {

    operator fun invoke(): Flow<Set<Int>> = repository.observeIds()
}

class ToggleFavorite(private val repository: FavoriteRepository) {

    suspend operator fun invoke(pokemonId: Int) = repository.toggle(pokemonId)
}

class ObserveFavoritePokemon(
    private val pokemonRepository: PokemonRepository,
    private val favoriteRepository: FavoriteRepository,
) {

    operator fun invoke(): Flow<List<Pokemon>> =
        pokemonRepository.observeAll().combine(favoriteRepository.observeIds()) { all, ids ->
            all.filter { it.id in ids }.sortedBy { it.id }
        }
}
