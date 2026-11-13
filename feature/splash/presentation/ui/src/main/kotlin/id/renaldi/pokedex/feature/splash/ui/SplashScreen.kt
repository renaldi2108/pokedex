package id.renaldi.pokedex.feature.splash.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import id.renaldi.pokedex.core.designsystem.component.NavigationBarScrim
import id.renaldi.pokedex.core.designsystem.component.PokeballBackdrop
import id.renaldi.pokedex.core.designsystem.system.SystemBarIcons
import id.renaldi.pokedex.core.designsystem.token.Spacing
import io.github.renaldi2108.arus.core.select

@Composable
fun SplashScreen(controller: SplashController) {
    val syncing by controller.select { it.syncing }
    val error by controller.select { it.error }

    SystemBarIcons(darkStatusBarIcons = !isSystemInDarkTheme())

    val transition = rememberInfiniteTransition(label = "splash")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600),
            repeatMode = RepeatMode.Restart,
        ),
        label = "splash-spin",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.xl),
            modifier = Modifier.padding(Spacing.xxl),
        ) {
            PokeballBackdrop(
                tint = MaterialTheme.colorScheme.primary,
                alpha = 1f,
                modifier = Modifier.rotate(if (syncing) angle else 0f),
            )
            Text(
                text = "Pokedex",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            if (error != null) {
                Text(
                    text = error.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )
                Button(
                    onClick = controller::onRetry,
                    modifier = Modifier.testTag("splash_retry"),
                ) {
                    Text("Coba lagi")
                }
            }
        }

        NavigationBarScrim(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
