package id.renaldi.pokedex.ui.common.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import id.renaldi.pokedex.R
import id.renaldi.pokedex.ui.common.theme.color.*
import id.renaldi.pokedex.ui.common.theme.color.ColorScheme

private val defaultTypography = Typography()

val Typography = Typography(
    displaySmall = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 36.sp,
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 30.sp,
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 24.sp,
    ),
    titleLarge = defaultTypography.titleLarge,
    titleMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
    bodyLarge = defaultTypography.bodyLarge,
    bodyMedium = defaultTypography.bodyMedium,
    bodySmall = defaultTypography.bodySmall,
)

enum class Type {
    Normal,
    Fire,
    Fighting,
    Water,
    Flying,
    Grass,
    Poison,
    Electric,
    Ground,
    Psychic,
    Rock,
    Ice,
    Bug,
    Dragon,
    Ghost,
    Dark,
    Steel,
    Fairy,
}

object TypeTheme {
    val colorScheme: ColorScheme
        @Composable
        get() = LocalColorScheme.current
}

fun typeToColor(type: String): Color {
    return when (Type.valueOf(type)) {
        Type.Grass -> PokemonColors.Grass
        Type.Bug -> PokemonColors.Bug
        Type.Fairy -> PokemonColors.Fairy
        Type.Fire -> PokemonColors.Fire
        Type.Flying -> PokemonColors.Flying
        Type.Water -> PokemonColors.Water
        Type.Ice -> PokemonColors.Ice
        Type.Dragon -> PokemonColors.Dragon
        Type.Normal -> PokemonColors.Normal
        Type.Fighting -> PokemonColors.Fighting
        Type.Electric -> PokemonColors.Electric
        Type.Psychic -> PokemonColors.Psychic
        Type.Poison -> PokemonColors.Poison
        Type.Ghost -> PokemonColors.Ghost
        Type.Ground -> PokemonColors.Ground
        Type.Rock -> PokemonColors.Rock
        Type.Dark -> PokemonColors.Dark
        else -> return Color.Magenta
    }
}

