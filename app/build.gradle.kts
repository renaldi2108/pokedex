import id.renaldi.pokedex.buildlogic.coverageClassTree
import id.renaldi.pokedex.buildlogic.coverageExecFiles
import id.renaldi.pokedex.buildlogic.coverageSourceDirs

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    id("pokedex.arus")
    jacoco
}

android {
    namespace = "id.renaldi.pokedex"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "id.renaldi.pokedex"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 2
        versionName = "2.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            enableUnitTestCoverage = true
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    packaging {
        resources.excludes += setOf(
            "/META-INF/{AL2.0,LGPL2.1}",
            "/META-INF/versions/9/previous-compilation-data.bin",
        )
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

tasks.register<JacocoReport>("coverageReport") {
    group = "verification"
    dependsOn("testDebugUnitTest")
    executionData.setFrom(coverageExecFiles())
    sourceDirectories.setFrom(files(coverageSourceDirs()))
    classDirectories.setFrom(coverageClassTree())
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.common)
    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)

    implementation(projects.feature.splash.presentation.navigation)
    implementation(projects.feature.splash.presentation.ui)
    implementation(projects.feature.pokedex.domain)
    implementation(projects.feature.pokedex.presentation.navigation)
    implementation(projects.feature.pokedex.presentation.ui)
    implementation(projects.feature.detail.domain)
    implementation(projects.feature.detail.presentation.navigation)
    implementation(projects.feature.detail.presentation.ui)
    implementation(projects.feature.favorite.domain)
    implementation(projects.feature.favorite.data)
    implementation(projects.feature.favorite.presentation.navigation)
    implementation(projects.feature.favorite.presentation.ui)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)

    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    testImplementation(projects.core.testing)
}
