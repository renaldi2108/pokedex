package id.renaldi.pokedex.feature.splash.ui

import id.renaldi.pokedex.core.common.Outcome
import id.renaldi.pokedex.core.domain.SyncPokedex
import id.renaldi.pokedex.feature.pokedex.navigation.PokedexRoute
import io.github.renaldi2108.arus.core.ArusController
import io.github.renaldi2108.arus.effects.ArusEffect
import io.github.renaldi2108.arus.nav.Navigator

data class SplashState(
    val syncing: Boolean = true,
    val error: String? = null,
)

private const val DEFAULT_ERROR = "Gagal memuat Pokedex"

class SplashController(
    private val sync: SyncPokedex,
    private val navigator: Navigator,
) : ArusController<SplashState, ArusEffect>(SplashState()) {

    init {
        load()
    }

    fun onRetry() {
        setState { copy(syncing = true, error = null) }
        load()
    }

    private fun load() = launchCatching {
        when (val outcome = sync()) {
            is Outcome.Success -> navigator.resetTo(PokedexRoute)
            is Outcome.Failure -> fail(outcome.error)
        }
    }

    override fun onError(error: Throwable) = fail(error)

    private fun fail(error: Throwable) {
        setState { copy(syncing = false, error = error.message ?: DEFAULT_ERROR) }
        emit(ArusEffect.Snackbar(error.message ?: DEFAULT_ERROR))
    }
}
