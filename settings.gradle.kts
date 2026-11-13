@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Pokedex"

include(":app")

include(":core:model")
include(":core:common")
include(":core:domain")
include(":core:database")
include(":core:data")
include(":core:designsystem")
include(":core:navigation")
include(":core:fixtures")
include(":core:testing")

include(":feature:splash:presentation:navigation")
include(":feature:splash:presentation:ui")

include(":feature:pokedex:domain")
include(":feature:pokedex:presentation:navigation")
include(":feature:pokedex:presentation:ui")

include(":feature:detail:domain")
include(":feature:detail:presentation:navigation")
include(":feature:detail:presentation:ui")

include(":feature:favorite:domain")
include(":feature:favorite:data")
include(":feature:favorite:presentation:navigation")
include(":feature:favorite:presentation:ui")
