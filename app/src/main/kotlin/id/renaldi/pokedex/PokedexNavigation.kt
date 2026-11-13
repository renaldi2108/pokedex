package id.renaldi.pokedex

import id.renaldi.pokedex.feature.detail.navigation.DetailRoute
import id.renaldi.pokedex.feature.favorite.navigation.FavoriteRoute
import id.renaldi.pokedex.feature.pokedex.navigation.PokedexRoute
import io.github.renaldi2108.arus.nav.ArusDeepLinks

val pokedexDeepLinks: ArusDeepLinks = ArusDeepLinks {
    host("pokedex.renaldi.id")

    path("pokedex") { listOf(PokedexRoute) }
    path("pokedex/{id}") { args -> listOf(PokedexRoute, DetailRoute(args.int("id"))) }
    path("favorites") { listOf(PokedexRoute, FavoriteRoute) }
}
