plugins {
    id("pokedex.feature.domain")
}

android {
    namespace = "id.renaldi.pokedex.feature.pokedex.domain"
}

dependencies {
    implementation(libs.koin.core)
}
