package io.opengood.gradle

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute.*
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.opengood.gradle.constant.*
import io.opengood.gradle.extension.OpenGoodExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.reflect.KClass

class ConfigPlugin : Plugin<Project> {

    private val extensions = mutableMapOf<KClass<*>, Any>()

    override fun apply(project: Project) {
        createExtensions(project)
        configurePlugins(project)
        configureRepositories(project)
        configureDependencies(project)

        project.afterEvaluate {
            configureTasks(project)
            configureExtensions(project)
        }
    }

    private inline fun <reified T : Any> getExtension(): T {
        return extensions[T::class]
            .takeIf { extensions.containsKey(T::class) }
            .let { extensions[T::class] as T }
    }

    private fun createExtensions(project: Project) {
        extensions[OpenGoodExtension::class] =
            project.extensions.create(
                OpenGoodExtension.EXTENSION_NAME,
                OpenGoodExtension::class.java,
                project)
    }

    private fun configurePlugins(project: Project) {
        when {
            isGroovyProject(project) -> {
                project.plugins.apply(Plugins.GROOVY)
            }
            isJavaProject(project) -> {
                project.plugins.apply(Plugins.JAVA)
                project.plugins.apply(Plugins.LOMBOK)
            }
            isKotlinProject(project) -> {
                project.plugins.apply(Plugins.KOTLIN)
                project.plugins.apply(Plugins.KOTLIN_SPRING)
            }
            else -> {
                throw IllegalStateException("Unable to detect type of Gradle project when configuring plugins")
            }
        }

        project.plugins.apply(Plugins.IDEA)
        project.plugins.apply(Plugins.MAVEN)
        project.plugins.apply(Plugins.MAVEN_PUBLISH)
        project.plugins.apply(Plugins.SPRING_BOOT)
        project.plugins.apply(Plugins.SPRING_DEPENDENCY_MANAGEMENT)
        project.plugins.apply(Plugins.VERSIONS)
    }

    private fun configureRepositories(project: Project) {
        project.repositories.add(project.repositories.mavenCentral())
        project.repositories.add(project.repositories.jcenter())
        project.repositories.add(project.repositories.mavenLocal())
    }

    private fun configureDependencies(project: Project) {
        val implementation = project.configurations.getByName("implementation")
        val annotationProcessor = project.configurations.getByName("annotationProcessor")
        val testImplementation = project.configurations.getByName("testImplementation")
        val testAnnotationProcessor = project.configurations.getByName("testAnnotationProcessor")

        when {
            isGroovyProject(project) -> {
                implementation.dependencies.add(project.dependencies.create(Dependencies.GROOVY))
            }
            isJavaProject(project) -> {
                implementation.dependencies.add(project.dependencies.create(Dependencies.LOMBOK))
                annotationProcessor.dependencies.add(project.dependencies.create(Dependencies.LOMBOK))
                testImplementation.dependencies.add(project.dependencies.create(Dependencies.LOMBOK))
                testAnnotationProcessor.dependencies.add(project.dependencies.create(Dependencies.LOMBOK))
            }
            isKotlinProject(project) -> {
                implementation.dependencies.add(project.dependencies.create(Dependencies.KOTLIN_JDK_STD_LIB))
                implementation.dependencies.add(project.dependencies.create(Dependencies.KOTLIN_REFLECT))
                testImplementation.dependencies.add(project.dependencies.create(Dependencies.KOTLIN_TEST))
                testImplementation.dependencies.add(project.dependencies.create(Dependencies.MOCK_K))
            }
            else -> {
                throw IllegalStateException("Unable to detect type of Gradle project when configuring dependencies")
            }
        }

        implementation.dependencies.add(project.dependencies.create(Dependencies.SPRING_BOOT_CONFIG_PROCESSOR))
        implementation.dependencies.add(project.dependencies.create(Dependencies.SPRING_BOOT_STARTER))
        implementation.dependencies.add(project.dependencies.create(Dependencies.J_COLOR))
        testImplementation.dependencies.add(project.dependencies.create(Dependencies.SPRING_BOOT_STARTER_TEST))
        testImplementation.dependencies.add(project.dependencies.create(Dependencies.JUNIT_JUPITER))
        testImplementation.dependencies.add(project.dependencies.create(Dependencies.ASSERT_J))
    }

