package io.opengood.gradle

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute.*
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.opengood.gradle.builder.groovy.withGroovyBuilder
import io.opengood.gradle.constant.*
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import io.opengood.gradle.extension.OpenGoodExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.plugins.DslObject
import org.gradle.api.plugins.BasePluginConvention
import org.gradle.api.plugins.MavenRepositoryHandlerConvention
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.Upload
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

class ConfigPlugin : Plugin<Project> {

    private lateinit var languageType: LanguageType
    private lateinit var extension: OpenGoodExtension

    override fun apply(project: Project) {
        languageType = project.languageType

        createExtensions(project)
        configurePlugins(project)
        configureRepositories(project)
        configureDependencies(project)

        project.afterEvaluate {
            configurePlugins(project, afterEval = true)
            configureTasks(project)
            configureArtifacts(project)
            configureExtensions(project)
        }
    }

    private fun createExtensions(project: Project) {
        extension = project.extensions.create(
            OpenGoodExtension.EXTENSION_NAME,
            OpenGoodExtension::class.java,
            project,
            languageType
        )
    }

    private fun configurePlugins(project: Project, afterEval: Boolean = false) {
        with(project) {
            with(plugins) {
                if (afterEval) {
                    with(extension) {
                        val basePluginConvention = convention.getPlugin(BasePluginConvention::class.java)
                        basePluginConvention.archivesBaseName = artifact.archiveBaseName

                        if (main.projectType == ProjectType.LIB) {
                            apply(Plugins.JAVA_LIBRARY)
                        }
                    }
                } else {
                    when (languageType) {
                        LanguageType.GROOVY -> {
                            apply(Plugins.GROOVY)
                        }
                        LanguageType.JAVA -> {
                            apply(Plugins.JAVA)
                            apply(Plugins.LOMBOK)
                        }
                        LanguageType.KOTLIN -> {
                            apply(Plugins.KOTLIN)
                            apply(Plugins.KOTLIN_SPRING)
                        }
                    }

                    apply(Plugins.BASE)
                    apply(Plugins.IDEA)
                    apply(Plugins.MAVEN)
                    apply(Plugins.MAVEN_PUBLISH)
                    apply(Plugins.SIGNING)
                    apply(Plugins.SPRING_BOOT)
                    apply(Plugins.SPRING_DEPENDENCY_MANAGEMENT)
                    apply(Plugins.VERSIONS)
                }
            }
        }
    }

    private fun configureRepositories(project: Project) {
        with(project.repositories) {
            add(mavenCentral())
            add(jcenter())
            add(gradlePluginPortal())
            add(mavenLocal())
        }
    }

    private fun configureDependencies(project: Project) {
        with(project) {
            with(configurations) {
                val implementation = getByName("implementation")
                val annotationProcessor = getByName("annotationProcessor")
                val testImplementation = getByName("testImplementation")
                val testAnnotationProcessor = getByName("testAnnotationProcessor")

                with(dependencies) {
                    when (languageType) {
                        LanguageType.GROOVY -> {
                            implementation.dependencies.add(create(Dependencies.GROOVY))
                        }
                        LanguageType.JAVA -> {
                            implementation.dependencies.add(create(Dependencies.LOMBOK))
                            annotationProcessor.dependencies.add(create(Dependencies.LOMBOK))
                            testImplementation.dependencies.add(create(Dependencies.LOMBOK))
                            testAnnotationProcessor.dependencies.add(create(Dependencies.LOMBOK))
                        }
                        LanguageType.KOTLIN -> {
                            implementation.dependencies.add(create(Dependencies.KOTLIN_STD_LIB))
                            implementation.dependencies.add(create(Dependencies.KOTLIN_REFLECT))
                            testImplementation.dependencies.add(create(Dependencies.KO_TEST))
                            testImplementation.dependencies.add(create(Dependencies.MOCK_K))
                        }
                    }

                    implementation.dependencies.add(create(Dependencies.SPRING_BOOT_STARTER))
                    annotationProcessor.dependencies.add(create(Dependencies.SPRING_BOOT_CONFIG_PROCESSOR))
                    testImplementation.dependencies.add(create(Dependencies.SPRING_BOOT_STARTER_TEST))
                    testImplementation.dependencies.add(create(Dependencies.JUNIT_JUPITER))
                    testImplementation.dependencies.add(create(Dependencies.ASSERT_J))
                }
            }
        }
    }

