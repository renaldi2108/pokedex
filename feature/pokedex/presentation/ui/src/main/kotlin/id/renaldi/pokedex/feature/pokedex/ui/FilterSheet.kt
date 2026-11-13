package id.renaldi.pokedex.feature.pokedex.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import id.renaldi.pokedex.core.designsystem.color.pokemonTypePalette
import id.renaldi.pokedex.core.designsystem.component.SectionHeader
import id.renaldi.pokedex.core.designsystem.token.Spacing
import id.renaldi.pokedex.core.model.PokedexSort
import id.renaldi.pokedex.core.model.PokemonType
import io.github.renaldi2108.arus.core.select

@Composable
fun FilterSheet(controller: FilterController) {
    val filter by controller.select { it.filter }
    val dark = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(Spacing.xl)
            .testTag("filter_sheet"),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Text(text = "Filter", style = MaterialTheme.typography.headlineMedium)

        SectionHeader(title = "Urutkan")
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
            PokedexSort.entries.forEach { sort ->
                FilterChip(
                    selected = filter.sort == sort,
                    onClick = { controller.onSortSelected(sort) },
                    label = { Text(sort.label) },
                    modifier = Modifier.testTag("sort_${sort.name}"),
                )
            }
        }

        SectionHeader(title = "Tipe")
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            PokemonType.entries.forEach { type ->
                val palette = pokemonTypePalette(type, dark)
                FilterChip(
                    selected = type in filter.types,
                    onClick = { controller.onTypeToggled(type) },
                    label = { Text(type.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = palette.seed,
                        selectedLabelColor = palette.onSeed,
                    ),
                    modifier = Modifier.testTag("filter_type_${type.label}"),
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            modifier = Modifier.fillMaxWidth(),
        ) {
            TextButton(
                onClick = controller::onReset,
                modifier = Modifier.testTag("filter_reset"),
            ) {
                Text("Reset")
            }
            Button(
                onClick = controller::onApply,
                modifier = Modifier
                    .weight(1f)
                    .testTag("filter_apply"),
            ) {
                Text("Terapkan")
            }
        }
    }
}
