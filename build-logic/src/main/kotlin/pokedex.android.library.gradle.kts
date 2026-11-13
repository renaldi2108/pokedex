import com.android.build.api.dsl.LibraryExtension
import id.renaldi.pokedex.buildlogic.coverageClassTree
import id.renaldi.pokedex.buildlogic.coverageExecFiles
import id.renaldi.pokedex.buildlogic.coverageSourceDirs
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
    id("pokedex.arus")
    jacoco
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

extensions.configure<LibraryExtension> {
    compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()

    defaultConfig {
        minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        getByName("debug") {
            enableUnitTestCoverage = true
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

extensions.configure<JacocoPluginExtension> {
    toolVersion = libs.findVersion("jacoco").get().requiredVersion
}

tasks.register<JacocoReport>("coverageReport") {
    group = "verification"
    description = "Unit-test coverage for this module."
    dependsOn("testDebugUnitTest")
    executionData.setFrom(coverageExecFiles())
    sourceDirectories.setFrom(files(coverageSourceDirs()))
    classDirectories.setFrom(coverageClassTree())
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
