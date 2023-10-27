package id.renaldi.pokedex.ui.common.poke

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import id.renaldi.pokedex.R

@Composable
fun PokeBallLarge(tint: Color = Color.Black, alpha: Float = 1f) {
    PokeBallBase(tint = tint, alpha = alpha, imageResId = R.drawable.pokeball)
}

@Composable
fun PokeBallSmall(tint: Color = Color.Black, alpha: Float = 1f) {
    PokeBallBase(tint = tint, alpha = alpha, imageResId = R.drawable.pokeball_s)
}

@Composable
fun PokeBall(
    modifier: Modifier = Modifier,
    tint: Color = Color.Black,
    alpha: Float = 1f,
) {
    PokeBallBase(modifier = modifier, tint = tint, alpha = alpha)
}

@Composable
private fun PokeBallBase(
    modifier: Modifier = Modifier,
    tint: Color = Color.Black,
    alpha: Float = 1f,
    imageResId: Int = R.drawable.pokeball,
) {
    Image(
        modifier = modifier,
        painter = painterResource(id = imageResId),
        contentDescription = "PokeBall",
        alpha = alpha,
        colorFilter = ColorFilter.tint(tint)
    )
}