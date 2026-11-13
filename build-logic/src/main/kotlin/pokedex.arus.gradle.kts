import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    "implementation"(libs.findLibrary("arus").get())
    "implementation"(libs.findLibrary("kotlinx-coroutines-android").get())
    "implementation"(libs.findLibrary("androidx-savedstate").get())
    "implementation"(libs.findLibrary("androidx-savedstate-compose").get())
    "implementation"(libs.findLibrary("kotlinx-serialization-json").get())

    "implementation"(platform(libs.findLibrary("compose-bom").get()))
    "implementation"(libs.findLibrary("compose-ui").get())
    "implementation"(libs.findLibrary("compose-foundation").get())
    "implementation"(libs.findLibrary("compose-material3").get())

    "testImplementation"(libs.findLibrary("junit").get())
    "testImplementation"(libs.findLibrary("arus-testing").get())
    "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
    "testImplementation"(libs.findLibrary("turbine").get())
}
