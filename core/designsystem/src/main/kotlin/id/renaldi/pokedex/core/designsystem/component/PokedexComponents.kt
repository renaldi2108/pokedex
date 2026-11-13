package id.renaldi.pokedex.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import id.renaldi.pokedex.core.common.PokemonFormat
import id.renaldi.pokedex.core.designsystem.R
import id.renaldi.pokedex.core.designsystem.color.pokemonTypePalette
import id.renaldi.pokedex.core.designsystem.token.Radius
import id.renaldi.pokedex.core.designsystem.token.Sizing
import id.renaldi.pokedex.core.designsystem.token.Spacing
import id.renaldi.pokedex.core.model.Pokemon
import id.renaldi.pokedex.core.model.PokemonType
import id.renaldi.pokedex.core.model.StatEntry

private const val ARTWORK_FLOAT_MILLIS = 2200
private const val BACKDROP_SPIN_MILLIS = 9000
private const val ARTWORK_BREATH = 0.04f
private const val FULL_TURN = 360f
private val ARTWORK_FLOAT_DISTANCE: Dp = 10.dp

private val STAT_LABEL_WIDTH: Dp = 72.dp
private val STAT_VALUE_WIDTH: Dp = 40.dp

@Composable
fun NavigationBarScrim(
    color: Color,
    modifier: Modifier = Modifier,
) {
    val animated by animateColorAsState(targetValue = color, label = "navigation-bar-scrim")
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsBottomHeight(WindowInsets.navigationBars)
            .background(animated),
    )
}

@Composable
fun PokemonArtwork(
    id: Int,
    name: String,
    modifier: Modifier = Modifier,
    floating: Boolean = false,
) {
    val transition = rememberInfiniteTransition(label = "artwork-$id")
    val lift by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (floating) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = ARTWORK_FLOAT_MILLIS, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "artwork-float-$id",
    )

    AsyncImage(
        model = PokemonFormat.artworkUrl(id),
        contentDescription = name,
        contentScale = ContentScale.Fit,
        modifier = modifier.graphicsLayer {
            translationY = -lift * ARTWORK_FLOAT_DISTANCE.toPx()
            val breath = 1f + lift * ARTWORK_BREATH
            scaleX = breath
            scaleY = breath
        },
    )
}

@Composable
fun PokeballBackdrop(
    tint: Color,
    modifier: Modifier = Modifier,
    size: Dp = Sizing.backdrop,
    alpha: Float = 0.18f,
    spinning: Boolean = false,
) {
    val transition = rememberInfiniteTransition(label = "backdrop")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (spinning) FULL_TURN else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = BACKDROP_SPIN_MILLIS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "backdrop-spin",
    )

    Icon(
        painter = painterResource(R.drawable.pokeball),
        contentDescription = null,
        tint = tint,
        modifier = modifier
            .size(size)
            .rotate(angle)
            .alpha(alpha),
    )
}

@Composable
fun TypeChip(
    type: PokemonType,
    dark: Boolean,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    val palette = pokemonTypePalette(type, dark)
    Surface(
        color = palette.seed,
        contentColor = palette.onSeed,
        shape = RoundedCornerShape(Radius.pill),
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            modifier = Modifier.padding(
                horizontal = if (compact) Spacing.sm else Spacing.md,
                vertical = Spacing.xs,
            ),
        ) {
            if (!compact) {
                Box(
                    modifier = Modifier
                        .size(Sizing.typeDot)
                        .clip(CircleShape)
                        .background(palette.onSeed.copy(alpha = 0.8f)),
                )
            }
            Text(
                text = type.label,
                style = if (compact) {
                    MaterialTheme.typography.labelSmall
                } else {
                    MaterialTheme.typography.labelMedium
                },
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip,
            )
        }
    }
}

@Composable
fun StatBar(
    entry: StatEntry,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val ratio by animateFloatAsState(
        targetValue = entry.ratio,
        animationSpec = tween(durationMillis = 650),
        label = "stat-${entry.kind.name}",
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = entry.kind.label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(STAT_LABEL_WIDTH),
        )
        Text(
            text = entry.value.toString(),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.width(STAT_VALUE_WIDTH),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(Sizing.statBarHeight)
                .clip(RoundedCornerShape(Radius.pill))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(ratio)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(Radius.pill))
                    .background(color),
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(vertical = Spacing.sm),
    )
}

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    dark: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onFavoriteClick: (() -> Unit)? = null,
) {
    val palette = pokemonTypePalette(pokemon.primaryType, dark)
    val container by animateColorAsState(palette.container, label = "card-container")

    Surface(
        onClick = onClick,
        color = container,
        contentColor = palette.onContainer,
        shape = RoundedCornerShape(Radius.card),
        modifier = modifier.testTag("pokemon_card_${pokemon.id}"),
    ) {
        Box {
            PokeballBackdrop(
                tint = palette.seed,
                size = Sizing.artworkCard,
                alpha = 0.22f,
                modifier = Modifier.align(Alignment.BottomEnd),
            )
            Column(modifier = Modifier.padding(Spacing.lg)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = PokemonFormat.displayId(pokemon.id),
                        style = MaterialTheme.typography.labelMedium,
                        color = palette.onContainer.copy(alpha = 0.7f),
                        modifier = Modifier.weight(1f),
                    )
                    if (onFavoriteClick != null) {
                        IconButton(
                            onClick = onFavoriteClick,
                            modifier = Modifier.testTag("favorite_toggle_${pokemon.id}"),
                        ) {
                            Icon(
                                imageVector = if (isFavorite) {
                                    Icons.Filled.Favorite
                                } else {
                                    Icons.Outlined.FavoriteBorder
                                },
                                contentDescription = pokemon.name,
                                tint = palette.accent,
                            )
                        }
                    }
                }
                PokemonArtwork(
                    id = pokemon.id,
                    name = pokemon.name,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(Sizing.artworkCard),
                )
                Spacer(Modifier.height(Spacing.sm))
                Text(
                    text = pokemon.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(Spacing.xs))
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    pokemon.types.forEach { type ->
                        TypeChip(type = type, dark = dark, compact = true)
                    }
                }
            }
        }
    }
}

@Composable
fun PokedexSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Cari Pokemon",
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
        },
        singleLine = true,
        shape = RoundedCornerShape(Radius.chip),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("search_field"),
    )
}

@Composable
fun EmptyState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
