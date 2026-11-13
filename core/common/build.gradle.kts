plugins {
    id("pokedex.android.library")
}

android {
    namespace = "id.renaldi.pokedex.core.common"
}

dependencies {
    api(projects.core.model)
}
