package id.renaldi.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import id.renaldi.pokedex.data.repo.PokemonRepository
import id.renaldi.pokedex.data.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    val imageLoader: ImageLoader,
    private val pokemonRepository: PokemonRepository,
): ViewModel() {
    init {
        viewModelScope.launch {
            println("Populating databases...")

            when(val pokemonResults = pokemonRepository.getAllPokemon()) {
                is Result.Success -> {
                    println("Pokemon database: ${pokemonResults.data.size}")
                }
                is Result.Error -> {
                    throw pokemonResults.exception
                }
            }
        }
    }
}