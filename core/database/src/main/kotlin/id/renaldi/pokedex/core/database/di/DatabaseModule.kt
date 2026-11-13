package id.renaldi.pokedex.core.database.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import id.renaldi.pokedex.core.database.PokedexDatabase
import id.renaldi.pokedex.core.database.dao.PokemonDao
import id.renaldi.pokedex.core.database.favorite.FavoriteStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.dsl.module

private const val PREFERENCES_NAME = "user_preferences"

val databaseModule: Module = module {
    single {
        Room.databaseBuilder(get(), PokedexDatabase::class.java, PokedexDatabase.NAME)
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    single<PokemonDao> { get<PokedexDatabase>().pokemonDao() }
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { get<android.content.Context>().preferencesDataStoreFile(PREFERENCES_NAME) },
        )
    }
    single { FavoriteStore(get()) }
}
