package id.renaldi.pokedex.core.database.favorite

private const val SEPARATOR = "|"

object FavoriteIds {

    fun encode(ids: Set<Int>): String = ids.sorted().joinToString(SEPARATOR)

    fun decode(raw: String?): Set<Int> {
        if (raw.isNullOrBlank()) return emptySet()
        return raw.split(SEPARATOR).mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun toggle(ids: Set<Int>, id: Int): Set<Int> =
        if (id in ids) ids - id else ids + id
}
