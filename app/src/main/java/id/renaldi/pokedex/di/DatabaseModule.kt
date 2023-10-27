package id.renaldi.pokedex.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.renaldi.pokedex.data.db.PokemonDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): PokemonDatabase {
        return Room
            .databaseBuilder(
                appContext,
                PokemonDatabase::class.java,
                "pokemon.db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}