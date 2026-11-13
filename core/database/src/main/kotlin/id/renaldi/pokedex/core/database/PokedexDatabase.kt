package id.renaldi.pokedex.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.renaldi.pokedex.core.database.dao.PokemonDao
import id.renaldi.pokedex.core.database.entity.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 1, exportSchema = true)
abstract class PokedexDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao

    companion object {
        const val NAME: String = "pokedex.db"
    }
}
