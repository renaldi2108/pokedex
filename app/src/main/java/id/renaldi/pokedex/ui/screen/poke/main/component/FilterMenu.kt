package id.renaldi.pokedex.ui.screen.poke.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import id.renaldi.pokedex.R

enum class FilterMenuItem {
    Favorites,
}

@Composable
fun FilterMenu(
    modifier: Modifier = Modifier,
    visible: Boolean,
    showFavorites: Boolean,
    onMenuItemClick: (FilterMenuItem) -> Unit = {},
) {
    AnimatedVisibility(
        visible = visible,
        enter = EnterTransition.None,
        exit = ExitTransition.None,
        label = "Filter menu",
        modifier = modifier
    ) {
        ProvideTextStyle(
            value = LocalTextStyle.current.merge(
                TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FilterMenuItem(
                    index = 0,
                    onClick = { onMenuItemClick(FilterMenuItem.Favorites) }
                ) {
                    Icon(
                        imageVector = if (showFavorites) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(if (showFavorites) "Show all" else "Show favorites")
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimatedVisibilityScope.FilterMenuItem(
    modifier: Modifier = Modifier,
    index: Int,
    onClick: () -> Unit = {},
    content: @Composable RowScope.() -> Unit = {}
) {
    FilledTonalButton(
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
        onClick = onClick,
        modifier = modifier
            .animateEnterExit(
                enter = fadeIn(animationSpec = tween(durationMillis = 200, delayMillis = index * 15 + 60)) +
                        slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(durationMillis = 240, delayMillis = index * 50 + 60)
                        ),
                exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) +
                        slideOutVertically(targetOffsetY = { it / 2 }),
                label = "Filter menu item"
            )
    ) {
        content()
    }
}