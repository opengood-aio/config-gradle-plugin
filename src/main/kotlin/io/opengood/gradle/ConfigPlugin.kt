package io.opengood.gradle

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute.CYAN_TEXT
import com.diogonunes.jcolor.Attribute.GREEN_TEXT
import com.diogonunes.jcolor.Attribute.RED_TEXT
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.opengood.gradle.constant.Boms
import io.opengood.gradle.constant.Dependencies
import io.opengood.gradle.constant.KotlinOptions
import io.opengood.gradle.constant.Plugins
import io.opengood.gradle.constant.Publications
import io.opengood.gradle.constant.Releases
import io.opengood.gradle.constant.Repositories
import io.opengood.gradle.constant.Resources
import io.opengood.gradle.constant.Tests
import io.opengood.gradle.constant.Versions
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import io.opengood.gradle.extension.OpenGoodExtension
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import net.researchgate.release.BaseScmAdapter
import net.researchgate.release.GitAdapter
import net.researchgate.release.ReleaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginConvention
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.plugins.signing.SigningExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.dsl.SpringBootExtension
import org.springframework.boot.gradle.tasks.bundling.BootJar
import java.net.URI

class ConfigPlugin : Plugin<Project> {

    private lateinit var extension: OpenGoodExtension

    override fun apply(project: Project) {
        createExtension(project)
        configurePlugins(project)
        configureDependencyResolutionStrategy(project)
        configureRepositories(project)

        project.afterEvaluate {
            configurePlugins(project, afterEval = true)
            configureConventions(project)
            configureBootJarResolution(project)
            configureDependencies(project)
            configureTasks(project)
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
                            apply(Plugins.KOTLIN_NO_ARG)
                            apply(Plugins.KOTLIN_SPRING)
                        }
                    }

                    apply(Plugins.BASE)
                    apply(Plugins.IDEA)
                    apply(Plugins.JACOCO)
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

    private fun configureDependencyResolutionStrategy(project: Project) {
        project.configurations.all { configuration ->
            with(configuration) {
                resolutionStrategy.eachDependency { resolver ->
                    with(resolver) {
                        if (requested.group == "org.jetbrains.kotlin") {
                            useVersion(Versions.KOTLIN)
                            because("Incompatibilities with older Kotlin versions")
                        }
                    }
                }
            }
        }
    }

