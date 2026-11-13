package id.renaldi.pokedex

import id.renaldi.pokedex.feature.detail.navigation.DetailRoute
import id.renaldi.pokedex.feature.favorite.navigation.FavoriteRoute
import id.renaldi.pokedex.feature.pokedex.navigation.PokedexRoute
import org.junit.Assert.assertEquals
import org.junit.Test

class PokedexDeepLinksTest {

    @Test
    fun `the list link opens the pokedex alone`() {
        assertEquals(listOf(PokedexRoute), pokedexDeepLinks.resolve("pokedex://pokedex"))
    }

    @Test
    fun `a detail link opens a synthetic stack so back lands on the list`() {
        assertEquals(
            listOf(PokedexRoute, DetailRoute(25)),
            pokedexDeepLinks.resolve("pokedex://pokedex/25"),
        )
    }

    @Test
    fun `a favorites link opens the list underneath`() {
        assertEquals(
            listOf(PokedexRoute, FavoriteRoute),
            pokedexDeepLinks.resolve("pokedex://favorites"),
        )
    }

    @Test
    fun `the declared web host resolves the same patterns`() {
        assertEquals(
            listOf(PokedexRoute, DetailRoute(7)),
            pokedexDeepLinks.resolve("https://pokedex.renaldi.id/pokedex/7"),
        )
    }

    @Test
    fun `an unknown path resolves to nothing`() {
        assertEquals(emptyList<Any>(), pokedexDeepLinks.resolve("pokedex://unknown"))
    }

    @Test
    fun `a foreign host does not resolve`() {
        assertEquals(
            emptyList<Any>(),
            pokedexDeepLinks.resolve("https://evil.example.com/pokedex/25"),
        )
    }
}
