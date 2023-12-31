package id.renaldi.pokedex.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.renaldi.pokedex.ui.common.theme.PokemonMenuTheme
import id.renaldi.pokedex.ui.common.theme.TypeTheme

data class TypeLabelMetrics(
    val cornerRadius: Dp,
    val fontSize: TextUnit,
    val fontWeight: FontWeight = FontWeight.Normal,
    val verticalPadding: Dp,
    val horizontalPadding: Dp,
    val elementSpacing: Dp
) {
    companion object {
        val SMALL = TypeLabelMetrics(24.dp,
            fontSize = 9.sp,
            verticalPadding = 3.dp,
            horizontalPadding = 8.dp,
            elementSpacing = 8.dp
        )
        val MEDIUM = TypeLabelMetrics(24.dp,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            verticalPadding = 4.dp,
            horizontalPadding = 12.dp,
            elementSpacing = 8.dp
        )
    }
}

@Composable
fun PokemonTypeLabels(
    modifier: Modifier = Modifier,
    types: List<String>,
    metrics: TypeLabelMetrics
) {
    PokemonMenuTheme(types = types[0]) {
        types.forEach {
            TypeLabel(modifier = modifier, text = it, metrics = metrics)
            Spacer(modifier = Modifier.size(metrics.elementSpacing))
        }
    }
}

@Composable
fun TypeLabel(
    modifier: Modifier = Modifier,
    text: String,
    colored: Boolean = false,
    metrics: TypeLabelMetrics
) {
    Surface(
        modifier = modifier,
        color = if (colored) TypeTheme.colorScheme.surface else Color(0x38FFFFFF),
        shape = RoundedCornerShape(metrics.cornerRadius)
    ) {
        Text(
            text = text,
            fontSize = metrics.fontSize,
            fontWeight = metrics.fontWeight,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                start = metrics.horizontalPadding,
                end = metrics.horizontalPadding,
                top = metrics.verticalPadding,
                bottom = metrics.verticalPadding
            ),
        )
    }
}

@Composable
fun Label(
    modifier: Modifier = Modifier,
    text: String,
    emphasis: Emphasis = Emphasis.Medium
) {
    Text(
        text = text,
        modifier = modifier.graphicsLayer {
            alpha = emphasis.alpha
        },
    )
}

enum class Emphasis(val alpha: Float) {
    Disabled(0.5f),
    Medium(0.7f),
    High(1f)
}