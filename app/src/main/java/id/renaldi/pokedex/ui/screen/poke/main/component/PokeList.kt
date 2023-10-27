package id.renaldi.pokedex.ui.screen.poke.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import id.renaldi.pokedex.data.model.Pokemon

@Composable
fun PokemonList(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    pokemon: List<Pokemon>,
    listState: LazyGridState,
    favorites: List<Pokemon>,
    title: (@Composable () -> Unit) ?= null,
    onPokemonSelected: (Pokemon) -> Unit = {},
) {
    val loaded = remember { MutableTransitionState(!loading) }

    LazyVerticalGrid(
        modifier = modifier.testTag("PokedexLazyGrid"),
        columns = GridCells.Fixed(2),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 15.dp, vertical = if(title!=null) 64.dp else 15.dp),
        content = {
            if(title!=null) {
                item(span = { GridItemSpan(2) }) {
                    title()
                }
            }

            if (loading) {
                item(span = { GridItemSpan(2) }) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(vertical = 24.dp)
                        )
                    }
                }
            } else {
                loaded.targetState = true

                itemsIndexed(pokemon) { idx, p ->
                    AnimatedVisibility(
                        visibleState = loaded,
                        enter = slideInVertically(
                            animationSpec = tween(
                                durationMillis = 500,
                                delayMillis = idx / 2 * 120
                            ),
                            initialOffsetY = { it / 2 }
                        ) + fadeIn(
                            animationSpec = tween(
                                durationMillis = 400,
                                delayMillis = idx / 2 * 150
                            ),
                        ),
                        exit = ExitTransition.None
                    ) {
                        PokeCard(
                            pokemon = p,
                            isFavorite = favorites.contains(p),
                            onPokemonSelected = onPokemonSelected
                        )
                    }
                }
            }
        }
    )
}