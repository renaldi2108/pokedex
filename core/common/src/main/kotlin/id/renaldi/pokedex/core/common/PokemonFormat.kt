package id.renaldi.pokedex.core.common

object PokemonFormat {

    private const val ID_DIGITS = 3
    private const val ARTWORK_BASE =
        "https://assets.pokemon.com/assets/cms2/img/pokedex/full"
    private const val FORM_FEED = '\u000C'

    fun paddedId(id: Int): String = id.toString().padStart(ID_DIGITS, '0')

    fun displayId(id: Int): String = "#${paddedId(id)}"

    fun artworkUrl(id: Int): String = "$ARTWORK_BASE/${paddedId(id)}.png"

    fun displayName(raw: String): String = raw.replaceFirstChar { it.uppercase() }

    fun height(meters: Double): String = "${oneDecimal(meters)} m"

    fun weight(kilograms: Double): String = "${oneDecimal(kilograms)} kg"

    fun flavorText(text: String, pokemonName: String = ""): String {
        val cleaned = text
            .replace('\n', ' ')
            .replace(FORM_FEED, ' ')
            .replace("POKéMON", "pokemon")
        return if (pokemonName.isBlank()) {
            cleaned
        } else {
            cleaned.replace(pokemonName.uppercase(), displayName(pokemonName))
        }
    }

    private fun oneDecimal(value: Double): String {
        val scaled = (value * 10).toLong()
        return "${scaled / 10}.${scaled % 10}"
    }
}
