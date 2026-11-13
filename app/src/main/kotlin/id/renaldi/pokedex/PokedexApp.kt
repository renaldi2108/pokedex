package id.renaldi.pokedex

import android.app.Application
import id.renaldi.pokedex.core.data.di.dataModule
import id.renaldi.pokedex.core.database.di.databaseModule
import id.renaldi.pokedex.feature.detail.domain.di.detailDomainModule
import id.renaldi.pokedex.feature.detail.ui.di.detailUiModule
import id.renaldi.pokedex.feature.favorite.data.di.favoriteDataModule
import id.renaldi.pokedex.feature.favorite.domain.di.favoriteDomainModule
import id.renaldi.pokedex.feature.favorite.ui.di.favoriteUiModule
import id.renaldi.pokedex.feature.pokedex.domain.di.pokedexDomainModule
import id.renaldi.pokedex.feature.pokedex.ui.di.pokedexUiModule
import id.renaldi.pokedex.feature.splash.ui.di.splashUiModule
import io.github.renaldi2108.arus.core.Arus
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PokedexApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Arus.debugLogging = BuildConfig.DEBUG
        startKoin {
            androidContext(this@PokedexApp)
            modules(
                databaseModule,
                dataModule,
                favoriteDataModule,
                pokedexDomainModule,
                detailDomainModule,
                favoriteDomainModule,
                splashUiModule,
                pokedexUiModule,
                detailUiModule,
                favoriteUiModule,
            )
        }
    }
}
