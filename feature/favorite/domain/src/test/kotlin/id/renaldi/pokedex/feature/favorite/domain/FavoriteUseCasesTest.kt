package id.renaldi.pokedex.feature.favorite.domain

import id.renaldi.pokedex.core.fixtures.SamplePokemon
import id.renaldi.pokedex.core.testing.FakeFavoriteRepository
import id.renaldi.pokedex.core.testing.FakePokemonRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteUseCasesTest {

    @Test
    fun `ObserveFavoriteIds forwards the stored ids`() = runTest {
        val observe = ObserveFavoriteIds(FakeFavoriteRepository(setOf(1, 25)))

        assertEquals(setOf(1, 25), observe().first())
    }

    @Test
    fun `ToggleFavorite delegates to the repository`() = runTest {
        val repository = FakeFavoriteRepository()
        val toggle = ToggleFavorite(repository)

        toggle(25)

        assertEquals(listOf(25), repository.toggled)
        assertEquals(setOf(25), repository.current)
    }

    @Test
    fun `ObserveFavoritePokemon keeps only favourited pokemon in national order`() = runTest {
        val observe = ObserveFavoritePokemon(
            FakePokemonRepository(SamplePokemon.all),
            FakeFavoriteRepository(setOf(26, 1)),
        )

        assertEquals(listOf(1, 26), observe().first().map { it.id })
    }

    @Test
    fun `ObserveFavoritePokemon yields nothing when nothing is favourited`() = runTest {
        val observe = ObserveFavoritePokemon(
            FakePokemonRepository(SamplePokemon.all),
            FakeFavoriteRepository(),
        )

        assertEquals(emptyList<Any>(), observe().first())
    }

    @Test
    fun `ObserveFavoritePokemon ignores ids the pokedex does not know`() = runTest {
        val observe = ObserveFavoritePokemon(
            FakePokemonRepository(listOf(SamplePokemon.pikachu)),
            FakeFavoriteRepository(setOf(25, 999)),
        )

        assertEquals(listOf(25), observe().first().map { it.id })
    }
}
