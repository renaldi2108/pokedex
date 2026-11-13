plugins {
    `kotlin-dsl`
}

group = "id.renaldi.pokedex.buildlogic"

dependencies {
    implementation(libs.plugin.android.gradle)
    implementation(libs.plugin.kotlin.compose.gradle)
}

kotlin {
    jvmToolchain(17)
}

gradlePlugin {
    plugins {
        register("moduleGuard") {
            id = "pokedex.module.guard"
            implementationClass = "ModuleGuardConventionPlugin"
        }
        register("coverageAggregate") {
            id = "pokedex.coverage.aggregate"
            implementationClass = "CoverageAggregateConventionPlugin"
        }
    }
}
