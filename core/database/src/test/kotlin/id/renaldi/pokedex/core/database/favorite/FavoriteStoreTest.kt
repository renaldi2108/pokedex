package id.renaldi.pokedex.core.database.favorite

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
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

class FavoriteStoreTest {

    private val dataStore = FakePreferencesDataStore()
    private val store = FavoriteStore(dataStore)

    @Test
    fun `an untouched store observes no favorites`() = runTest {
        assertEquals(emptySet<Int>(), store.observeIds().first())
    }

    @Test
    fun `toggling an id adds it and toggling again removes it`() = runTest {
        store.toggle(25)
        assertEquals(setOf(25), store.observeIds().first())

        store.toggle(1)
        assertEquals(setOf(1, 25), store.observeIds().first())

        store.toggle(25)
        assertEquals(setOf(1), store.observeIds().first())
    }
}
