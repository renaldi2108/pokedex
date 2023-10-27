package id.renaldi.pokedex.ui.screen.home.appBar

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.renaldi.pokedex.ui.common.poke.PokeBallBackground
import id.renaldi.pokedex.ui.screen.home.appBar.component.RoundedSearchBar
import kotlinx.coroutines.delay

@Composable
fun AppBar(
    completedExpansion: (Boolean) -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(
            bottomStart = 32.dp,
            bottomEnd = 32.dp
        ),
        tonalElevation = if (isSystemInDarkTheme()) 2.dp else 0.dp
    ) {
        Box {
            val isNeedExpansion = rememberSaveable{ mutableStateOf(true) }
            completedExpansion(isNeedExpansion.value)

            val animatedSizeDp: Dp by animateDpAsState(targetValue = if (isNeedExpansion.value) 960.dp else 240.dp,
                label = ""
            )
            LaunchedEffect("PokeBallBackground") {
                delay(700)
                isNeedExpansion.value = false
                completedExpansion(isNeedExpansion.value)
            }

            PokeBallBackground(
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 90.dp, y = (-70).dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                size = animatedSizeDp
            )
            Column(
                modifier = Modifier.padding(32.dp)
            ) {
                Text(
                    text = "What Pokémon\nare you looking for?",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(
                        top = 64.dp, bottom = 32.dp
                    )
                )
                RoundedSearchBar()
            }
        }
    }
}