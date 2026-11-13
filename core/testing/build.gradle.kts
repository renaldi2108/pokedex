plugins {
    id("pokedex.android.library")
}

android {
    namespace = "id.renaldi.pokedex.core.testing"
}

dependencies {
    api(projects.core.model)
    api(projects.core.domain)
    api(projects.core.fixtures)
    implementation(projects.core.common)

    api(libs.junit)
    api(libs.arus.testing)
    api(libs.kotlinx.coroutines.test)
    api(libs.turbine)
}
