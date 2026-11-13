plugins {
    id("pokedex.android.library")
}

dependencies {
    "api"(project(":core:model"))
    "implementation"(project(":core:common"))
    "api"(project(":core:domain"))
    "testImplementation"(project(":core:testing"))
    "testImplementation"(project(":core:fixtures"))
}
