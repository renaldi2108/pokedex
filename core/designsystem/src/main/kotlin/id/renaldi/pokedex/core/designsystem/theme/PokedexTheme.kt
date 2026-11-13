package id.renaldi.pokedex.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.renaldi.pokedex.core.designsystem.color.PokemonTypePalette
import id.renaldi.pokedex.core.designsystem.color.pokemonTypePalette
import id.renaldi.pokedex.core.model.PokemonType

val LocalTypePalette = staticCompositionLocalOf {
    pokemonTypePalette(PokemonType.Normal, dark = false)
}

object PokedexTheme {

    val typePalette: PokemonTypePalette
        @Composable get() = LocalTypePalette.current
}

private val LightColors = lightColorScheme(
    primary = Color(0xFF3F4BB8),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDFE0FF),
    onPrimaryContainer = Color(0xFF00105C),
    secondary = Color(0xFF5B5D72),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0E0F9),
    onSecondaryContainer = Color(0xFF181A2C),
    tertiary = Color(0xFF8B4E6F),
    onTertiary = Color.White,
    background = Color(0xFFFBF8FF),
    onBackground = Color(0xFF191A22),
    surface = Color(0xFFFBF8FF),
    onSurface = Color(0xFF191A22),
    surfaceVariant = Color(0xFFE3E1EC),
    onSurfaceVariant = Color(0xFF46464F),
    surfaceContainer = Color(0xFFF0EEF9),
    surfaceContainerHigh = Color(0xFFEAE8F5),
    surfaceContainerHighest = Color(0xFFE4E2F1),
    outline = Color(0xFF777680),
    outlineVariant = Color(0xFFC7C5D0),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFBCC2FF),
    onPrimary = Color(0xFF0D1A7E),
    primaryContainer = Color(0xFF2A379E),
    onPrimaryContainer = Color(0xFFDFE0FF),
    secondary = Color(0xFFC4C5DD),
    onSecondary = Color(0xFF2D2F42),
    secondaryContainer = Color(0xFF434559),
    onSecondaryContainer = Color(0xFFE0E0F9),
    tertiary = Color(0xFFFFB0CF),
    onTertiary = Color(0xFF53233D),
    background = Color(0xFF111118),
    onBackground = Color(0xFFE4E1E9),
    surface = Color(0xFF111118),
    onSurface = Color(0xFFE4E1E9),
    surfaceVariant = Color(0xFF46464F),
    onSurfaceVariant = Color(0xFFC7C5D0),
    surfaceContainer = Color(0xFF1D1D26),
    surfaceContainerHigh = Color(0xFF282831),
    surfaceContainerHighest = Color(0xFF32323C),
    outline = Color(0xFF90909A),
    outlineVariant = Color(0xFF46464F),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

private val PokedexShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

private val base = Typography()

private val PokedexTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 46.sp,
        lineHeight = 50.sp,
        letterSpacing = (-1).sp,
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 38.sp,
        lineHeight = 42.sp,
        letterSpacing = (-0.8).sp,
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 32.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.5).sp,
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 19.sp,
        lineHeight = 24.sp,
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp,
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.2.sp,
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyLarge = base.bodyLarge,
    bodyMedium = base.bodyMedium,
    bodySmall = base.bodySmall,
)

@Composable
fun PokedexTheme(
    dark: Boolean = isSystemInDarkTheme(),
    type: PokemonType = PokemonType.Normal,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalTypePalette provides pokemonTypePalette(type, dark)) {
        MaterialTheme(
            colorScheme = if (dark) DarkColors else LightColors,
            typography = PokedexTypography,
            shapes = PokedexShapes,
            content = content,
        )
    }
}