@Composable
fun typeColorToScheme(
    types: String,
    isDark: Boolean
): ColorScheme = when(Type.valueOf(types)) {
    Type.Bug -> ColorScheme(
        primary = if(!isDark) BugTypeColors.primaryLight else BugTypeColors.primaryDark,
        surface = if(!isDark) BugTypeColors.primaryLight else BugTypeColors.surfaceDark,
        onSurface = if(!isDark) BugTypeColors.onSurfaceLight else BugTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) BugTypeColors.surfaceVariantLight else BugTypeColors.surfaceVariantDark,
    )
    Type.Dark -> ColorScheme(
        primary = if(!isDark) DarkTypeColors.primaryLight else DarkTypeColors.primaryDark,
        surface = if(!isDark) DarkTypeColors.primaryLight else DarkTypeColors.surfaceDark,
        onSurface = if(!isDark) Color.White else DarkTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) DarkTypeColors.surfaceVariantLight else DarkTypeColors.surfaceVariantDark,
    )
    Type.Dragon -> ColorScheme(
        primary = if(!isDark) DragonTypeColors.primaryLight else DragonTypeColors.primaryDark,
        surface = if(!isDark) DragonTypeColors.primaryLight else DragonTypeColors.surfaceDark,
        onSurface = if(!isDark) Color.White else DragonTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) DragonTypeColors.surfaceVariantLight else DragonTypeColors.surfaceVariantDark,
    )
    Type.Electric -> ColorScheme(
        primary = if(!isDark) ElectricTypeColors.primaryLight else ElectricTypeColors.primaryDark,
        surface = if(!isDark) ElectricTypeColors.primaryLight else ElectricTypeColors.surfaceDark,
        onSurface = if(!isDark) ElectricTypeColors.onSurfaceLight else ElectricTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) ElectricTypeColors.surfaceVariantLight else ElectricTypeColors.surfaceVariantDark,
    )
    Type.Fairy -> ColorScheme(
        primary = if(!isDark) FairyTypeColors.primaryLight else FairyTypeColors.primaryDark,
        surface = if(!isDark) FairyTypeColors.primaryLight else FairyTypeColors.surfaceDark,
        onSurface = if(!isDark) FairyTypeColors.onSurfaceLight else FairyTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) FairyTypeColors.surfaceVariantLight else FairyTypeColors.surfaceVariantDark,
    )
    Type.Fighting -> ColorScheme(
        primary = if(!isDark) FightingTypeColors.primaryLight else FightingTypeColors.primaryDark,
        surface = if(!isDark) FightingTypeColors.primaryLight else FightingTypeColors.surfaceDark,
        onSurface = if(!isDark) Color.White else FightingTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) FightingTypeColors.surfaceVariantLight else FightingTypeColors.surfaceVariantDark,
    )
    Type.Fire -> ColorScheme(
        primary = if(!isDark) FireTypeColors.primaryLight else FireTypeColors.primaryDark,
        surface = if(!isDark) FireTypeColors.primaryLight else FireTypeColors.surfaceDark,
        onSurface = if(!isDark) Color.White else FireTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) FireTypeColors.surfaceVariantLight else FireTypeColors.surfaceVariantDark,
    )
    Type.Flying -> ColorScheme(
        primary = if(!isDark) FlyingTypeColors.primaryLight else FlyingTypeColors.primaryDark,
        surface = if(!isDark) FlyingTypeColors.primaryLight else FlyingTypeColors.surfaceDark,
        onSurface = if(!isDark) Color.White else FlyingTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) FlyingTypeColors.surfaceVariantLight else FlyingTypeColors.surfaceVariantDark,
    )
    Type.Ghost -> ColorScheme(
        primary = if(!isDark) GhostTypeColors.primaryLight else GhostTypeColors.primaryDark,
        surface = if(!isDark) GhostTypeColors.primaryLight else GhostTypeColors.surfaceDark,
        onSurface = if(!isDark) Color.White else GhostTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) GhostTypeColors.surfaceVariantLight else GhostTypeColors.surfaceVariantDark,
    )
    Type.Grass -> ColorScheme(
        primary = if(!isDark) GrassTypeColors.primaryLight else GrassTypeColors.primaryDark,
        surface = if(!isDark) GrassTypeColors.primaryLight else GrassTypeColors.surfaceDark,
        onSurface = if(!isDark) Color.White else GrassTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) GrassTypeColors.surfaceVariantLight else GrassTypeColors.surfaceVariantDark,
    )
    Type.Ground -> ColorScheme(
        primary = if(!isDark) GroundTypeColors.primaryLight else GroundTypeColors.primaryDark,
        surface = if(!isDark) GroundTypeColors.primaryLight else GroundTypeColors.surfaceDark,
        onSurface = if(!isDark) Color.White else GroundTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) GroundTypeColors.surfaceVariantLight else GroundTypeColors.surfaceVariantDark,
    )
    Type.Ice -> ColorScheme(
        primary = if(!isDark) IceTypeColors.primaryLight else IceTypeColors.primaryDark,
        surface = if(!isDark) IceTypeColors.primaryLight else IceTypeColors.surfaceDark,
        onSurface = if(!isDark) IceTypeColors.onSurfaceLight else IceTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) IceTypeColors.surfaceVariantLight else IceTypeColors.surfaceVariantDark,
    )
    Type.Normal -> ColorScheme(
        primary = if(!isDark) NormalTypeColors.primaryLight else NormalTypeColors.primaryDark,
        surface = if(!isDark) NormalTypeColors.primaryLight else NormalTypeColors.surfaceDark,
        onSurface = if(!isDark) NormalTypeColors.onSurfaceLight else NormalTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) NormalTypeColors.surfaceVariantLight else NormalTypeColors.surfaceVariantDark,
    )
    Type.Poison -> ColorScheme(
        primary = if(!isDark) PoisonTypeColors.primaryLight else PoisonTypeColors.primaryDark,
        surface = if(!isDark) PoisonTypeColors.primaryLight else PoisonTypeColors.surfaceDark,
        onSurface = if(!isDark) Color.White else PoisonTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) PoisonTypeColors.surfaceVariantLight else PoisonTypeColors.surfaceVariantDark,
    )
    Type.Psychic -> ColorScheme(
        primary = if(!isDark) PsychicTypeColors.primaryLight else PsychicTypeColors.primaryDark,
        surface = if(!isDark) PsychicTypeColors.primaryLight else PsychicTypeColors.surfaceDark,
        onSurface = if(!isDark) Color.White else PsychicTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) PsychicTypeColors.surfaceVariantLight else PsychicTypeColors.surfaceVariantDark,
    )
    Type.Rock -> ColorScheme(
        primary = if(!isDark) RockTypeColors.primaryLight else RockTypeColors.primaryDark,
        surface = if(!isDark) RockTypeColors.primaryLight else RockTypeColors.surfaceDark,
        onSurface = if(!isDark) RockTypeColors.onSurfaceLight else RockTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) RockTypeColors.surfaceVariantLight else RockTypeColors.surfaceVariantDark,
    )
    Type.Water -> ColorScheme(
        primary = if(!isDark) WaterTypeColors.primaryLight else WaterTypeColors.primaryDark,
        surface = if(!isDark) WaterTypeColors.primaryLight else WaterTypeColors.surfaceDark,
        onSurface = if(!isDark) Color.White else WaterTypeColors.onSurfaceDark,
        surfaceVariant = if(!isDark) WaterTypeColors.surfaceVariantLight else WaterTypeColors.surfaceVariantDark,
    )
    else -> LocalColorScheme.current
}