plugins {
    id("pokedex.android.library")
}

android {
    namespace = "id.renaldi.pokedex.core.fixtures"
}

dependencies {
    api(projects.core.model)
}
