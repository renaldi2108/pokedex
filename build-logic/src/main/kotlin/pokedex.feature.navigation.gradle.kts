plugins {
    id("pokedex.android.library")
}

dependencies {
    "api"(project(":core:navigation"))
    "implementation"(project(":core:model"))
}
