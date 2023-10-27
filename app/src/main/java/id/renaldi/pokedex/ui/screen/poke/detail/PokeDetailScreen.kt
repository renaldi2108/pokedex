package id.renaldi.pokedex.ui.screen.poke.detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import id.renaldi.pokedex.data.model.Pokemon
import id.renaldi.pokedex.ui.common.NestedScrollConnection
import id.renaldi.pokedex.ui.common.TopNavBar
import id.renaldi.pokedex.ui.common.theme.PokemonMenuTheme
import id.renaldi.pokedex.ui.common.theme.TypeTheme
import id.renaldi.pokedex.ui.screen.poke.main.PokemonViewModel
import id.renaldi.pokedex.ui.screen.poke.detail.component.CardContent
import id.renaldi.pokedex.ui.screen.poke.detail.component.DottedDecoration
import id.renaldi.pokedex.ui.screen.poke.detail.component.Header
import id.renaldi.pokedex.ui.screen.poke.detail.component.PagerPokemonImage
import id.renaldi.pokedex.ui.screen.poke.detail.component.PokemonPager
import id.renaldi.pokedex.ui.screen.poke.detail.component.RotatingPokeBall
import id.renaldi.pokedex.ui.screen.poke.detail.component.RoundedRectangleDecoration
import kotlin.math.roundToInt

