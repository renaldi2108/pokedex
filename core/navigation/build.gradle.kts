plugins {
    id("pokedex.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "id.renaldi.pokedex.core.navigation"
}

dependencies {
    implementation(projects.core.model)
}
