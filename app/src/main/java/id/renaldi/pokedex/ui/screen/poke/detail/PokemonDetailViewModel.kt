package id.renaldi.pokedex.ui.screen.poke.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.renaldi.pokedex.data.model.Pokemon
import id.renaldi.pokedex.data.Result
import id.renaldi.pokedex.data.model.EvolutionTrigger
import id.renaldi.pokedex.data.model.Move
import id.renaldi.pokedex.data.model.PokemonDetailsEvolutions
import id.renaldi.pokedex.data.pref.UserPreferencesRepository
import id.renaldi.pokedex.data.repo.PokemonRepository
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PokemonDetailsMoves(
    val move: Move,
    val targetLevel: Int
)

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
): ViewModel() {
    private val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow

    var details by mutableStateOf<Pokemon?>(null)
        private set

//    private val _details = MutableLiveData<Pokemon>()
//    val details: LiveData<Pokemon> = _details

    var evolutions by mutableStateOf(listOf<PokemonDetailsEvolutions>())
        private set

    var moves by mutableStateOf(listOf<PokemonDetailsMoves>())
        private set

    var isFavorite by mutableStateOf(false)
        private set

    fun refresh(incomingPokemon: Pokemon) {
        viewModelScope.launch {
            details = incomingPokemon

            val ev = mutableListOf<PokemonDetailsEvolutions>()
            incomingPokemon.evolutionChain.map {
                launch {
                    when (val result = pokemonRepository.getPokemonById(it.id)) {
                        is Result.Success -> {
                            ev.add(PokemonDetailsEvolutions(
                                pokemon = result.data,
                                targetLevel = it.targetLevel,
                                trigger = it.trigger,
                            ))
                        }
                        is Result.Error -> {
                            // TODO: Pokemon only queried from local database which currently limited to original 151
                            println(result.exception)
                        }
                    }
                }
            }.joinAll()
            evolutions = ev
                .sortedBy { it.pokemon.id }
                .toList()

            userPreferencesFlow.collect {
                isFavorite = it.favorites.contains(incomingPokemon.id)
            }
        }
    }

    fun toggleFavorite(pokemonId: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateFavorites(pokemonId)
        }
    }
}