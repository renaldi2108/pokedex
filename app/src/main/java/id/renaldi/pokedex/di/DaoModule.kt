package id.renaldi.pokedex.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.renaldi.pokedex.data.db.PokemonDatabase
import id.renaldi.pokedex.data.db.dao.PokemonDao

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {
    @Provides
    fun providePokemonDao(database: PokemonDatabase): PokemonDao {
        return database.pokemonDao()
    }
}