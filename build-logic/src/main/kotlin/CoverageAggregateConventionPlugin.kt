import id.renaldi.pokedex.buildlogic.coverageClassTree
import id.renaldi.pokedex.buildlogic.coverageExecFiles
import id.renaldi.pokedex.buildlogic.coverageSourceDirs
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.math.BigDecimal

class CoverageAggregateConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        if (target != target.rootProject) {
            throw GradleException("pokedex.coverage.aggregate may only be applied to the root project")
        }

        target.pluginManager.apply(JacocoPlugin::class.java)

        val covered = { target.subprojects.filter { it.plugins.hasPlugin(JacocoPlugin::class.java) } }

        val report = target.tasks.register<JacocoReport>("coverageReport") {
            group = "verification"
            description = "Aggregated unit-test coverage across every module."
            reports {
                xml.required.set(true)
                html.required.set(true)
            }
        }

        val verify = target.tasks.register<JacocoCoverageVerification>("coverageVerify") {
            group = "verification"
            description = "Fails unless every covered class reaches 100% line coverage."
            dependsOn(report)
            violationRules {
                rule {
                    limit {
                        counter = "LINE"
                        value = "COVEREDRATIO"
                        minimum = BigDecimal.ONE
                    }
                }
            }
        }

        target.gradle.projectsEvaluated {
            val modules = covered()
            val tests = modules.flatMap { module ->
                module.tasks.withType<org.gradle.api.tasks.testing.Test>().matching {
                    it.name == "testDebugUnitTest"
                }
            }
            listOf(report, verify).forEach { task ->
                task.configure {
                    dependsOn(tests)
                    executionData.setFrom(target.files(modules.map { it.coverageExecFiles() }))
                    sourceDirectories.setFrom(
                        target.files(
                            modules.flatMap { module ->
                                module.coverageSourceDirs().map { module.file(it) }
                            },
                        ),
                    )
                    classDirectories.setFrom(target.files(modules.map { it.coverageClassTree() }))
                }
            }
        }
    }
}
