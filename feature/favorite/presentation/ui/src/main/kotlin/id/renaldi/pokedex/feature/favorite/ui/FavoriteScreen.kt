package id.renaldi.pokedex.feature.favorite.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import id.renaldi.pokedex.core.designsystem.component.EmptyState
import id.renaldi.pokedex.core.designsystem.component.NavigationBarScrim
import id.renaldi.pokedex.core.designsystem.component.PokemonCard
import id.renaldi.pokedex.core.designsystem.system.SystemBarIcons
import id.renaldi.pokedex.core.designsystem.token.Spacing
import io.github.renaldi2108.arus.core.select

@Composable
fun FavoriteScreen(controller: FavoriteController) {
    val pokemon by controller.select { it.pokemon }
    val isEmpty by controller.select { it.isEmpty }
    val dark = isSystemInDarkTheme()

    SystemBarIcons(darkStatusBarIcons = !dark)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = Spacing.sm, end = Spacing.gutter, top = Spacing.sm),
            ) {
                IconButton(
                    onClick = controller::onBackClicked,
                    modifier = Modifier.testTag("favorite_back"),
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                }
                Text(
                    text = "Favorit",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(start = Spacing.sm),
                )
            }

            if (isEmpty) {
                EmptyState(
                    title = "Belum ada favorit",
                    subtitle = "Tekan ikon hati pada kartu Pokemon untuk menyimpannya di sini.",
                    modifier = Modifier.testTag("favorite_empty"),
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
                        .testTag("favorite_grid"),
                ) {
                    items(pokemon, key = { it.id }) { item ->
                        PokemonCard(
                            pokemon = item,
                            dark = dark,
                            isFavorite = true,
                            onClick = { controller.onPokemonClicked(item) },
                            onFavoriteClick = { controller.onFavoriteToggled(item) },
                        )
                    }
                }
            }
        }

        NavigationBarScrim(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
