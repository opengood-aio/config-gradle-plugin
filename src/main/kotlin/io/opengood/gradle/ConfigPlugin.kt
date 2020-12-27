package io.opengood.gradle

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute.*
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.opengood.gradle.builder.groovy.withGroovyBuilder
import io.opengood.gradle.constant.*
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import io.opengood.gradle.extension.OpenGoodExtension
import net.researchgate.release.BaseScmAdapter
import net.researchgate.release.GitAdapter
import net.researchgate.release.ReleaseExtension
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
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.dsl.SpringBootExtension
import org.springframework.boot.gradle.tasks.bundling.BootJar

class ConfigPlugin : Plugin<Project> {

    private lateinit var extension: OpenGoodExtension

    override fun apply(project: Project) {
        createExtension(project)
        configurePlugins(project)
        configureRepositories(project)

        project.afterEvaluate {
            configurePlugins(project, afterEval = true)
            configureConventions(project)
            configureDependencies(project)
            configureTasks(project)
            configureArtifacts(project)
            configureExtensions(project)
        }
    }

    private fun createExtension(project: Project) {
        extension = project.extensions.create(
            OpenGoodExtension.EXTENSION_NAME,
            OpenGoodExtension::class.java,
            project
        )
    }

    private fun configurePlugins(project: Project, afterEval: Boolean = false) {
        with(project) {
            with(pluginManager) {
                if (afterEval) {
                    with(extension) {
                        with(main) {
                            if (projectType == ProjectType.LIB) {
                                apply(Plugins.JAVA_LIBRARY)
                            }
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
                            apply(Plugins.KOTLIN_JVM)
                            apply(Plugins.KOTLIN_ALL_OPEN)
                            apply(Plugins.KOTLIN_SPRING)
                        }
                    }

                    apply(Plugins.BASE)
                    apply(Plugins.IDEA)
                    apply(Plugins.MAVEN)
                    apply(Plugins.MAVEN_PUBLISH)
                    apply(Plugins.RELEASE)
                    apply(Plugins.SIGNING)
                    apply(Plugins.VERSIONS)
                    apply(Plugins.SPRING_BOOT)
                    apply(Plugins.SPRING_DEPENDENCY_MANAGEMENT)
                }
            }
        }
    }

