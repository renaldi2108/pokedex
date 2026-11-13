plugins {
    id("pokedex.feature.ui")
}

android {
    namespace = "id.renaldi.pokedex.feature.pokedex.ui"
}

dependencies {
    implementation(projects.feature.pokedex.domain)
    implementation(projects.feature.favorite.domain)

    implementation(projects.feature.pokedex.presentation.navigation)
    implementation(projects.feature.detail.presentation.navigation)
    implementation(projects.feature.favorite.presentation.navigation)
}
