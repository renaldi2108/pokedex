package id.renaldi.pokedex

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.HiltAndroidApp
import id.renaldi.pokedex.data.model.Evolution
import id.renaldi.pokedex.data.model.EvolutionTrigger
import id.renaldi.pokedex.data.model.Pokemon
//import id.renaldi.pokedex.data.model.SamplePokemonData
import id.renaldi.pokedex.ui.screen.poke.main.PokemonScreenRoute
import id.renaldi.pokedex.ui.screen.poke.detail.PokemonDetailsScreenRoute
import id.renaldi.pokedex.ui.screen.poke.favorite.PokeFavScreenRoute
import kotlinx.coroutines.delay

@HiltAndroidApp
class PokeApp: Application()

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun NavScreen() {
    val navController = rememberNavController()
    var pokemon by remember { mutableStateOf(
        Pokemon(
        id = 26,
        name = "Raichu",
        description = "If the electrical sacks become excessively charged, Raichu plants its tail in the ground and discharges. Scorched patches of ground will be found near this pokemon’s nest.",
        typeOfPokemon = listOf("Electric"),
        category = "Mouse Pokémon",
        image = 26,
        height = 0.8,
        weight = 30.0,
        genderRate = 4,
        hp = 60,
        attack = 90,
        defense = 55,
        specialAttack = 90,
        specialDefense = 80,
        speed = 110,
        evolutionChain = listOf(
            Evolution(25, -1),
            Evolution(26, -1, EvolutionTrigger.UseItem, 83),
        )
    )
    ) }

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        },
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        navigation(startDestination = "list", route = "pokedex") {
            composable(
                route = "list",
            ) {
                EnterAnimation {
                    PokemonScreenRoute(
                        viewModel = hiltViewModel(),
                        onPokemonSelected = {
                            pokemon = it
                            navController.navigate("details")
                        },
                        onFavoriteClick = {
                            navController.navigate("favorite")
                        }
                    )
                }
            }

            composable(
                route = "favorite",
            ) {
                EnterAnimation {
                    PokeFavScreenRoute(
                        viewModel = hiltViewModel(),
                        onBackClick = { navController.popBackStack() },
                        onPokemonSelected = {
                            pokemon = it
                            navController.navigate("details")
                        },
                    )
                }
            }

            composable("details") {
                PokemonDetailsScreenRoute(
                    viewModel = hiltViewModel(),
                    detailViewModel = hiltViewModel(),
                    pokemon = pokemon,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LaunchedEffect("SplashScreen") {
            delay(1000)  // the delay of 3 seconds
            navController.popBackStack()
            navController.navigate("pokedex")
        }

        Image(
            painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun EnterAnimation(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visibleState = MutableTransitionState(
            initialState = false
        ).apply { targetState = true },
        modifier = Modifier,
        enter = slideInVertically(
            animationSpec = tween(500),
            initialOffsetY = { -40 }
        ) + expandVertically(
            expandFrom = Alignment.Top
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically(animationSpec = tween(500)) + shrinkVertically() + fadeOut(),
    ) {
        content()
    }
}