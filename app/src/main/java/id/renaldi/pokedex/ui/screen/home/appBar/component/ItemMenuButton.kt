package id.renaldi.pokedex.ui.screen.home.appBar.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.renaldi.pokedex.ui.common.poke.PokeBall
import id.renaldi.pokedex.ui.common.theme.PokemonMenuTheme
import id.renaldi.pokedex.ui.common.theme.PokedexTheme
import id.renaldi.pokedex.ui.common.theme.TypeTheme
import id.renaldi.pokedex.ui.screen.home.MenuItem.*
import id.renaldi.pokedex.ui.screen.home.MenuItem

@Composable
fun Menu(
    onItemSelected: (MenuItem) -> Unit = {}
) {
    val menuItems = listOf(
        Pokedex, Moves,
        Items,
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            items(menuItems.size) { index ->
                val item = menuItems[index]
                PokemonMenuTheme(types = item.typeColor.name) {
                    ItemMenuButton(
                        text = item.label
                    ) {
                        onItemSelected(item)
                    }
                }
            }
        }
    )
}

@Composable
fun ItemMenuButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = TypeTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .height(64.dp)
                .clickable { onClick() },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = text,
                color = Color.White
            )
            PokeBall(
                Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-30).dp, y = (-40).dp)
                    .requiredSize(60.dp),
                Color.White,
                0.15f
            )
            PokeBall(
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp)
                    .requiredSize(96.dp),
                Color.White,
                0.15f
            )
        }
    }
}

@Preview
@Composable
fun MenuPreview() {
    PokedexTheme {
        Surface {
            Menu()
        }
    }
}