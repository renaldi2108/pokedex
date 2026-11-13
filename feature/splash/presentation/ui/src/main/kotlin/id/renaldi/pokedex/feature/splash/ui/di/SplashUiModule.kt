package id.renaldi.pokedex.feature.splash.ui.di

import id.renaldi.pokedex.feature.splash.ui.SplashController
import io.github.renaldi2108.arus.nav.Navigator
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val splashUiModule: Module = module {
    viewModel { (navigator: Navigator) -> SplashController(get(), navigator) }
}
