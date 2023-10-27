package id.renaldi.pokedex.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import id.renaldi.pokedex.data.db.dao.PokemonDao
import id.renaldi.pokedex.data.model.Evolution
import id.renaldi.pokedex.data.model.EvolutionTrigger
import id.renaldi.pokedex.data.model.Pokemon
import id.renaldi.pokedex.data.model.PokemonMove

@Database(
    version = 2,
    entities = [Pokemon::class],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ],
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class PokemonDatabase: RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}

class Converters {
    @TypeConverter
    fun stringToList(str: String?): List<String>? {
        return str?.split(",")
    }

    @TypeConverter
    fun listToString(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun stringToEvolutionList(str: String): List<Evolution> {
        val list = mutableListOf<Evolution>()

        if (str.isNotBlank()) {
            str.split("|").map {
                val evo = it.split(",")
                list.add(
                    Evolution(
                        id = evo[0].toInt(),
                        targetLevel = evo[1].toInt(),
                        trigger = EvolutionTrigger.fromInt(evo[2].toInt()),
                        itemId = evo[3].toInt()
                    ))
            }
        }

        return list.toList()
    }

    @TypeConverter
    fun evolutionListToString(list: List<Evolution>): String {
        return list.joinToString(separator = "|") {
            val data = listOf(it.id, it.targetLevel, it.trigger.value, it.itemId)
            data.joinToString(",")
        }
    }

    @TypeConverter
    fun stringToPokemonMoveList(str: String): List<PokemonMove> {
        val list = mutableListOf<PokemonMove>()

        if (str.isNotBlank()) {
            str.split("|").map {
                val move = it.split(",")
                list.add(PokemonMove(move[0].toInt(), move[1].toInt()))
            }
        }

        return list.toList()
    }

    @TypeConverter
    fun pokemonMoveListToString(list: List<PokemonMove>): String {
        return list.joinToString(separator = "|") {
            val data = listOf(it.id, it.targetLevel)
            data.joinToString(",")
        }
    }
}