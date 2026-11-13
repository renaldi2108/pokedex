plugins {
    id("pokedex.android.library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "id.renaldi.pokedex.core.database"
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.generateKotlin", "true")
}

dependencies {
    implementation(libs.koin.core)

    api(projects.core.model)
    implementation(projects.core.common)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.androidx.datastore.preferences)
}
