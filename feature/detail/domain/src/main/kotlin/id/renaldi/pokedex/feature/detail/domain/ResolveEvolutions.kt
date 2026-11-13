package id.renaldi.pokedex.feature.detail.domain

import id.renaldi.pokedex.core.domain.PokemonRepository
import id.renaldi.pokedex.core.model.EvolutionStep
import id.renaldi.pokedex.core.model.Pokemon

class ResolveEvolutions(private val repository: PokemonRepository) {

    suspend operator fun invoke(pokemon: Pokemon): List<EvolutionStep> {
        if (pokemon.evolutionChain.isEmpty()) return emptyList()
        val byId = repository.byIds(pokemon.evolutionChain.map { it.id }).associateBy { it.id }
        return pokemon.evolutionChain
            .mapNotNull { evolution ->
                byId[evolution.id]?.let { resolved ->
                    EvolutionStep(
                        pokemon = resolved,
                        trigger = evolution.trigger,
                        targetLevel = evolution.targetLevel,
                    )
                }
            }
            .sortedBy { it.pokemon.id }
    }
}
