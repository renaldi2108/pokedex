plugins {
    id("pokedex.feature.domain")
}

android {
    namespace = "id.renaldi.pokedex.feature.favorite.domain"
}

dependencies {
    implementation(libs.koin.core)
}
