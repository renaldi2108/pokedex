package id.renaldi.pokedex.feature.detail.navigation

import id.renaldi.pokedex.core.navigation.AppRoute
import kotlinx.serialization.Serializable

@Serializable
data class DetailRoute(val pokemonId: Int) : AppRoute
