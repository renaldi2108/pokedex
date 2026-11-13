package id.renaldi.pokedex.core.database.favorite

import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteIdsTest {

    @Test
    fun `encode writes a sorted pipe separated list`() {
        assertEquals("1|25|151", FavoriteIds.encode(setOf(151, 1, 25)))
        assertEquals("", FavoriteIds.encode(emptySet()))
    }

    @Test
    fun `decode parses a stored list`() {
        assertEquals(setOf(1, 25, 151), FavoriteIds.decode("1|25|151"))
    }

    @Test
    fun `decode treats null blank and malformed input as empty`() {
        assertEquals(emptySet<Int>(), FavoriteIds.decode(null))
        assertEquals(emptySet<Int>(), FavoriteIds.decode(""))
        assertEquals(emptySet<Int>(), FavoriteIds.decode("||"))
        assertEquals(setOf(7), FavoriteIds.decode("7|abc"))
    }

    @Test
    fun `toggle adds a missing id and removes a present one`() {
        assertEquals(setOf(1), FavoriteIds.toggle(emptySet(), 1))
        assertEquals(emptySet<Int>(), FavoriteIds.toggle(setOf(1), 1))
        assertEquals(setOf(1, 2), FavoriteIds.toggle(setOf(1), 2))
    }
}
