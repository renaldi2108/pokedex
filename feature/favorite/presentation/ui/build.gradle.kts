plugins {
    id("pokedex.feature.ui")
}

android {
    namespace = "id.renaldi.pokedex.feature.favorite.ui"
}

dependencies {
    implementation(projects.feature.favorite.domain)

    implementation(projects.feature.favorite.presentation.navigation)
    implementation(projects.feature.detail.presentation.navigation)
}
