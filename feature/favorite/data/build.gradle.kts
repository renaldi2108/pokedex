plugins {
    id("pokedex.android.library")
}

android {
    namespace = "id.renaldi.pokedex.feature.favorite.data"
}

dependencies {
    implementation(libs.koin.core)

    api(projects.core.model)
    implementation(projects.core.common)
    api(projects.core.domain)
    implementation(projects.core.database)

    testImplementation(libs.androidx.datastore.preferences)
    testImplementation(projects.core.testing)
    testImplementation(projects.core.fixtures)
}