@Composable
fun PokemonDetailsScreenRoute(
    viewModel: PokemonViewModel,
    detailViewModel: PokemonDetailsViewModel,
    pokemon: Pokemon,
    onBackClick: () -> Unit,
) {
    viewModel.refresh()
    detailViewModel.refresh(pokemon)

    PokemonDetailsScreen(
        pokemonSet = viewModel.uiState.pokemon,
        detailViewModel = detailViewModel,
        onBackClick = onBackClick,
        onFavoriteClick = {
            detailViewModel.toggleFavorite(it)
        },
    )
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
internal fun PokemonDetailsScreen(
    pokemonSet: List<Pokemon>,
    detailViewModel: PokemonDetailsViewModel,
    onBackClick: () -> Unit = {},
    onFavoriteClick: (Int) -> Unit = { _ -> },
) {
    var pokemon by remember { mutableStateOf(detailViewModel.details) }
    var evolutions by mutableStateOf(detailViewModel.evolutions)

    val density = LocalDensity.current

    val pagerState = rememberPagerState(initialPage = pokemon!!.id - 1)

    val swipeableState = rememberSwipeableState(initialValue = 1)
    val topAnchorMin = with(density) { (16 + 16 + 48).dp.toPx() }
    val topAnchorMax = with(density) { 324.dp.toPx() }

    val anchors = mapOf(topAnchorMin to 0, topAnchorMax to 1)
    val swipeableProgress by remember {
        derivedStateOf {
            swipeableState.progress
        }
    }

    val scaleTarget by remember {
        derivedStateOf {
            if (swipeableProgress.to == 1) {
                if (swipeableProgress.fraction > 0.7f) {
                    swipeableProgress.fraction
                } else {
                    0f
                }
            } else {
                1f - swipeableProgress.fraction
            }
        }
    }
    val scaleModifier = Modifier.graphicsLayer {
        scaleX = scaleTarget
        scaleY = scaleTarget
    }

    val textAlphaTarget by remember {
        derivedStateOf {
            if (swipeableProgress.to == 1) {
                swipeableProgress.fraction
            } else {
                1f - (swipeableProgress.fraction)
            }
        }
    }

    val imageAlphaTarget by remember {
        derivedStateOf {
            if (swipeableProgress.to == 1) {
                if (swipeableProgress.fraction > 0.6f) {
                    swipeableProgress.fraction
                } else {
                    0f
                }
            } else {
                1f - (swipeableProgress.fraction * 4f)
            }
        }
    }

    val cardPaddingTarget by remember {
        derivedStateOf {
            val max = with(density) { 40.dp.toPx() }
            val min = max / 4

            val resolvedValue = if (swipeableProgress.to == 1) {
                swipeableProgress.fraction * max
            } else {
                (1 - swipeableProgress.fraction) * max
            }

            resolvedValue
                .coerceIn(min, max)
                .roundToInt()
        }
    }

    val pagerZIndex by remember {
        derivedStateOf {
            if (swipeableProgress.from == 0 && swipeableProgress.to == 0) {
                -1f
            } else {
                0f
            }
        }
    }

    LaunchedEffect(pagerState, pokemonSet) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (pokemonSet.isNotEmpty()) {
                detailViewModel.refresh(pokemonSet[page])
                pokemon = pokemonSet[page]
                evolutions = detailViewModel.evolutions
            }
        }
    }

    PokemonMenuTheme(
        types = pokemon!!.typeOfPokemon[0]
    ) {
        val pokemonTypeColor by animateColorAsState(
            targetValue = TypeTheme.colorScheme.surface,
            animationSpec = tween(durationMillis = 500),
            label = "pokemonTypeSurfaceColor"
        )

        Surface(
            modifier = Modifier
                .drawBehind {
                    drawRect(pokemonTypeColor)
                },
            color = Color.Transparent
        ) {
            Box(Modifier.fillMaxSize()) {
                RoundedRectangleDecoration(
                    Modifier
                        .offset(x = (-60).dp, y = (-50).dp)
                        .rotate(-20f)
                )
                DottedDecoration(
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 100.dp)
                )
                RotatingPokeBall(
                    Modifier
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                        .padding(top = 16.dp)
                        .padding(top = 164.dp)
                        .size(180.dp)
                        .graphicsLayer { alpha = textAlphaTarget },
                    tint = TypeTheme.colorScheme.surfaceVariant
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .padding(top = 16.dp)
                ) {
                    val textFadeInTransition = fadeIn(tween(durationMillis = 210, delayMillis = 90, easing = LinearOutSlowInEasing))
                    val textFadeOutTransition = fadeOut(tween(durationMillis = 90, easing = FastOutLinearInEasing))

                    AnimatedContent(
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .graphicsLayer { alpha = textAlphaTarget },
                        targetState = pokemon,
                        transitionSpec = {
                            if (initialState!!.id < targetState!!.id) {
                                textFadeInTransition +
                                        slideInHorizontally(initialOffsetX = { with(density) { 16.dp.roundToPx() } }, animationSpec = tween(300)) with
                                        textFadeOutTransition
                            } else {
                                textFadeInTransition +
                                        slideInHorizontally(initialOffsetX = { with(density) { -16.dp.roundToPx() } }, animationSpec = tween(300)) with
                                        textFadeOutTransition
                            }.using(SizeTransform(clip = false))
                        }, label = ""
                    ) { targetPokemon ->
                        Header(pokemon = targetPokemon!!)
                    }

                    val nestedScrollConnection = NestedScrollConnection(
                        swipeableState = swipeableState,
                        minAnchor = topAnchorMin
                    )

                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .swipeable(
                                state = swipeableState,
                                anchors = anchors,
                                orientation = Orientation.Vertical
                            )
                            .nestedScroll(nestedScrollConnection)
                            .offset {
                                IntOffset(
                                    x = 0,
                                    y = swipeableState.offset.value.roundToInt()
                                )
                            },
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    ) {
                        CardContent(
                            pokemon = pokemon!!,
                            evolutions = evolutions,
                            modifier = Modifier.offset { IntOffset(x = 0, y = cardPaddingTarget) },
                        )
                    }

                    PokemonPager(
                        modifier = Modifier
                            .zIndex(pagerZIndex)
                            .padding(top = 124.dp)
                            .graphicsLayer { alpha = imageAlphaTarget },
                        pokemonList = pokemonSet,
                        backgroundColor = TypeTheme.colorScheme.surface,
                        enabled = swipeableState.currentValue == 1,
                        pagerState = pagerState,
                    ) { it, progress, tint ->
                        PagerPokemonImage(
                            image = it.image,
                            description = it.name,
                            tint = tint,
                            progress = progress,
                            modifier = scaleModifier.size(240.dp),
                        )
                    }
                }
                TopNavBar(
                    modifier = Modifier
                        .statusBarsPadding(),
                    title = {
                        Text(
                            text = pokemon!!.name,
                            modifier = Modifier.graphicsLayer {
                                alpha = 1f - textAlphaTarget
                            }
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { onFavoriteClick(pokemon!!.id) }
                        ) {
                            Icon(
                                imageVector = if (detailViewModel.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (detailViewModel.isFavorite) "Remove from Favorites" else "Add to Favorites",
                            )
                        }
                    },
                    onBackClick = onBackClick
                )
            }
        }
    }
}