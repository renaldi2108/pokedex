package id.renaldi.pokedex.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import id.renaldi.pokedex.ui.common.theme.color.LocalColorScheme
import id.renaldi.pokedex.ui.common.theme.color.M3DarkColors
import id.renaldi.pokedex.ui.common.theme.color.M3LightColors

private val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(16.dp)
)

@Composable
fun PokedexTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!isDarkTheme) {
        M3LightColors
    } else {
        M3DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun PokemonMenuTheme(
    types: String,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val extendedTypesColors = typeColorToScheme(types = types, isDark = useDarkTheme)

    CompositionLocalProvider(
        LocalColorScheme provides extendedTypesColors,
        LocalContentColor provides extendedTypesColors.onSurface
    ) {
        content()
    }
}