package id.renaldi.pokedex.ui.screen.poke.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import id.renaldi.pokedex.R
import id.renaldi.pokedex.data.model.Pokemon
import id.renaldi.pokedex.ui.screen.home.appBar.AppBar
import id.renaldi.pokedex.ui.screen.poke.main.component.FilterMenu
import id.renaldi.pokedex.ui.screen.poke.main.component.FilterMenuItem
import id.renaldi.pokedex.ui.screen.poke.main.component.PokemonList

@Composable
fun PokemonScreenRoute(
    viewModel: PokemonViewModel,
    onPokemonSelected: (Pokemon) -> Unit,
    onFavoriteClick: () -> Unit = {}
) {
    PokemonScreen(
//        loading = viewModel.uiState.loading,
//        pokemon = viewModel.uiState.pokemon,
//        favorites = viewModel.favorites,
//        showFavorites = viewModel.showFavorites,
        viewModel = viewModel,
        onPokemonSelected = onPokemonSelected,
        onFavoriteClick = onFavoriteClick,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PokemonScreen(
//    loading: Boolean,
//    pokemon: List<Pokemon>,
//    favorites: List<Pokemon>,
//    showFavorites: Boolean = false,
    viewModel: PokemonViewModel,
    onPokemonSelected: (Pokemon) -> Unit = {},
    onFavoriteClick: () -> Unit = {}
) {
    var showFilterMenu by remember { mutableStateOf(false) }
    var isRefresh by remember { mutableStateOf(true) }
    val listState = rememberLazyGridState()

    LaunchedEffect(viewModel.uiState, isRefresh) {
        if(!isRefresh) viewModel.refresh()
    }

    Surface(
        Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            Column {
                AppBar {
                    isRefresh = it
                }
                PokemonList(
                    loading = viewModel.uiState.loading,
                    pokemon = if (viewModel.showFavorites) viewModel.favorites else viewModel.uiState.pokemon,
                    favorites = mutableListOf(),
                    onPokemonSelected = onPokemonSelected,
                    listState = listState
                )
            }
            AnimatedVisibility(
                visible = showFilterMenu,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.matchParentSize()
            ) {
                Box(
                    Modifier
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f))
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = { showFilterMenu = false }
                        ),
                )
            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(WindowInsets.navigationBars.asPaddingValues())
                    .padding(bottom = 24.dp)
            ) {
                FilterMenu(
                    visible = showFilterMenu,
                    showFavorites = viewModel.showFavorites,
                    onMenuItemClick = {
                        if (it == FilterMenuItem.Favorites) {
                            showFilterMenu = false
                            onFavoriteClick()
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { showFilterMenu = !showFilterMenu },
                ) {
                    AnimatedContent(
                        targetState = showFilterMenu, label = ""
                    ) { targetState ->
                        if (targetState) {
                            Icon(
                                painterResource(id = R.drawable.ic_close),
                                contentDescription = "Hide filters",
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filter),
                                contentDescription = "Show filters",
                            )
                        }
                    }
                }
            }
        }
    }
}