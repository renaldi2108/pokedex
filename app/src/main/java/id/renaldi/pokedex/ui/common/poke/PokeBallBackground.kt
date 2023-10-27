package id.renaldi.pokedex.ui.common.poke

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.renaldi.pokedex.ui.common.theme.color.Grey100

@Composable
fun PokeBallBackground(
    modifier: Modifier = Modifier,
    tint: Color = Grey100,
    size: Dp = 240.dp
) {
    Box(
        modifier.size(size),
    ) {
        PokeBallLarge(tint = tint)
    }
}