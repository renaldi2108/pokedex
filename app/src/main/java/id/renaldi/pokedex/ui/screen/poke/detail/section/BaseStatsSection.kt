package id.renaldi.pokedex.ui.screen.poke.detail.section

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.renaldi.pokedex.data.model.Pokemon
import id.renaldi.pokedex.ui.common.Label
import id.renaldi.pokedex.ui.common.theme.PokemonMenuTheme
import id.renaldi.pokedex.ui.common.theme.TypeTheme
import kotlinx.coroutines.delay

data class Stat(
    val label: String,
    val value: Int?,
    val max: Int = 200
) {
    val progress: Float =
        1f * (value ?: 0) / max
}

@Composable
fun BaseStatsSection(
    pokemon: Pokemon
) {
    val stats = listOf(
        Stat("HP", pokemon.hp),
        Stat("Attack", pokemon.attack),
        Stat("Defense", pokemon.defense),
        Stat("Sp. Atk", pokemon.specialAttack),
        Stat("Sp. Def", pokemon.specialDefense),
        Stat("Speed", pokemon.speed),
    )

    Column(Modifier.fillMaxWidth()) {
        stats.forEachIndexed { idx, stat ->
            val statValue = remember { Animatable(0f) }

            LaunchedEffect(stat) {
                delay(70L * idx)
                statValue.animateTo(
                    targetValue = stat.progress,
                    animationSpec = spring(
                        0.6f,
                        1000f
                    )
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Label(
                    text = stat.label,
                    modifier = Modifier
                        .weight(1f)
                        .graphicsLayer { alpha = 0.7f }
                )
                Text(
                    "${stat.value}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .weight(0.6f)
                )
                PokemonMenuTheme(
                    types = pokemon.typeOfPokemon[0]
                ) {
                    val indicatorColor by animateColorAsState(
                        targetValue = TypeTheme.colorScheme.primary,
                        tween(durationMillis = 500),
                        label = "statsProgressIndicatorColor"
                    )

                    LinearProgressIndicator(
                        progress = statValue.value,
                        color = indicatorColor,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clip(RoundedCornerShape(100))
                            .weight(2.5f)
                    )
                }
            }
        }
    }
}