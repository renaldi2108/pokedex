plugins {
    id("pokedex.feature.ui")
}

android {
    namespace = "id.renaldi.pokedex.feature.splash.ui"
}

dependencies {
    implementation(projects.feature.splash.presentation.navigation)
    implementation(projects.feature.pokedex.presentation.navigation)
}
