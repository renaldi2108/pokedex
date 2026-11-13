package id.renaldi.pokedex.feature.pokedex.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.testTag
import id.renaldi.pokedex.core.designsystem.color.pokemonTypeContainerBlend
import id.renaldi.pokedex.core.designsystem.color.pokemonTypePalette
import id.renaldi.pokedex.core.designsystem.component.EmptyState
import id.renaldi.pokedex.core.designsystem.component.NavigationBarScrim
import id.renaldi.pokedex.core.designsystem.component.PokedexSearchField
import id.renaldi.pokedex.core.designsystem.component.PokemonCard
import id.renaldi.pokedex.core.designsystem.system.SystemBarIcons
import id.renaldi.pokedex.core.designsystem.token.Spacing
import id.renaldi.pokedex.core.model.PokedexFilter
import id.renaldi.pokedex.core.model.PokemonType
import io.github.renaldi2108.arus.core.select
import io.github.renaldi2108.arus.core.watch

@Composable
fun PokedexScreen(controller: PokedexController) {
    val pokemon by controller.select { it.pokemon }
    val isEmpty by controller.select { it.isEmpty }
    val filter by controller.select { it.filter }
    val favorites by controller.select { it.favorites }
    val dark = isSystemInDarkTheme()

    val tint = pokemonTypeContainerBlend(filter.types, dark)
    val headerColor by animateColorAsState(
        targetValue = tint ?: MaterialTheme.colorScheme.background,
        label = "pokedex-header",
    )
    val darkBarIcons = if (tint == null) !dark else headerColor.luminance() > 0.5f

    SystemBarIcons(darkStatusBarIcons = darkBarIcons, darkNavigationBarIcons = darkBarIcons)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            PokedexHeader(
                controller = controller,
                filter = filter,
                dark = dark,
                background = headerColor,
            )
            if (isEmpty) {
                EmptyState(
                    title = "Tidak ditemukan",
                    subtitle = "Coba ubah kata kunci atau filter tipe.",
                    modifier = Modifier.testTag("pokedex_empty"),
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        start = Spacing.gutter,
                        end = Spacing.gutter,
                        top = Spacing.gutter,
                        bottom = Spacing.gutter +
                            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
                    ),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                    verticalArrangement = Arrangement.spacedBy(Spacing.md),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("pokedex_grid"),
                ) {
                    items(pokemon, key = { it.id }) { item ->
                        PokemonCard(
                            pokemon = item,
                            dark = dark,
                            isFavorite = item.id in favorites,
                            onClick = { controller.onPokemonClicked(item) },
                            onFavoriteClick = { controller.onFavoriteToggled(item) },
                        )
                    }
                }
            }
        }

        NavigationBarScrim(
            color = headerColor,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}


@Composable
private fun PokedexHeader(
    controller: PokedexController,
    filter: PokedexFilter,
    dark: Boolean,
    background: Color,
) {
    val draft by controller.draftQuery.watch()

    Column(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    listOf(background, background, MaterialTheme.colorScheme.background),
                ),
            )
            .statusBarsPadding()
            .padding(
                start = Spacing.gutter,
                end = Spacing.gutter,
                top = Spacing.lg,
                bottom = Spacing.md,
            ),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Pokedex",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.weight(1f),
            )
            IconButton(
                onClick = controller::onFavoritesClicked,
                modifier = Modifier.testTag("open_favorites"),
            ) {
                Icon(Icons.Filled.FavoriteBorder, contentDescription = "Favorit")
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            PokedexSearchField(
                value = draft,
                onValueChange = controller::onQueryChanged,
                modifier = Modifier.weight(1f),
            )
            FilledIconButton(
                onClick = controller::onFilterClicked,
                modifier = Modifier.testTag("open_filter"),
            ) {
                Icon(Icons.Filled.Tune, contentDescription = "Filter")
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            modifier = Modifier.testTag("type_filters"),
        ) {
            items(PokemonType.entries) { type ->
                val selected = type in filter.types
                val palette = pokemonTypePalette(type, dark)
                FilterChip(
                    selected = selected,
                    onClick = { controller.onTypeToggled(type) },
                    label = { Text(type.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = palette.seed,
                        selectedLabelColor = palette.onSeed,
                    ),
                    modifier = Modifier.testTag("type_chip_${type.label}"),
                )
            }
        }
    }
}
