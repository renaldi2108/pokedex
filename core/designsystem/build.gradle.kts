plugins {
    id("pokedex.android.library")
}

android {
    namespace = "id.renaldi.pokedex.core.designsystem"
}

dependencies {
    api(projects.core.model)
    implementation(projects.core.common)

    implementation(libs.androidx.core.ktx)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.ui.graphics)
    implementation(libs.coil.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
}
