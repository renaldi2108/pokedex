package id.renaldi.pokedex.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import id.renaldi.pokedex.R
import id.renaldi.pokedex.ui.common.theme.Type

@Entity
data class Move(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val type: String,
    val pp: Int,
    val power: Int?,
    val accuracy: Int?
)

fun Move.category(): MoveCategory {
    return when(category) {
        "physical" -> MoveCategory.Physical
        "special" -> MoveCategory.Special
        else -> MoveCategory.Status
    }
}

fun Move.type(): Type {
    return when(type) {
        else -> Type.Normal
    }
}

fun Move.categoryIcon(): Int {
    return when(category.lowercase()) {
        "physical" -> R.drawable.ic_move_physical
        "special" -> R.drawable.ic_move_special
        else -> R.drawable.ic_move_status
    }
}

enum class MoveCategory {
    Physical,
    Special,
    Status
}

data class PokemonMove(
    val id: Int,
    val targetLevel: Int
)