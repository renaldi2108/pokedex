package id.renaldi.pokedex.feature.favorite.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import id.renaldi.pokedex.core.database.favorite.FavoriteStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

private class FakePreferencesDataStore : DataStore<Preferences> {

    private val state = MutableStateFlow(emptyPreferences())

    override val data: Flow<Preferences> = state

    override suspend fun updateData(
        transform: suspend (Preferences) -> Preferences,
    ): Preferences {
        val next = transform(state.value)
        state.value = next
        return next
    }
}

class DataStoreFavoriteRepositoryTest {

    private val repository =
        DataStoreFavoriteRepository(FavoriteStore(FakePreferencesDataStore()))

    @Test
    fun `a fresh repository holds no favorites`() = runTest {
        assertEquals(emptySet<Int>(), repository.observeIds().first())
    }

    @Test
    fun `toggling writes through to the store and back`() = runTest {
        repository.toggle(25)
        assertEquals(setOf(25), repository.observeIds().first())

        repository.toggle(25)
        assertEquals(emptySet<Int>(), repository.observeIds().first())
    }
}
