package id.renaldi.pokedex.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.renaldi.pokedex.data.db.dao.PokemonDao
import id.renaldi.pokedex.data.pref.UserPreferencesRepository
import id.renaldi.pokedex.data.repo.PokemonRepository
import id.renaldi.pokedex.data.repo.RemotePokemonRepository

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    fun providePokemonRepository(
        pokemonDao: PokemonDao,
        apolloClient: ApolloClient
    ): PokemonRepository {
        return RemotePokemonRepository(pokemonDao, apolloClient)
    }

    @Provides
    fun provideUserPreferencesRepository(
        dataStore: DataStore<Preferences>
    ): UserPreferencesRepository {
        return UserPreferencesRepository(dataStore)
    }
}