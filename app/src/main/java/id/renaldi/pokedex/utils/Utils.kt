package id.renaldi.pokedex.utils

fun formatId(id: Int): String = "#" + "$id".padStart(3, '0')

fun artworkUrl(id: Int): String = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/${id.toString().padStart(3, '0')}.png"

fun itemAssetsUri(name: String): String = assetsUri("items", "$name.webp")
private fun assetsUri(
    subDirectory: String? = null,
    name: String
): String {
    val baseUri = "file:///android_asset"
    return subDirectory?.let {
        "$baseUri/$subDirectory/$name"
    } ?: run {
        "$baseUri/$name"
    }
}

fun formatFlavorText(
    text: String,
): String {
    return text
        .replace("\n", " ")
        .replace("\u000c", " ")
        .replace("POKéMON", "pokemon")
}