    private fun configureTasks(project: Project) {
        if (languageType == LanguageType.KOTLIN) {
            configureKotlinCompileTask(project)
        }

        configureJavaCompileTask(project)
        configureDependencyUpdatesTask(project)
        configureTestTask(project)
        configureJarTask(project)
        configureBootJarTask(project)
        configureUploadArchivesTask(project)
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
            val stableKeywords = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase() in it }
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
        project.tasks.withType(Test::class.java) {
            it.useJUnitPlatform()

            it.testLogging { logging ->
                logging.apply {
                    events = Tests.LOGGING_EVENTS
                    exceptionFormat = Tests.EXCEPTION_FORMAT
                    showCauses = Tests.SHOW_CAUSES
                    showExceptions = Tests.SHOW_EXCEPTIONS
                    showStackTraces = Tests.SHOW_STACK_TRACES
                }
            }

            it.maxParallelForks = extension.test.maxParallelForks
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

    private fun configureJarTask(project: Project) {
        if (extension.main.projectType == ProjectType.LIB) {
            project.tasks.withType(Jar::class.java).getByName("jar") {
                it.enabled = true
            }
        }
    }

    private fun configureBootJarTask(project: Project) {
        if (extension.main.projectType == ProjectType.LIB) {
            project.tasks.withType(BootJar::class.java).getByName("bootJar") {
                it.enabled = false
            }
        }
    }

    private fun configureUploadArchivesTask(project: Project) {
        project.tasks.withType(Upload::class.java) {
            val ossrhUsername = project.getProperty("ossrhUsername", getEnvVar("OSSRH_USERNAME", ""))
            val ossrhPassword = project.getProperty("ossrhPassword", getEnvVar("OSSRH_USERNAME", ""))

            val mavenConvention = DslObject(it.repositories).convention.getPlugin(MavenRepositoryHandlerConvention::class.java)
            mavenConvention.mavenDeployer { mavenDeployer ->
                with(mavenDeployer) {
                    beforeDeployment { deployment ->
                        val signing = project.getExtension<SigningExtension>()
                        signing.signPom(deployment)
                    }

                    with(extension.artifact) {
                        with(mavenDeployer) {
                            withGroovyBuilder {
                                "repository"("url" to stagingUri) {
                                    "authentication"(
                                        "userName" to ossrhUsername,
                                        "password" to ossrhPassword
                                    )
                                }
                                "snapshotRepository"("url" to snapshotsUri) {
                                    "authentication"(
                                        "userName" to ossrhUsername,
                                        "password" to ossrhPassword
                                    )
                                }
                            }
                        }

                        pom.project { pom ->
                            with(pom) {
                                withGroovyBuilder {
                                    "groupId"(project.group)
                                    "artifactId"(project.name)
                                    "version"(project.version)
                                    "name"(name)
                                    "packaging"(packaging)
                                    "description"(description)
                                    "url"(uri)
                                    "scm" {
                                        "connection"(scm.connection)
                                        "developerConnection"(scm.developerConnection)
                                        "url"(scm.uri)
                                    }
                                    "licenses" {
                                        licenses.forEach { license ->
                                            "license" {
                                                "name"(license.name)
                                                "url"(license.uri)
                                            }
                                        }
                                    }
                                    "developers" {
                                        developers.forEach { dev ->
                                            "developer" {
                                                "id"(dev.id)
                                                "name"(dev.name)
                                                "email"(dev.email)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun configureArtifacts(project: Project) {
        with(project) {
            if (extension.main.projectType == ProjectType.LIB) {
                val sourcesJar = tasks.create("sourcesJar", Jar::class.java) {
                    val sourceSets = project.getExtension<SourceSetContainer>("sourceSets")
                    it.dependsOn("classes")
                    it.from(sourceSets.getByName("main").allSource)
                    it.archiveClassifier.set("sources")
                }
                artifacts.add("archives", sourcesJar)

                val javadocJar = tasks.create("javadocJar", Jar::class.java) {
                    val javadoc = tasks.getByName("javadoc") as Javadoc
                    it.dependsOn.add(javadoc)
                    it.from(javadoc)
                    it.archiveClassifier.set("javadoc")
                }
                artifacts.add("archives", javadocJar)
            }

            val jar = tasks.getByName("jar") as Jar
            artifacts.add("archives", jar)
        }
    }

    private fun configureExtensions(project: Project) {
        configurePublishingExtension(project)
        configureSigningExtension(project)
    }

    private fun configurePublishingExtension(project: Project) {
        with(project) {
            pluginManager.withPlugin(Plugins.MAVEN_PUBLISH) {
                extensions.configure(PublishingExtension::class.java) {
                    it.publications { publications ->
                        publications.register("mavenJava", MavenPublication::class.java) { publication ->
                            publication.from(components.getByName("java"))
                        }
                    }

                    it.repositories { repos ->
                        repos.maven { repo ->
                            repo.name = Repositories.LOCAL_REPO_NAME
                            repo.url = repositories.mavenLocal().url
                        }
                    }
                }
            }
        }
    }

    private fun configureSigningExtension(project: Project) {
        with(project) {
            pluginManager.withPlugin(Plugins.SIGNING) {
                extensions.configure(SigningExtension::class.java) {
                    it.sign(configurations.getByName("archives"))
                }
            }
        }
    }

    companion object {
        const val PLUGIN_ID = "io.opengood.gradle.config"
    }
}