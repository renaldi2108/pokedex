package id.renaldi.pokedex.data.model

import id.renaldi.pokedex.R

fun placeholderPokemonImage(id: Int): Int {
    val sampleImages = listOf(
        R.drawable.poke001,
        R.drawable.poke002,
        R.drawable.poke003,
        R.drawable.poke004,
        R.drawable.poke005,
        R.drawable.poke006,
        R.drawable.poke007,
        R.drawable.poke008,
        R.drawable.poke009,
        R.drawable.poke010,
        R.drawable.poke011,
        R.drawable.poke012,
        R.drawable.poke013,
        R.drawable.poke014,
        R.drawable.poke015,
        R.drawable.poke016,
        R.drawable.poke017,
        R.drawable.poke018,
        R.drawable.poke019,
        R.drawable.poke020,
        R.drawable.poke021,
        R.drawable.poke022,
        R.drawable.poke023,
        R.drawable.poke024,
        R.drawable.poke025,
        R.drawable.poke026,
    )
    return sampleImages[Integer.min(id, sampleImages.size) - 1]
}