    private fun configureBootJarResolution(project: Project) {
        with(extension) {
            with(features) {
                with(main) {
                    if (spring && projectType == ProjectType.APP) {
                        with(project.configurations) {
                            val apiElements = named("apiElements")
                            val runtimeElements = named("runtimeElements")
                            all {
                                with(project.tasks) {
                                    val jar = withType(Jar::class.java).getByName("jar")
                                    val bootJar = withType(BootJar::class.java).getByName("bootJar")

                                    val elements = listOf(apiElements, runtimeElements)
                                    elements.forEach { element ->
                                        with(element.get().outgoing) {
                                            artifacts.removeIf {
                                                it.buildDependencies.getDependencies(null).contains(jar)
                                            }
                                            artifact(bootJar)
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

    private fun configureRepositories(project: Project) {
        with(project.repositories) {
            add(mavenCentral())
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
                                    implementation.dependencies.add(create(Dependencies.KOTLIN_COROUTINES))
                                    implementation.dependencies.add(create(Dependencies.KOTLIN_REFLECT))
                                    implementation.dependencies.add(create(Dependencies.KOTLIN_STD_LIB))
                                    testImplementation.dependencies.add(create(Dependencies.KOTLIN_TEST))

                                    if (jacksonKotlin) {
                                        implementation.dependencies.add(create(Dependencies.JACKSON_KOTLIN))
                                    }
                                    if (kotest) {
                                        testImplementation.dependencies.add(create(Dependencies.KOTEST))
                                        testImplementation.dependencies.add(create(Dependencies.KOTEST_EXTENSIONS))
                                    }
                                    if (kotestSpring) {
                                        testImplementation.dependencies.add(create(Dependencies.KOTEST_SPRING))
                                    }
                                    if (mockk) {
                                        testImplementation.dependencies.add(create(Dependencies.MOCKK))
                                    }
                                    if (springMockk) {
                                        testImplementation.dependencies.add(create(Dependencies.SPRING_MOCKK))
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
                                    (languageType == LanguageType.KOTLIN && testFrameworks.java)
                                ) {
                                    if (assertj) {
                                        testImplementation.dependencies.add(create(Dependencies.ASSERTJ))
                                    }
                                    if (junitJupiter) {
                                        testImplementation.dependencies.add(create(Dependencies.JUNIT_JUPITER))
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
                }
            }
        }

        configureJavaCompileTask(project)
        configureProcessResourcesTask(project)
        configureDependencyUpdatesTask(project)
        configureTestTask(project)
        configureJacocoTestReportTask(project)
        configureJarTask(project)
        configureBootJarTask(project)
    }

    private fun configureGradleWrapperTask(project: Project) {
        project.tasks.withType(Wrapper::class.java) { task ->
            with(task) {
                distributionType = DistributionType.ALL
            }
        }
    }

    private fun configureKotlinCompileTask(project: Project) {
        project.tasks.withType(KotlinCompile::class.java) { task ->
            with(task) {
                kotlinOptions {
                    freeCompilerArgs = KotlinOptions.FREE_COMPILER_ARGS
                    jvmTarget = Versions.JAVA
                }
            }
        }
    }

    private fun configureJavaCompileTask(project: Project) {
        project.tasks.withType(JavaCompile::class.java) { task ->
            with(task) {
                sourceCompatibility = Versions.JAVA
                targetCompatibility = Versions.JAVA
            }
        }
    }

    private fun configureProcessResourcesTask(project: Project) {
        with(extension.main) {
            if (projectType == ProjectType.APP) {
                project.tasks.withType(ProcessResources::class.java) { task ->
                    with(task) {
                        filesMatching(Resources.APPLICATION_PROPERTIES) { file ->
                            file.expand(project.properties)
                        }
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

        project.tasks.withType(DependencyUpdatesTask::class.java) { task ->
            with(task) {
                resolutionStrategy { strategy ->
                    with(strategy) {
                        componentSelection { component ->
                            component.all { selection ->
                                with(selection) {
                                    if (isDependencyVersionNotStable(candidate.version) &&
                                        !isDependencyVersionNotStable(currentVersion)
                                    ) {
                                        reject("Release candidate")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun configureTestTask(project: Project) {
        project.tasks.withType(Test::class.java) { task ->
            with(task) {
                finalizedBy("jacocoTestReport")
                setOnlyIf { !project.hasProperty(Tests.SKIP_TESTS) }
                useJUnitPlatform()

                testLogging { logging ->
                    logging.apply {
                        events = Tests.LOGGING_EVENTS
                        exceptionFormat = Tests.EXCEPTION_FORMAT
                        showCauses = Tests.SHOW_CAUSES
                        showExceptions = Tests.SHOW_EXCEPTIONS
                        showStackTraces = Tests.SHOW_STACK_TRACES
                    }
                }

                maxParallelForks = extension.test.maxParallelForks
                systemProperty(Tests.INSTANCE_LIFECYCLE_SYS_PROP_NAME, Tests.INSTANCE_LIFECYCLE_SYS_PROP_VALUE)

                doFirst {
                    println(colorize("***************************************************", CYAN_TEXT()))
                    println(colorize(" >> Running Tests", CYAN_TEXT()))
                    println(colorize("***************************************************", CYAN_TEXT()))
                }

                addTestListener(object : TestListener {
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

                doLast {
                    println(colorize("***************************************************", CYAN_TEXT()))
                    println(colorize(" >> Tests FINISHED", CYAN_TEXT()))
                    println(colorize("***************************************************", CYAN_TEXT()))
                }
            }
        }
    }

    private fun configureJacocoTestReportTask(project: Project) {
        project.tasks.withType(JacocoReport::class.java).getByName("jacocoTestReport") { task ->
            with(task) {
                reports { config ->
                    with(config) {
                        xml.isEnabled = true
                        html.isEnabled = false
                    }
                }
            }
        }
    }

    private fun configureJarTask(project: Project) {
        with(extension.main) {
            if (projectType == ProjectType.LIB) {
                project.tasks.withType(Jar::class.java).getByName("jar") { task ->
                    with(task) {
                        enabled = true
                    }
                }
            }
        }
    }

    private fun configureBootJarTask(project: Project) {
        with(extension) {
            with(main) {
                with(features) {
                    if (!spring || projectType == ProjectType.LIB) {
                        project.tasks.withType(BootJar::class.java).getByName("bootJar") { task ->
                            with(task) {
                                enabled = false
                            }
                        }
                    }
                }
            }
        }
    }

    private fun configureAfterReleaseBuildTask(project: Project) {
        with(extension.release) {
            project.tasks.getByName("afterReleaseBuild") { task ->
                with(task) {
                    dependsOn(afterReleaseBuildTasks)
                }
            }
        }
    }

    private fun configureExtensions(project: Project) {
        with(extension.features) {
            if (spring) {
                configureDependencyManagementExtension(project)
            }
        }

        configureJavaExtension(project)
        configureSpringBootExtension(project)
        configureReleaseExtension(project)

        with(extension.features) {
            if (publishing) {
                configurePublishingExtension(project)
                configureSigningExtension(project)
            }
        }
    }

    private fun configureDependencyManagementExtension(project: Project) {
        with(project) {
            pluginManager.withPlugin(Plugins.SPRING_DEPENDENCY_MANAGEMENT) {
                extensions.configure(DependencyManagementExtension::class.java) { ext ->
                    with(ext) {
                        imports { imports ->
                            with(imports) {
                                mavenBom(Boms.KOTLIN)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun configureJavaExtension(project: Project) {
        with(project) {
            pluginManager.withPlugin(Plugins.JAVA) {
                extensions.configure(JavaPluginExtension::class.java) { ext ->
                    with(ext) {
                        withJavadocJar()
                        withSourcesJar()
                    }
                }
            }
        }
    }

    private fun configureSpringBootExtension(project: Project) {
        with(extension.main) {
            if (projectType == ProjectType.APP) {
                with(project) {
                    pluginManager.withPlugin(Plugins.SPRING_BOOT) {
                        extensions.configure(SpringBootExtension::class.java) { ext ->
                            with(ext) {
                                buildInfo()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun configureReleaseExtension(project: Project) {
        with(project) {
            pluginManager.withPlugin(Plugins.RELEASE) {
                extensions.configure(ReleaseExtension::class.java) { ext ->
                    with(ext) {
                        scmAdapters = mutableListOf<Class<out BaseScmAdapter>>(GitAdapter::class.java)
                        preTagCommitMessage = Releases.PRE_TAG_COMMIT_MESSAGE
                        newVersionCommitMessage = Releases.NEW_VERSION_COMMIT_MESSAGE
                        versionPatterns = Releases.VERSION_PATTERNS
                        git {
                            requireBranch = extension.release.requireBranch
                            pushToRemote = extension.release.pushToRemote
                        }
                    }
                }
            }
        }
    }

    private fun configurePublishingExtension(project: Project) {
        with(project) {
            pluginManager.withPlugin(Plugins.MAVEN_PUBLISH) {
                extensions.configure(PublishingExtension::class.java) { ext ->
                    with(ext) {
                        publications { publications ->
                            publications.register(
                                Publications.OSS_PUB_NAME,
                                MavenPublication::class.java
                            ) { publication ->
                                with(publication) {
                                    with(extension) {
                                        from(components.getByName("java"))
                                        pom { pom ->
                                            with(pom) {
                                                name.set(artifact.name)
                                                packaging = artifact.packaging.toString()
                                                description.set(artifact.description)
                                                url.set(artifact.uri)
                                                scm { scm ->
                                                    with(scm) {
                                                        connection.set(artifact.scm.connection)
                                                        developerConnection.set(artifact.scm.developerConnection)
                                                        url.set(artifact.scm.uri)
                                                    }
                                                }
                                                licenses {
                                                    it.license { license ->
                                                        with(license) {
                                                            name.set(artifact.license.name)
                                                            url.set(artifact.license.uri)
                                                        }
                                                    }
                                                }
                                                developers {
                                                    it.developer { developer ->
                                                        with(developer) {
                                                            id.set(artifact.developer.id)
                                                            name.set(artifact.developer.name)
                                                            email.set(artifact.developer.email)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        repositories { repos ->
                            repos.maven { repo ->
                                with(repo) {
                                    name = Repositories.LOCAL_REPO_NAME
                                    url = repositories.mavenLocal().url
                                }
                            }

                            val ossrhUsername = project.getProperty("ossrhUsername", getEnv("OSSRH_USERNAME", ""))
                            val ossrhPassword = project.getProperty("ossrhPassword", getEnv("OSSRH_PASSWORD", ""))

                            if (ossrhUsername.isBlank()) println("WARN: ossrhUsername property or OSSRH_USERNAME environment variable is not set")
                            if (ossrhPassword.isBlank()) println("WARN: ossrhPassword property or OSSRH_PASSWORD environment variable is not set")

                            with(extension) {
                                repos.maven { repo ->
                                    with(repo) {
                                        name =
                                            if (isSnapshotVersion) Repositories.OSS_SNAPSHOTS_REPO_NAME else Repositories.OSS_STAGING_REPO_NAME
                                        url =
                                            URI(if (isSnapshotVersion) artifact.repo.snapshotsRepoUri else artifact.repo.stagingRepoUri)
                                        credentials { credential ->
                                            with(credential) {
                                                username = ossrhUsername
                                                password = ossrhPassword
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

    private fun configureSigningExtension(project: Project) {
        with(project) {
            pluginManager.withPlugin(Plugins.SIGNING) {
                extensions.configure(SigningExtension::class.java) { ext ->
                    with(ext) {
                        val signingKey = getEnv("GPG_SIGNING_PRIVATE_KEY", "")
                        val signingPassword = getEnv("GPG_SIGNING_PASSWORD", "")

                        if (signingKey.isNotBlank() && signingPassword.isNotBlank()) {
                            println("Environment variables GPG_SIGNING_PRIVATE_KEY and GPG_SIGNING_PASSWORD are set")
                            println("Using in-memory GPG key for signing")
                            useInMemoryPgpKeys(signingKey, signingPassword)
                        } else {
                            println("Environment variables GPG_SIGNING_PRIVATE_KEY and GPG_SIGNING_PASSWORD are not set")
                            println("Defaulting to global Gradle properties file for GPG key for signing")
                        }

                        sign(getExtension<PublishingExtension>().publications.getByName(Publications.OSS_PUB_NAME))
                    }
                }
            }
        }
    }

    companion object {
        const val PLUGIN_ID = "io.opengood.gradle.config"
    }
}
