package id.renaldi.pokedex.core.database.favorite

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteStore(private val dataStore: DataStore<Preferences>) {

    fun observeIds(): Flow<Set<Int>> = dataStore.data.map { preferences ->
        FavoriteIds.decode(preferences[KEY])
    }

    suspend fun toggle(id: Int) {
        dataStore.edit { preferences ->
            val current = FavoriteIds.decode(preferences[KEY])
            preferences[KEY] = FavoriteIds.encode(FavoriteIds.toggle(current, id))
        }
    }

    private companion object {
        val KEY: Preferences.Key<String> = stringPreferencesKey("favorites")
    }
}
