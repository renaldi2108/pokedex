plugins {
    id("pokedex.android.library")
}

android {
    namespace = "id.renaldi.pokedex.core.domain"
}

dependencies {
    api(projects.core.model)
    api(projects.core.common)
}
