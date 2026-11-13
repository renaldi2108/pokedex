import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.register

class ModuleGuardConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        if (target != target.rootProject) {
            throw GradleException("pokedex.module.guard may only be applied to the root project")
        }

        val guard = target.tasks.register<ModuleGuardTask>("checkModuleDependencies") {
            group = "verification"
            description = "Verifies every inter-module dependency against the allowed matrix."
            allowedDependencies.set(ALLOWED_MATRIX)
        }

        target.gradle.projectsEvaluated {
            val snapshot = LinkedHashMap<String, Set<String>>()
            target.subprojects.forEach { sub ->
                if (sub.subprojects.isNotEmpty()) return@forEach
                val deps = LinkedHashSet<String>()
                sub.configurations.forEach { config ->
                    config.dependencies.forEach { dep ->
                        if (dep is ProjectDependency && dep.path != sub.path) {
                            deps += dep.path
                        }
                    }
                }
                snapshot[sub.path] = deps
            }
            guard.configure { moduleDependencies.set(snapshot) }
        }

        target.subprojects.forEach { sub ->
            sub.tasks.matching { it.name == "check" }.configureEach { dependsOn(guard) }
        }
    }

    private companion object {
        const val UI = "presentation:ui"
        const val NAV = "presentation:navigation"

        val CORE_ALL = listOf(
            ":core:model", ":core:common", ":core:domain", ":core:database",
            ":core:data", ":core:designsystem", ":core:navigation",
            ":core:fixtures", ":core:testing",
        )

        val FEATURE_LAYERS: Map<String, List<String>> = mapOf(
            ":feature:splash" to listOf(UI, NAV),
            ":feature:pokedex" to listOf(UI, NAV, "domain"),
            ":feature:detail" to listOf(UI, NAV, "domain"),
            ":feature:favorite" to listOf(UI, NAV, "domain", "data"),
        )

        val FEATURE_ALL = FEATURE_LAYERS.flatMap { (feature, layers) ->
            layers.map { layer -> "$feature:$layer" }
        }

        val ALLOWED_MATRIX: Map<String, Set<String>> = buildMap {
            put(":core:model", emptySet())
            put(":core:common", setOf(":core:model"))
            put(":core:domain", setOf(":core:model", ":core:common"))
            put(":core:database", setOf(":core:model", ":core:common"))
            put(
                ":core:data",
                setOf(
                    ":core:model", ":core:common", ":core:domain",
                    ":core:database", ":core:fixtures", ":core:testing",
                ),
            )
            put(":core:designsystem", setOf(":core:model", ":core:common"))
            put(":core:navigation", setOf(":core:model"))
            put(":core:fixtures", setOf(":core:model"))
            put(
                ":core:testing",
                setOf(":core:model", ":core:common", ":core:domain", ":core:fixtures"),
            )

            val featureDomains = FEATURE_LAYERS.filterValues { "domain" in it }
                .keys.map { "$it:domain" }.toSet()
            val featureNavigations = FEATURE_LAYERS.keys.map { "$it:$NAV" }.toSet()

            FEATURE_LAYERS.forEach { (feature, layers) ->
                layers.forEach { layer ->
                    val allowed = when (layer) {
                        UI -> setOf(
                            ":core:model", ":core:common", ":core:domain",
                            ":core:designsystem", ":core:navigation",
                            ":core:fixtures", ":core:testing",
                        ) + featureDomains + featureNavigations

                        NAV -> setOf(
                            ":core:model", ":core:common", ":core:navigation",
                            ":core:designsystem", ":core:testing",
                        )

                        "domain" -> setOf(
                            ":core:model", ":core:common", ":core:domain",
                            ":core:fixtures", ":core:testing",
                        ) + (featureDomains - "$feature:domain")

                        else -> setOf(
                            ":core:model", ":core:common", ":core:domain",
                            ":core:database", ":core:data",
                            ":core:fixtures", ":core:testing",
                        ) + featureDomains
                    }
                    put("$feature:$layer", allowed)
                }
            }

            put(":app", (CORE_ALL + FEATURE_ALL).toSet())
        }
    }
}

abstract class ModuleGuardTask : DefaultTask() {

    @get:Input
    abstract val moduleDependencies: MapProperty<String, Set<String>>

    @get:Input
    abstract val allowedDependencies: MapProperty<String, Set<String>>

    @TaskAction
    fun verify() {
        val actual = moduleDependencies.get()
        val allowed = allowedDependencies.get()
        val violations = mutableListOf<String>()

        actual.toSortedMap().forEach { (module, deps) ->
            val permitted = allowed[module]
            if (permitted == null) {
                violations += "$module is not in the matrix - add it before it depends on anything"
                return@forEach
            }
            deps.sorted().forEach { dep ->
                if (dep !in permitted) {
                    violations += "$module  x->  $dep${reasonFor(module, dep)}"
                }
            }
        }

        val missing = allowed.keys - actual.keys
        if (missing.isNotEmpty()) {
            violations += "In the matrix but absent from the build: ${missing.sorted().joinToString(", ")}"
        }

        if (violations.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("Module dependency violations:")
                    appendLine()
                    violations.forEach { appendLine("  - $it") }
                    appendLine()
                    appendLine("Fix the dependency rather than loosening the matrix.")
                },
            )
        }

        logger.lifecycle("[module-guard] ${actual.size} modules verified against the matrix")
    }

    private fun reasonFor(module: String, dep: String): String = when {
        module.startsWith(":feature:") && dep.startsWith(":feature:") &&
            featureOf(module) != featureOf(dep) ->
            "   (across features only :domain or :presentation:navigation is reachable)"

        module.startsWith(":feature:") && (dep == ":core:data" || dep == ":core:database") ->
            "   (only a feature :data layer may touch :core:data/:core:database)"

        dep.startsWith(":app") ->
            "   (nothing may depend on :app)"

        else -> "   (not in the matrix)"
    }

    private fun featureOf(path: String): String = path.split(":").take(3).joinToString(":")
}
