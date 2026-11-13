plugins {
    id("pokedex.feature.ui")
}

android {
    namespace = "id.renaldi.pokedex.feature.detail.ui"
}

dependencies {
    implementation(projects.feature.detail.domain)
    implementation(projects.feature.favorite.domain)

    implementation(projects.feature.detail.presentation.navigation)
}
