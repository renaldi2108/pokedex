package id.renaldi.pokedex.core.designsystem.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import id.renaldi.pokedex.core.model.PokemonType

@Immutable
data class PokemonTypePalette(
    val seed: Color,
    val container: Color,
    val onContainer: Color,
    val accent: Color,
    val onSeed: Color,
) {
    val prefersDarkIcons: Boolean get() = onSeed.luminance() < ICON_LUMINANCE_PIVOT
}

private const val LIGHT_CONTAINER_MIX = 0.82f
private const val LIGHT_ON_CONTAINER_MIX = 0.62f
private const val LIGHT_ACCENT_MIX = 0.24f
private const val DARK_CONTAINER_MIX = 0.68f
private const val DARK_ON_CONTAINER_MIX = 0.74f
private const val DARK_ACCENT_MIX = 0.30f
private const val LUMINANCE_PIVOT = 0.42f
private const val ICON_LUMINANCE_PIVOT = 0.5f

private val TYPE_SEEDS: Map<PokemonType, Color> = mapOf(
    PokemonType.Normal to Color(0xFFAAAA99),
    PokemonType.Fire to Color(0xFFFF4422),
    PokemonType.Fighting to Color(0xFFBB5544),
    PokemonType.Water to Color(0xFF429BED),
    PokemonType.Flying to Color(0xFF8899FF),
    PokemonType.Grass to Color(0xFF4FC1A6),
    PokemonType.Poison to Color(0xFFAA5599),
    PokemonType.Electric to Color(0xFFF0C03E),
    PokemonType.Ground to Color(0xFFCC9966),
    PokemonType.Psychic to Color(0xFFFF5599),
    PokemonType.Rock to Color(0xFFBBAA66),
    PokemonType.Ice to Color(0xFF66CCFF),
    PokemonType.Bug to Color(0xFFAABB22),
    PokemonType.Dragon to Color(0xFF7766EE),
    PokemonType.Ghost to Color(0xFF9F5BBA),
    PokemonType.Dark to Color(0xFF775544),
    PokemonType.Steel to Color(0xFF8899AA),
    PokemonType.Fairy to Color(0xFFEE99EE),
)

fun pokemonTypeSeed(type: PokemonType): Color = TYPE_SEEDS.getValue(type)

fun pokemonTypePalette(type: PokemonType, dark: Boolean): PokemonTypePalette {
    val seed = pokemonTypeSeed(type)
    return if (dark) {
        PokemonTypePalette(
            seed = seed,
            container = seed.towards(Color.Black, DARK_CONTAINER_MIX),
            onContainer = seed.towards(Color.White, DARK_ON_CONTAINER_MIX),
            accent = seed.towards(Color.White, DARK_ACCENT_MIX),
            onSeed = seed.readableOn(),
        )
    } else {
        PokemonTypePalette(
            seed = seed,
            container = seed.towards(Color.White, LIGHT_CONTAINER_MIX),
            onContainer = seed.towards(Color.Black, LIGHT_ON_CONTAINER_MIX),
            accent = seed.towards(Color.Black, LIGHT_ACCENT_MIX),
            onSeed = seed.readableOn(),
        )
    }
}

fun pokemonTypeContainerBlend(types: Collection<PokemonType>, dark: Boolean): Color? {
    if (types.isEmpty()) return null
    return types
        .map { pokemonTypePalette(it, dark).container }
        .reduceIndexed { index, blended, next -> lerp(blended, next, 1f / (index + 1)) }
}

fun pokemonTypeSeedBlend(types: Collection<PokemonType>, dark: Boolean): Color? {
    if (types.isEmpty()) return null
    return types
        .map { pokemonTypePalette(it, dark).seed }
        .reduceIndexed { index, blended, next -> lerp(blended, next, 1f / (index + 1)) }
}

fun Color.towards(target: Color, fraction: Float): Color = lerp(this, target, fraction)

fun Color.readableOn(): Color =
    if (luminance() > LUMINANCE_PIVOT) Color(0xFF14131A) else Color.White