    private fun configureTasks(project: Project) {
        when {
            isKotlinProject(project) -> {
                configureKotlinCompileTask(project)
            }
        }

        configureJavaCompileTask(project)
        configureDependencyUpdatesTask(project)
        configureTestTask(project)
    }

    private fun configureKotlinCompileTask(project: Project) {
        project.tasks.withType(KotlinCompile::class.java) {
            it.kotlinOptions {
                freeCompilerArgs = KotlinOptions.FREE_COMPILER_ARGS
                jvmTarget = Versions.JAVA
            }
        }
    }

    private fun configureJavaCompileTask(project: Project) {
        project.tasks.withType(JavaCompile::class.java) {
            it.sourceCompatibility = Versions.JAVA
            it.targetCompatibility = Versions.JAVA
        }
    }

    private fun configureDependencyUpdatesTask(project: Project) {
        val isDependencyVersionNotStable = fun(version: String): Boolean {
            val stableKeywords = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
            val regex = "^[0-9,.v-]+(-r)?$".toRegex()
            val isStable = stableKeywords || regex.matches(version)
            return isStable.not()
        }

        project.tasks.withType(DependencyUpdatesTask::class.java) { it ->
            it.resolutionStrategy { strategy ->
                strategy.componentSelection { component ->
                    component.all {
                        if (isDependencyVersionNotStable(it.candidate.version) &&
                            !isDependencyVersionNotStable(it.currentVersion)) {
                            it.reject("Release candidate")
                        }
                    }
                }
            }
        }
    }

    private fun configureTestTask(project: Project) {
        val extension = getExtension<OpenGoodExtension>()

        project.tasks.withType(Test::class.java) {
            it.useJUnitPlatform()

            it.testLogging { logging ->
                logging.events = Tests.LOGGING_EVENTS
                logging.exceptionFormat = Tests.EXCEPTION_FORMAT
                logging.showCauses = Tests.SHOW_CAUSES
                logging.showExceptions = Tests.SHOW_EXCEPTIONS
                logging.showStackTraces = Tests.SHOW_STACK_TRACES
            }

            it.maxParallelForks = extension.testMaxParallelForks
            it.systemProperty(Tests.INSTANCE_LIFECYCLE_SYS_PROP_NAME, Tests.INSTANCE_LIFECYCLE_SYS_PROP_VALUE)

            it.doFirst {
                println(colorize("***************************************************", CYAN_TEXT()))
                println(colorize(" >> Running Tests", CYAN_TEXT()))
                println(colorize("***************************************************", CYAN_TEXT()))
            }

            it.addTestListener(object : TestListener {
                override fun beforeSuite(suite: TestDescriptor) {}
                override fun beforeTest(testDescriptor: TestDescriptor) {}
                override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
                override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                    if (suite.parent == null) {
                        val output =
                            "Results: ${result.resultType} " +
                                "(" +
                                "${result.testCount} tests, " +
                                "${result.successfulTestCount} successes, " +
                                "${result.failedTestCount} failures, " +
                                "${result.skippedTestCount} skipped" +
                                ")"
                        val startItem = "| "
                        val endItem = " |"
                        val repeatLength = startItem.length + output.length + endItem.length
                        println(colorize("""
                            |
                            |${"-".repeat(repeatLength)}
                            |$startItem$output$endItem
                            |${"-".repeat(repeatLength)}
                            |
                            """.trimMargin(),
                            if (result.failedTestCount == 0L) GREEN_TEXT() else RED_TEXT()
                        ))
                    }
                }
            })

            it.doLast {
                println(colorize("***************************************************", CYAN_TEXT()))
                println(colorize(" >> Tests FINISHED", CYAN_TEXT()))
                println(colorize("***************************************************", CYAN_TEXT()))
            }
        }
    }

    private fun configureExtensions(project: Project) {
        configurePublishingExtension(project)
    }

    private fun configurePublishingExtension(project: Project) {
        project.pluginManager.withPlugin(Plugins.MAVEN_PUBLISH) {
            project.extensions.configure(PublishingExtension::class.java) {
                it.repositories { repos ->
                    repos.maven { repo ->
                        repo.name = Repositories.LOCAL_REPO_NAME
                        repo.url = project.repositories.mavenLocal().url
                    }
                }
            }
        }
    }

    companion object {
        const val PLUGIN_ID = "io.opengood.gradle.config"
    }
}