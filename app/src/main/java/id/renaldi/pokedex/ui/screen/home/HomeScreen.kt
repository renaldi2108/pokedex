package id.renaldi.pokedex.ui.screen.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.renaldi.pokedex.ui.common.theme.PokedexTheme
import id.renaldi.pokedex.ui.common.theme.Type
import id.renaldi.pokedex.ui.screen.home.appBar.AppBar
import id.renaldi.pokedex.ui.screen.home.appBar.component.Menu
import id.renaldi.pokedex.ui.screen.home.appBar.component.RoundedSearchBar

sealed class MenuItem(
    val label: String,
    val typeColor: Type
) {
    object Pokedex : MenuItem("Pokedex", Type.Grass)
    object Moves : MenuItem("Moves", Type.Fire)
    object Items : MenuItem("Items", Type.Electric)
}

@Composable
fun HomeScreen(onItemSelected: (MenuItem) -> Unit ={}) {
    Surface(
        Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            AppBar()
            Spacer(modifier = Modifier.height(20.dp))
            Menu(onItemSelected = onItemSelected)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
@Composable
fun HomeScreenPreview() {
    PokedexTheme {
        HomeScreen()
    }
}