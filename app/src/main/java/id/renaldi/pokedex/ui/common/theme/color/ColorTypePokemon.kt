package id.renaldi.pokedex.ui.common.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class TypeColors(
    val primaryDark: Color,
    val primaryLight: Color,
    val surfaceDark: Color,
    val surfaceLight: Color = Color.Unspecified,
    val onSurfaceDark: Color,
    val onSurfaceLight: Color = Color.Unspecified,
    val surfaceVariantDark: Color,
    val surfaceVariantLight: Color
)

val BugTypeColors = TypeColors(
    primaryDark = Color(0xffBFD039),
    primaryLight = Color(0xff5A6400),
    surfaceDark = Color(0xff434B00),
    surfaceLight = PokemonColors.Bug,
    onSurfaceDark = Color(0xffF8FFB9),
    onSurfaceLight = Color(0xff5A6400),
    surfaceVariantDark = Color(0x661A1E00),
    surfaceVariantLight = Color(0x26002019),
)

val DarkTypeColors = TypeColors(
    primaryDark = Color(0xffFFB691),
    primaryLight = PokemonColors.Dark,
    surfaceDark = Color(0xff793100),
    onSurfaceDark = Color(0xffFFDBCB),
    surfaceVariantDark = Color(0x66341100),
    surfaceVariantLight = Color(0x26341100),
)

val DragonTypeColors = TypeColors(
    primaryDark = Color(0xffC7BFFF),
    primaryLight = PokemonColors.Dragon,
    surfaceDark = Color(0xff422AB7),
    onSurfaceDark = Color(0xffE4DFFF),
    surfaceVariantDark = Color(0x66180065),
    surfaceVariantLight = Color(0x26180065),
)

val ElectricTypeColors = TypeColors(
    primaryDark = Color(0xffF0C03E),
    primaryLight = PokemonColors.Electric,
    surfaceDark = Color(0xFFB48B00),
    onSurfaceDark = Color(0xffFFF8F1),
    onSurfaceLight = Color(0xff4C3900),
    surfaceVariantDark = Color(0xA8251A00),
    surfaceVariantLight = Color(0x26251A00),
)


val FairyTypeColors = TypeColors(
    primaryDark = Color(0xffC473C5),
    primaryLight = Color(0xffE18EE2),
    surfaceDark = Color(0xff702875),
    onSurfaceDark = Color(0xffFFD6FA),
    onSurfaceLight = Color(0xff631B69),
    surfaceVariantDark = Color(0x6636003C),
    surfaceVariantLight = Color(0x4036003C),
)

val FightingTypeColors = TypeColors(
    primaryDark = Color(0xffFFB4A7),
    primaryLight = PokemonColors.Fighting,
    surfaceDark = Color(0xff80291C),
    onSurfaceDark = Color(0xffFFDAD4),
    surfaceVariantDark = Color(0x662B1B1A),
    surfaceVariantLight = Color(0x402B1B1A),
)

val FireTypeColors = TypeColors(
    primaryDark = Color(0xffE3300E),
    primaryLight = PokemonColors.Fire,
    surfaceDark = Color(0xff8E1400),
    onSurfaceDark = Color(0xffFFDAD3),
    surfaceVariantDark = Color(0x663E0400),
    surfaceVariantLight = Color(0x263E0400),
)

val FlyingTypeColors = TypeColors(
    primaryDark = Color(0xffBAC3FF),
    primaryLight = PokemonColors.Flying,
    surfaceDark = Color(0xff2A3C9E),
    onSurfaceDark = Color(0xffDEE0FF),
    surfaceVariantDark = Color(0x6600105C),
    surfaceVariantLight = Color(0x2600105C),
)

val GhostTypeColors = TypeColors(
    primaryDark = Color(0xffECB2FF),
    primaryLight = PokemonColors.Ghost,
    surfaceDark = Color(0xff6A2885),
    onSurfaceDark = Color(0xffF8D8FF),
    surfaceVariantDark = Color(0x66320046),
    surfaceVariantLight = Color(0x26320046),
)

val GrassTypeColors = TypeColors(
    primaryDark = Color(0xff00876F),
    primaryLight = PokemonColors.Grass,
    surfaceDark = Color(0xff005141),
    onSurfaceDark = Color(0xffE6FFF5),
    surfaceVariantDark = Color(0x66002019),
    surfaceVariantLight = Color(0x26002019),
)

val GroundTypeColors = TypeColors(
    primaryDark = Color(0xffEAC248),
    primaryLight = PokemonColors.Ground,
    surfaceDark = Color(0xff574500),
    onSurfaceDark = Color(0xffFFEFCC),
    surfaceVariantDark = Color(0x66241A00),
    surfaceVariantLight = Color(0x40241A00),
)

val IceTypeColors = TypeColors(
    primaryDark = Color(0xff79D1FF),
    primaryLight = Color(0xff4AB6E8),
    surfaceDark = Color(0xff004C68),
    onSurfaceDark = Color(0xffC3E8FF),
    onSurfaceLight = Color(0xff004C68),
    surfaceVariantDark = Color(0x66001E2C),
    surfaceVariantLight = Color(0x40001E2C),
)

val NormalTypeColors = TypeColors(
    primaryDark = Color(0xffC7C9A7),
    primaryLight = Color(0xff2F321A),
    surfaceDark = Color(0xff47483B),
    surfaceLight = PokemonColors.Normal,
    onSurfaceDark = Color(0xffC8C7B7),
    onSurfaceLight = Color(0xFF39400A),
    surfaceVariantDark = Color(0x66181B03),
    surfaceVariantLight = Color(0x40191C03),
)

val PoisonTypeColors = TypeColors(
    primaryDark = Color(0xffFFACE9),
    primaryLight = PokemonColors.Poison,
    surfaceDark = Color(0xff752769),
    onSurfaceDark = Color(0xffFFD7F1),
    surfaceVariantDark = Color(0x66390033),
    surfaceVariantLight = Color(0x26390033),
)

val PsychicTypeColors = TypeColors(
    primaryDark = Color(0xffF95095),
    primaryLight = PokemonColors.Psychic,
    surfaceDark = Color(0xff8E0049),
    onSurfaceDark = Color(0xffFFD9E2),
    surfaceVariantDark = Color(0x663E001D),
    surfaceVariantLight = Color(0x403E001D),
)

val RockTypeColors = TypeColors(
    primaryDark = Color(0xffE1C64B),
    primaryLight = PokemonColors.Rock,
    surfaceDark = Color(0xff534600),
    onSurfaceDark = Color(0xffFFF0BF),
    onSurfaceLight = Color(0xff463B00),
    surfaceVariantDark = Color(0x66221B00),
    surfaceVariantLight = Color(0x26221B00),
)

val WaterTypeColors = TypeColors(
    primaryDark = Color(0xff037BCB),
    primaryLight = PokemonColors.Water,
    surfaceDark = Color(0xff00497C),
    onSurfaceDark = Color(0xffE9F1FF),
    surfaceVariantDark = Color(0x66001D36),
    surfaceVariantLight = Color(0x33001D36),
)