package id.renaldi.pokedex.buildlogic

import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree

val coverageExclusions: List<String> = listOf(
    "**/ComposableSingletons*",
    "**/*\$\$serializer*",
    "**/*_Impl*",
    "**/*_Factory*",
    "**/graphql/**",
    "**/di/**",
    "**/*Screen*",
    "**/*Sheet*",
    "**/*Components*",
    "**/*Theme*",
    "**/*Entries*",
    "**/*Preview*",
    "**/*Activity*",
    "**/SystemBars*",
    "**/PokedexApp*",
    "**/*Backdrop*",
    "**/*Artwork*",
    "**/*Apollo*",
    "**/PokedexDatabase*",
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig*",
)

fun Project.coverageClassTree(): ConfigurableFileTree =
    fileTree(layout.buildDirectory.dir("intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes")) {
        exclude(coverageExclusions)
    }

fun Project.coverageSourceDirs(): List<String> = listOf("src/main/kotlin", "src/main/java")

fun Project.coverageExecFiles(): ConfigurableFileTree =
    fileTree(layout.buildDirectory) {
        include("outputs/unit_test_code_coverage/debugUnitTest/*.exec")
    }
