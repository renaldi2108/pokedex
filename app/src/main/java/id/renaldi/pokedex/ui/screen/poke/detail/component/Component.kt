package id.renaldi.pokedex.ui.screen.poke.detail.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.renaldi.pokedex.R
import id.renaldi.pokedex.data.model.Pokemon
import id.renaldi.pokedex.data.model.PokemonDetailsEvolutions
import id.renaldi.pokedex.ui.common.Emphasis
import id.renaldi.pokedex.ui.common.PokemonTypeLabels
import id.renaldi.pokedex.ui.common.TypeLabelMetrics.Companion.MEDIUM
import id.renaldi.pokedex.ui.common.poke.PokeBall
import id.renaldi.pokedex.ui.common.theme.PokemonMenuTheme
import id.renaldi.pokedex.ui.common.theme.TypeTheme
import id.renaldi.pokedex.ui.screen.poke.detail.section.AboutSection
import id.renaldi.pokedex.ui.screen.poke.detail.section.BaseStatsSection
import id.renaldi.pokedex.ui.screen.poke.detail.section.EvolutionSection
import id.renaldi.pokedex.utils.formatId
import kotlinx.coroutines.launch

@Composable
fun RoundedRectangleDecoration(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(150.dp)
            .background(color = Color(0x22FFFFFF), shape = RoundedCornerShape(32.dp))
    )
}

@Composable
fun DottedDecoration(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.dotted),
        contentDescription = null,
        modifier = modifier.size(width = 63.dp, height = 34.dp),
        alpha = 0.3f
    )
}

@Composable
fun RotatingPokeBall(
    modifier: Modifier = Modifier,
    tint: Color = Color(0x40F5F5F5)
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing)
        )
    )

    PokeBall(
        modifier = modifier
            .graphicsLayer {
                rotationZ = angle
            },
        tint = tint,
    )
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    pokemon: Pokemon
) {
    Column(
        modifier.padding(top = 40.dp, bottom = 32.dp, start = 24.dp, end = 24.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = pokemon.name,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = formatId(pokemon.id),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .alignByBaseline()
                    .graphicsLayer { alpha = Emphasis.Medium.alpha }
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            Modifier.fillMaxWidth(),
        ) {
            PokemonTypeLabels(
                types = pokemon.typeOfPokemon,
                metrics = MEDIUM,
            )
        }
    }
}

enum class Sections(val title: String) {
    About("About"),
    BaseStats("Base stats"),
    Evolution("Evolution")
}

@Composable
fun CardContent(
    modifier: Modifier,
    pokemon: Pokemon,
    evolutions: List<PokemonDetailsEvolutions>,
) {
    val sectionTitles = Sections.values().map { it.title }
    var section by rememberSaveable { mutableStateOf(Sections.BaseStats) }

    val tabIndicatorColor by animateColorAsState(
        targetValue = TypeTheme.colorScheme.primary,
        tween(durationMillis = 500),
        label = "tabIndicatorColor"
    )

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        PokemonMenuTheme(types = pokemon.typeOfPokemon[0]) {
            TabRow(
                containerColor = MaterialTheme.colorScheme.surface,
                selectedTabIndex = section.ordinal,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[section.ordinal])
                            .clip(RoundedCornerShape(100)),
                        color = tabIndicatorColor
                    )
                },
            ) {
                sectionTitles.forEachIndexed { index, text ->
                    val active = index == section.ordinal
                    Tab(
                        selected = active,
                        selectedContentColor = TypeTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        onClick = { section = Sections.values()[index] },
                    ) {
                        Text(
                            text = text,
                            fontWeight = if (active) FontWeight.Medium else FontWeight.Normal,
                            modifier = Modifier.padding(vertical = 20.dp)
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier.padding(24.dp)
        ) {
            when (section) {
                Sections.About -> AboutSection(pokemon)
                Sections.BaseStats -> BaseStatsSection(pokemon)
                else -> EvolutionSection(evolutions = evolutions)
            }
        }
    }
}