package id.renaldi.pokedex.ui.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import com.skydoves.landscapist.coil.CoilImage
import id.renaldi.pokedex.data.model.placeholderPokemonImage
import id.renaldi.pokedex.utils.artworkUrl

@Composable
fun PokemonImage(
    modifier: Modifier = Modifier,
    image: Int,
    description: String? = null,
    tint: Color? = null
) {
    Log.e("hello", "$image")
    CoilImage(
        imageModel = { artworkUrl(image) },
        previewPlaceholder = placeholderPokemonImage(image),
        loading = {
            CircularProgressIndicator()
        },
        success = { imageState ->
            imageState.drawable?.let { img ->
                Image(
                    bitmap = img.toBitmap().asImageBitmap(),
                    contentDescription = description,
                    modifier = Modifier.matchParentSize(),
                    colorFilter = tint?.let { ColorFilter.tint(it) }
                )
            }
        },
        modifier = modifier
    )
}