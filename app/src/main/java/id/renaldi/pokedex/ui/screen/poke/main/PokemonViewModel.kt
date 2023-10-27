package id.renaldi.pokedex.ui.screen.poke.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.renaldi.pokedex.data.model.Pokemon
import id.renaldi.pokedex.data.pref.UserPreferencesRepository
import id.renaldi.pokedex.data.repo.PokemonRepository
import id.renaldi.pokedex.data.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PokedexUiState(
    val pokemon: List<Pokemon> = listOf(),
    val loading: Boolean = false,
)

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    userPreferencesRepository: UserPreferencesRepository,
): ViewModel() {
    var uiState by mutableStateOf(PokedexUiState(loading = true))
        private set

    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow
    var favorites = mutableStateListOf<Pokemon>()
    var showFavorites by mutableStateOf(false)

//    init {
//        refresh()
//    }

    /**
     * Refresh Pokemon list
     */
    fun refresh() {
        viewModelScope.launch {
            when (val result = pokemonRepository.getAllPokemon()) {
                is Result.Success -> {
                    uiState = uiState.copy(
                        loading = false,
                        pokemon = result.data
                    )
                }
                is Result.Error -> {
                    throw result.exception
                }

                else -> {}
            }

            userPreferencesFlow.collect {
                if (favorites.isNotEmpty()) {
                    favorites.clear()
                }

                when (val result = pokemonRepository.getPokemonByIds(it.favorites)) {
                    is Result.Success -> {
                        favorites.addAll(result.data.toList())
                    }
                    else ->
                        favorites
                }
            }
        }
    }

    fun toggleFavorites() {
        showFavorites = !showFavorites
    }
}