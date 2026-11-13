import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

plugins {
    id("pokedex.android.library")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    "implementation"(project(":core:model"))
    "implementation"(project(":core:common"))
    "implementation"(project(":core:domain"))
    "implementation"(project(":core:designsystem"))
    "implementation"(project(":core:navigation"))

    "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel").get())
    "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
    "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-savedstate").get())
    "implementation"(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
    "implementation"(libs.findLibrary("compose-material-icons-extended").get())
    "implementation"(libs.findLibrary("koin-core").get())
    "implementation"(libs.findLibrary("koin-compose").get())
    "implementation"(libs.findLibrary("compose-ui-tooling-preview").get())
    "debugImplementation"(libs.findLibrary("compose-ui-tooling").get())

    "testImplementation"(project(":core:testing"))
    "testImplementation"(project(":core:fixtures"))
}