    private fun configureConventions(project: Project) {
        with(project) {
            with(extension) {
                val basePluginConvention = convention.getPlugin(BasePluginConvention::class.java)
                with(basePluginConvention) {
                    archivesBaseName = artifact.name
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
                    with(extension) {
                        with(features) {
                            when (languageType) {
                                LanguageType.GROOVY -> {
                                    implementation.dependencies.add(create(Dependencies.GROOVY))
                                }
                                LanguageType.JAVA -> {
                                    if (lombok) {
                                        implementation.dependencies.add(create(Dependencies.LOMBOK))
                                        annotationProcessor.dependencies.add(create(Dependencies.LOMBOK))
                                        testImplementation.dependencies.add(create(Dependencies.LOMBOK))
                                        testAnnotationProcessor.dependencies.add(create(Dependencies.LOMBOK))
                                    }
                                }
                                LanguageType.KOTLIN -> {
                                    implementation.dependencies.add(create(Dependencies.KOTLIN_STD_LIB))
                                    implementation.dependencies.add(create(Dependencies.KOTLIN_REFLECT))
                                    if (kotest) {
                                        testImplementation.dependencies.add(create(Dependencies.KOTEST_JUNIT_RUNNER))
                                        testImplementation.dependencies.add(create(Dependencies.KOTEST_JUNIT_EXTENSIONS))
                                        testImplementation.dependencies.add(create(Dependencies.KOTEST_SPRING_EXTENSIONS))
                                        testImplementation.dependencies.add(create(Dependencies.KOTEST_KOIN_EXTENSIONS))
                                    }
                                    if (mockk) {
                                        testImplementation.dependencies.add(create(Dependencies.MOCKK))
                                    }
                                }
                            }

                            if (spring) {
                                implementation.dependencies.add(create(Dependencies.SPRING_BOOT_STARTER))
                                annotationProcessor.dependencies.add(create(Dependencies.SPRING_BOOT_CONFIG_PROCESSOR))
                                testImplementation.dependencies.add(create(Dependencies.SPRING_BOOT_STARTER_TEST))
                            }

                            with(test) {
                                if (languageType != LanguageType.KOTLIN ||
                                    (languageType == LanguageType.KOTLIN && multipleFrameworks.kotlin)
                                ) {
                                    if (junit) {
                                        testImplementation.dependencies.add(create(Dependencies.JUNIT_JUPITER))
                                    }
                                    if (assertj) {
                                        testImplementation.dependencies.add(create(Dependencies.ASSERTJ))
                                    }
                                    if (mockito) {
                                        testImplementation.dependencies.add(create(Dependencies.MOCKITO))
                                        testImplementation.dependencies.add(create(Dependencies.MOCKITO_JUNIT_JUPITER))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun configureTasks(project: Project) {
        configureGradleWrapperTask(project)

        with(extension) {
            with(main) {
                if (languageType == LanguageType.KOTLIN) {
                    configureKotlinCompileTask(project)
                }
            }
            with(features) {
                if (publishing) {
                    configureAfterReleaseBuildTask(project)
                    configureUploadArchivesTask(project)
                }
            }
        }

        configureJavaCompileTask(project)
        configureProcessResourcesTask(project)
        configureDependencyUpdatesTask(project)
        configureTestTask(project)
        configureJarTask(project)
        configureBootJarTask(project)
    }

    private fun configureGradleWrapperTask(project: Project) {
        project.tasks.withType(Wrapper::class.java) {
            it.distributionType = DistributionType.ALL
        }
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

    private fun configureProcessResourcesTask(project: Project) {
        with(extension.main) {
            if (projectType == ProjectType.APP) {
                project.tasks.withType(ProcessResources::class.java) {
                    it.filesMatching(Resources.APPLICATION_PROPERTIES) { file ->
                        file.expand(project.properties)
                    }
                }
            }
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
                            !isDependencyVersionNotStable(it.currentVersion)
                        ) {
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
                        println(
                            colorize(
                                """
                            |
                            |${"-".repeat(repeatLength)}
                            |$startItem$output$endItem
                            |${"-".repeat(repeatLength)}
                            |
                            """.trimMargin(),
                                if (result.failedTestCount == 0L) GREEN_TEXT() else RED_TEXT()
                            )
                        )
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
        with(extension.main) {
            if (projectType == ProjectType.LIB) {
                project.tasks.withType(Jar::class.java).getByName("jar") {
                    it.enabled = true
                }
            }
        }
    }

    private fun configureBootJarTask(project: Project) {
        with(extension) {
            with(main) {
                with(features) {
                    if (!spring || projectType == ProjectType.LIB) {
                        project.tasks.withType(BootJar::class.java).getByName("bootJar") {
                            it.enabled = false
                        }
                    }
                }
            }
        }
    }

    private fun configureAfterReleaseBuildTask(project: Project) {
        project.tasks.getByName("afterReleaseBuild") {
            it.dependsOn("uploadArchives")
        }
    }

    private fun configureUploadArchivesTask(project: Project) {
        project.tasks.withType(Upload::class.java) {
            val ossrhUsername = project.getProperty("ossrhUsername", getEnv("OSSRH_USERNAME", ""))
            val ossrhPassword = project.getProperty("ossrhPassword", getEnv("OSSRH_PASSWORD", ""))

            if (ossrhUsername.isBlank()) println("WARN: ossrhUsername property or OSSRH_USERNAME environment variable is not set")
            if (ossrhPassword.isBlank()) println("WARN: ossrhPassword property or OSSRH_PASSWORD environment variable is not set")

            val mavenConvention =
                DslObject(it.repositories).convention.getPlugin(MavenRepositoryHandlerConvention::class.java)

            mavenConvention.mavenDeployer { mavenDeployer ->
                with(mavenDeployer) {
                    beforeDeployment { deployment ->
                        val signing = project.getExtension<SigningExtension>()
                        signing.signPom(deployment)
                    }

                    with(extension.artifact) {
                        with(repo) {
                            mavenDeployer.withGroovyBuilder {
                                "repository"("url" to stagingRepoUri) {
                                    "authentication"(
                                        "userName" to ossrhUsername,
                                        "password" to ossrhPassword
                                    )
                                }
                                "snapshotRepository"("url" to snapshotsRepoUri) {
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
                                        with(scm) {
                                            "connection"(connection)
                                            "developerConnection"(developerConnection)
                                            "url"(uri)
                                        }
                                    }
                                    "licenses" {
                                        "license" {
                                            with(license) {
                                                "name"(name)
                                                "url"(uri)
                                            }
                                        }
                                    }
                                    "developers" {
                                        "developer" {
                                            with(developer) {
                                                "id"(id)
                                                "name"(name)
                                                "email"(email)
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
            with(extension.main) {
                if (projectType == ProjectType.LIB) {
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
            }

            val jar = tasks.getByName("jar") as Jar
            artifacts.add("archives", jar)
        }
    }

    private fun configureExtensions(project: Project) {
        configureSpringBootExtension(project)
        configureReleaseExtension(project)

        with(extension.features) {
            if (publishing) {
                configurePublishingExtension(project)
                configureSigningExtension(project)
            }
        }
    }

    private fun configureSpringBootExtension(project: Project) {
        with(extension.main) {
            if (projectType == ProjectType.APP) {
                with(project) {
                    pluginManager.withPlugin(Plugins.SPRING_BOOT) {
                        extensions.configure(SpringBootExtension::class.java) {
                            it.buildInfo()
                        }
                    }
                }
            }
        }
    }

    private fun configureReleaseExtension(project: Project) {
        with(project) {
            pluginManager.withPlugin(Plugins.RELEASE) {
                extensions.configure(ReleaseExtension::class.java) {
                    it.scmAdapters = mutableListOf<Class<out BaseScmAdapter>>(GitAdapter::class.java)
                    it.preTagCommitMessage = Releases.PRE_TAG_COMMIT_MESSAGE
                    it.newVersionCommitMessage = Releases.NEW_VERSION_COMMIT_MESSAGE
                    it.versionPatterns = Releases.VERSION_PATTERNS
                    it.git {
                        with(extension) {
                            requireBranch = release.requireBranch
                            pushToRemote = release.pushToRemote
                        }
                    }
                }
            }
        }
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
                    val signingKey = getEnv("GPG_SIGNING_PRIVATE_KEY", "")
                    val signingPassword = getEnv("GPG_SIGNING_PASSWORD", "")

                    if (signingKey.isNotBlank() && signingPassword.isNotBlank()) {
                        println("Environment variables GPG_SIGNING_PRIVATE_KEY and GPG_SIGNING_PASSWORD are set")
                        println("Using in-memory GPG key for signing")
                        it.useInMemoryPgpKeys(signingKey, signingPassword)
                    } else {
                        println("Environment variables GPG_SIGNING_PRIVATE_KEY and GPG_SIGNING_PASSWORD are not set")
                        println("Defaulting to global Gradle properties file for GPG key for signing")
                    }

                    it.sign(configurations.getByName("archives"))
                }
            }
        }
    }

    companion object {
        const val PLUGIN_ID = "io.opengood.gradle.config"
    }
}
