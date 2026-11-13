package id.renaldi.pokedex.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    @ColumnInfo(name = "types") val types: String,
    val category: String,
    val height: Double,
    val weight: Double,
    val genderRate: Int,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int,
    @ColumnInfo(name = "evolutions") val evolutions: String,
    @ColumnInfo(name = "moves") val moves: String,
)
