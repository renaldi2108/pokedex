plugins {
    id("pokedex.feature.domain")
}

android {
    namespace = "id.renaldi.pokedex.feature.detail.domain"
}

dependencies {
    implementation(libs.koin.core)
}
