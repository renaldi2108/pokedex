package id.renaldi.pokedex.ui.screen.poke.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import id.renaldi.pokedex.data.model.Pokemon
import id.renaldi.pokedex.ui.common.TopNavBar
import id.renaldi.pokedex.ui.common.poke.PokeBallBackground
import id.renaldi.pokedex.ui.screen.poke.main.PokemonViewModel
import id.renaldi.pokedex.ui.screen.poke.main.component.PokemonList

@Composable
fun PokeFavScreenRoute(
    viewModel: PokemonViewModel,
    onBackClick: () -> Unit = {},
    onPokemonSelected: (Pokemon) -> Unit = {},
){
    viewModel.refresh()

    PokeFavScreen(
        favorites = viewModel.favorites,
        onBackClick = onBackClick,
        onPokemonSelected = onPokemonSelected
    )
}

@Composable
fun PokeFavScreen(
    favorites: List<Pokemon>,
    onBackClick: () -> Unit = {},
    onPokemonSelected: (Pokemon) -> Unit = {},
) {
    val listState = rememberLazyGridState()

    val topAppBarRevealThreshold = with(LocalDensity.current) { 64.dp.toPx() }
    val scrollOffset by remember {
        derivedStateOf {
            listState.firstVisibleItemScrollOffset
        }
    }

    val isGridAtTop by remember {
        derivedStateOf {
            scrollOffset < topAppBarRevealThreshold && listState.firstVisibleItemIndex == 0
        }
    }

    val topAppBarTitleRevealProgress by remember {
        derivedStateOf {
            if (isGridAtTop) {
                1f - (topAppBarRevealThreshold - scrollOffset) / topAppBarRevealThreshold
            } else {
                1f
            }
        }
    }

    val backgroundScrollThreshold = with(LocalDensity.current) { 40.dp.toPx() }
    val backgroundRevealProgress by remember {
        derivedStateOf {
            if (isGridAtTop) {
                (1f - (backgroundScrollThreshold - scrollOffset) / backgroundScrollThreshold).coerceIn(0f, 1f)
            } else {
                1f
            }
        }
    }

    Surface {
        Box(
            Modifier.fillMaxSize()
        ) {
            PokeBallBackground(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 90.dp, y = (-70).dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
            )
            PokemonList(
                modifier = Modifier.statusBarsPadding(),
                loading = false,
                title = {
                    Text(
                        text = "Favorite",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .padding(
                                top = 16.dp,
                                bottom = 24.dp
                            )
                            .graphicsLayer {
                                alpha = 1f - topAppBarTitleRevealProgress
                            }
                    )
                },
                pokemon = favorites,
                favorites = favorites,
                onPokemonSelected = onPokemonSelected,
                listState = listState
            )

            val navBarCollapsedColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)

            TopNavBar(
                modifier = Modifier
                    .drawBehind {
                        drawRect(color = navBarCollapsedColor, alpha = backgroundRevealProgress)
                    }
                    .statusBarsPadding()
                    .padding(top = 16.dp)
                ,
                title = {
                    Text(
                        text = "Favorite",
                        modifier = Modifier.graphicsLayer {
                            alpha = topAppBarTitleRevealProgress
                        }
                    )
                },
                onBackClick = onBackClick
            )
        }
    }
}