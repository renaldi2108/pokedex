package id.renaldi.pokedex.feature.favorite.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoriteRoutesTest {

    @Test
    fun `the favorites screen is a singleton destination`() {
        assertEquals(FavoriteRoute, FavoriteRoute)
        assertTrue(FavoriteRoute.toString().contains("FavoriteRoute"))
    }
}
