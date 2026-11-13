package id.renaldi.pokedex.feature.detail.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import id.renaldi.pokedex.core.common.PokemonFormat
import id.renaldi.pokedex.core.designsystem.color.pokemonTypePalette
import id.renaldi.pokedex.core.designsystem.component.NavigationBarScrim
import id.renaldi.pokedex.core.designsystem.component.PokeballBackdrop
import id.renaldi.pokedex.core.designsystem.component.PokemonArtwork
import id.renaldi.pokedex.core.designsystem.component.SectionHeader
import id.renaldi.pokedex.core.designsystem.component.StatBar
import id.renaldi.pokedex.core.designsystem.component.TypeChip
import id.renaldi.pokedex.core.designsystem.system.SystemBarIcons
import id.renaldi.pokedex.core.designsystem.token.Radius
import id.renaldi.pokedex.core.designsystem.token.Sizing
import id.renaldi.pokedex.core.designsystem.token.Spacing
import id.renaldi.pokedex.core.model.EvolutionStep
import id.renaldi.pokedex.core.model.EvolutionTrigger
import id.renaldi.pokedex.core.model.Pokemon
import io.github.renaldi2108.arus.core.select

@Composable
fun DetailScreen(controller: DetailController) {
    val pokemon by controller.select { it.pokemon }
    val dark = isSystemInDarkTheme()
    val current = pokemon

    if (current == null) {
        SystemBarIcons(darkStatusBarIcons = !dark)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag("detail_loading"),
            contentAlignment = Alignment.Center,
        ) {
            Text("Memuat...", style = MaterialTheme.typography.titleMedium)
        }
        return
    }

    val palette = pokemonTypePalette(current.primaryType, dark)
    val hero by animateColorAsState(palette.seed, label = "detail-hero")
    val isFavorite by controller.select { it.isFavorite }
    val tab by controller.select { it.tab }

    SystemBarIcons(
        darkStatusBarIcons = palette.prefersDarkIcons,
        darkNavigationBarIcons = palette.prefersDarkIcons,
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(hero, palette.container, MaterialTheme.colorScheme.background),
                    ),
                )
                .testTag("detail_screen"),
        ) {
            DetailHero(
                pokemon = current,
                isFavorite = isFavorite,
                onBack = controller::onBackClicked,
                onFavorite = controller::onFavoriteToggled,
                onSeed = palette.onSeed,
                dark = dark,
            )

            Surface(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = Radius.sheet, topEnd = Radius.sheet),
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(Spacing.gutter),
                ) {
                    TabRow(selectedTabIndex = tab.ordinal) {
                        DetailTab.entries.forEach { entry ->
                            Tab(
                                selected = entry == tab,
                                onClick = { controller.onTabSelected(entry) },
                                text = { Text(entry.label) },
                                modifier = Modifier.testTag("tab_${entry.name}"),
                            )
                        }
                    }
                    Spacer(Modifier.height(Spacing.lg))
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        when (tab) {
                            DetailTab.About -> AboutSection(current)
                            DetailTab.BaseStats -> StatsSection(current, palette.seed)
                            DetailTab.Evolution -> EvolutionSection(controller, current)
                        }
                    }
                }
            }
        }

        NavigationBarScrim(
            color = hero,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}


@Composable
private fun DetailHero(
    pokemon: Pokemon,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onFavorite: () -> Unit,
    onSeed: androidx.compose.ui.graphics.Color,
    dark: Boolean,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        PokeballBackdrop(
            tint = onSeed,
            alpha = 0.12f,
            spinning = true,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding(),
        )
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(Spacing.gutter),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back")) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = onSeed,
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = onFavorite,
                    modifier = Modifier.testTag("detail_favorite"),
                ) {
                    Icon(
                        imageVector = if (isFavorite) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Outlined.FavoriteBorder
                        },
                        contentDescription = "Favorit",
                        tint = onSeed,
                    )
                }
            }
            Text(
                text = pokemon.name,
                style = MaterialTheme.typography.displayMedium,
                color = onSeed,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(Spacing.xs))
            Text(
                text = PokemonFormat.displayId(pokemon.id),
                style = MaterialTheme.typography.titleMedium,
                color = onSeed.copy(alpha = 0.75f),
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                    modifier = Modifier.weight(1f),
                ) {
                    pokemon.types.forEach { type ->
                        TypeChip(type = type, dark = dark)
                    }
                }
                PokemonArtwork(
                    id = pokemon.id,
                    name = pokemon.name,
                    floating = true,
                    modifier = Modifier.size(Sizing.artworkHero),
                )
            }
        }
    }
}

@Composable
private fun AboutSection(pokemon: Pokemon) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
        Text(text = pokemon.description, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(Spacing.sm))
        AboutRow(label = "Kategori", value = pokemon.category)
        AboutRow(label = "Tinggi", value = PokemonFormat.height(pokemon.height))
        AboutRow(label = "Berat", value = PokemonFormat.weight(pokemon.weight))
        AboutRow(
            label = "Gender",
            value = if (pokemon.hasGenderRate) {
                val female = (pokemon.femaleRate * 100).toInt()
                "${100 - female}% male / $female% female"
            } else {
                "Tidak diketahui"
            },
        )
        AboutRow(label = "Total stat", value = pokemon.stats.total.toString())
        AboutRow(label = "Jumlah move", value = pokemon.moves.size.toString())
    }
}

@Composable
private fun AboutRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(text = value, style = MaterialTheme.typography.titleSmall)
    }
}

@Composable
private fun StatsSection(pokemon: Pokemon, color: androidx.compose.ui.graphics.Color) {
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        pokemon.stats.entries().forEach { entry ->
            StatBar(entry = entry, color = color)
        }
        Spacer(Modifier.height(Spacing.sm))
        AboutRow(label = "Total", value = pokemon.stats.total.toString())
    }
}

@Composable
private fun EvolutionSection(controller: DetailController, pokemon: Pokemon) {
    val evolutions by controller.select { it.evolutions }

    if (evolutions.isEmpty()) {
        SectionHeader(title = "Pokemon ini tidak berevolusi")
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
        evolutions.forEach { step ->
            EvolutionRow(
                step = step,
                isCurrent = step.pokemon.id == pokemon.id,
                onClick = { controller.onEvolutionClicked(step) },
            )
        }
    }
}

@Composable
private fun EvolutionRow(step: EvolutionStep, isCurrent: Boolean, onClick: () -> Unit) {
    val dark = isSystemInDarkTheme()
    val palette = pokemonTypePalette(step.pokemon.primaryType, dark)

    Surface(
        onClick = onClick,
        color = if (isCurrent) palette.container else MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(Radius.card),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("evolution_${step.pokemon.id}"),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(Spacing.md),
        ) {
            PokemonArtwork(
                id = step.pokemon.id,
                name = step.pokemon.name,
                modifier = Modifier
                    .size(Sizing.artworkEvolution)
                    .clip(RoundedCornerShape(Radius.chip)),
            )
            Spacer(Modifier.size(Spacing.md))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = step.pokemon.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = step.requirementLabel(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = PokemonFormat.displayId(step.pokemon.id),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
            )
        }
    }
}

private fun EvolutionStep.requirementLabel(): String = when {
    trigger == EvolutionTrigger.UseItem -> "Pakai item"
    trigger == EvolutionTrigger.Trade -> "Tukar"
    targetLevel > 0 -> "Level $targetLevel"
    else -> "Bentuk dasar"
}
