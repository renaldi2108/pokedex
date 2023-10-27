package id.renaldi.pokedex.ui.screen.poke.detail.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.renaldi.pokedex.data.model.EvolutionTrigger
import id.renaldi.pokedex.data.model.Pokemon
import id.renaldi.pokedex.data.model.PokemonDetailsEvolutions
import id.renaldi.pokedex.ui.common.PokemonImage
import id.renaldi.pokedex.ui.common.poke.PokeBall

@Composable
fun EvolutionSection(
    modifier: Modifier = Modifier,
    evolutions: List<PokemonDetailsEvolutions> = listOf(),
) {
    Column(modifier) {
        if(evolutions.size > 1) {
            evolutions.forEachIndexed { idx, evo ->
                if (idx < evolutions.size - 1) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val e1 = evo
                        val e2 = evolutions[idx + 1]
                        EvolutionCard(e1.pokemon)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.surfaceTint
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = when (e2.trigger) {
                                    EvolutionTrigger.Trade ->
                                        "Trade"

                                    else ->
                                        "Lvl ${e2.targetLevel}"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        EvolutionCard(e2.pokemon)
                    }
                }
                if (idx < evolutions.size - 2) Divider()
            }
        } else {
            Text(text = "No evolutions found")
        }
    }
}

@Composable
private fun EvolutionCard(
    pokemon: Pokemon,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable {}
            .width(128.dp)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            PokeBall(
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
            )
            PokemonImage(
                modifier = Modifier.size(72.dp),
                image = pokemon.id
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            pokemon.name
        )
        Spacer(Modifier.height(4.dp))
    }
}