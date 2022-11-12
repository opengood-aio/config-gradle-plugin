package io.opengood.gradle

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute.CYAN_TEXT
import com.diogonunes.jcolor.Attribute.GREEN_TEXT
import com.diogonunes.jcolor.Attribute.RED_TEXT
import io.opengood.gradle.constant.Archives
import io.opengood.gradle.constant.Boms
import io.opengood.gradle.constant.Configurations
import io.opengood.gradle.constant.Dependencies
import io.opengood.gradle.constant.Elements
import io.opengood.gradle.constant.EnvVars
import io.opengood.gradle.constant.Jars
import io.opengood.gradle.constant.KotlinOptions
import io.opengood.gradle.constant.Plugins
import io.opengood.gradle.constant.Properties
import io.opengood.gradle.constant.Publications
import io.opengood.gradle.constant.Releases
import io.opengood.gradle.constant.Repositories
import io.opengood.gradle.constant.Resources
import io.opengood.gradle.constant.Tasks
import io.opengood.gradle.constant.Tests
import io.opengood.gradle.constant.Versions
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import io.opengood.gradle.enumeration.PublicationType
import io.opengood.gradle.extension.OpenGoodExtension
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import net.researchgate.release.BaseScmAdapter
import net.researchgate.release.GitAdapter
import net.researchgate.release.ReleaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublicationContainer
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
        println("Applying $PLUGIN_ID project configuration...")
        createExtension(project)
        configurePlugins(project)
        configureDependencyResolutionStrategy(project)
        configureRepositories(project)

        project.afterEvaluate {
            configurePlugins(project, afterEval = true)
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
                            apply(Plugins.KOTLIN_ALL_OPEN)
                            apply(Plugins.KOTLIN_JVM)
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
                    apply(Plugins.SPRING_BOOT)
                    apply(Plugins.SPRING_DEPENDENCY_MANAGEMENT)
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
                        if (requested.group == "org.jetbrains.kotlinx" && requested.name == "kotlinx-coroutines-core") {
                            useVersion(Versions.KOTLIN_COROUTINES)
                            because("Incompatibilities with older Kotlin Coroutines versions")
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
                            val apiElements = named(Elements.API)
                            val runtimeElements = named(Elements.RUNTIME)
                            all {
                                with(project.tasks) {
                                    val jar = withType(Jar::class.java).getByName(Jars.JAR)
                                    val bootJar = withType(BootJar::class.java).getByName(Jars.BOOT_JAR)

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
                val implementation = getByName(Configurations.IMPLEMENTATION)
                val annotationProcessor = getByName(Configurations.ANNOTATION_PROCESSOR)
                val testImplementation = getByName(Configurations.TEST_IMPLEMENTATION)
                val testAnnotationProcessor = getByName(Configurations.TEST_ANNOTATION_PROCESSOR)

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
                                    implementation.dependencies.add(create(Dependencies.KOTLIN_REFLECT))
                                    implementation.dependencies.add(create(Dependencies.KOTLIN_STD_LIB))
                                    testImplementation.dependencies.add(create(Dependencies.KOTLIN_TEST))

                                    if (kotlinCoroutines) {
                                        implementation.dependencies.add(create(Dependencies.KOTLIN_COROUTINES))
                                    }
                                    if (jacksonKotlin) {
                                        implementation.dependencies.add(create(Dependencies.JACKSON_KOTLIN))
                                    }
                                    if (kotest) {
                                        testImplementation.dependencies.add(create(Dependencies.KOTEST))
                                        testImplementation.dependencies.add(create(Dependencies.KOTEST_ASSERTIONS))
                                        testImplementation.dependencies.add(create(Dependencies.KOTEST_EXTENSIONS))
                                        testImplementation.dependencies.add(create(Dependencies.KOTEST_PROPERTIES))
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
                                annotationProcessor.dependencies.add(create(Dependencies.SPRING_BOOT_CONFIG_PROCESSOR))
                                implementation.dependencies.add(create(Dependencies.SPRING_BOOT_STARTER))
                                testImplementation.dependencies.add(create(Dependencies.SPRING_BOOT_STARTER_TEST))
                            }

                            with(test) {
                                if (languageType != LanguageType.KOTLIN ||
                                    (languageType == LanguageType.KOTLIN && frameworks.java)
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
            with(artifact) {
                if (publications.isNotEmpty()) {
                    configureAfterReleaseBuildTask(project)
                }
            }
        }

        configureJavaCompileTask(project)
        configureProcessResourcesTask(project)
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

    private fun configureTestTask(project: Project) {
        project.tasks.withType(Test::class.java) { task ->
            with(task) {
                setOnlyIf { !project.hasProperty(Tests.SKIP_TESTS) }
                finalizedBy(Tasks.JACOCO_TEST_REPORT)
                jvmArgs(Tests.JVM_ARGS_ADD_OPENS, Tests.JVM_ARGS_ADD_OPENS_JAVA_UTIL)
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
        project.tasks.withType(JacocoReport::class.java).getByName(Tasks.JACOCO_TEST_REPORT) { task ->
            with(task) {
                reports { config ->
                    with(config) {
                        xml.required.set(true)
                        html.required.set(false)
                    }
                }
            }
        }
    }

    private fun configureJarTask(project: Project) {
        with(extension.main) {
            project.tasks.withType(Jar::class.java).getByName(Jars.JAR) { task ->
                with(task) {
                    enabled = projectType == ProjectType.LIB
                    archiveClassifier.set(Archives.CLASSIFIER)
                }
            }
        }
    }

    private fun configureBootJarTask(project: Project) {
        with(extension) {
            with(main) {
                with(features) {
                    if (!spring || projectType == ProjectType.LIB) {
                        project.tasks.withType(BootJar::class.java).getByName(Jars.BOOT_JAR) { task ->
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
        with(extension) {
            with(release) {
                with(artifact) {
                    val tasks = mutableListOf<Any>()

                    if (publications.contains(PublicationType.GITHUB)) {
                        tasks.add(
                            String.format(
                                Tasks.PUBLISH_PUBLICATION,
                                Publications.GITHUB_PUB_NAME,
                                Repositories.GITHUB_PACKAGES_REPO_NAME
                            )
                        )
                    }
                    if (publications.contains(PublicationType.OSS)) {
                        tasks.add(
                            String.format(
                                Tasks.PUBLISH_PUBLICATION,
                                Publications.OSS_PUB_NAME,
                                if (project.isSnapshotVersion) Repositories.OSS_SNAPSHOTS_REPO_NAME else Repositories.OSS_STAGING_REPO_NAME
                            )
                        )
                    }

                    if (tasks.isNotEmpty()) {
                        afterReleaseBuildTasks = tasks.toTypedArray()
                    }
                }

                project.tasks.getByName(Tasks.AFTER_RELEASE_BUILD) { task ->
                    with(task) {
                        dependsOn(afterReleaseBuildTasks)
                    }
                }
            }
        }
    }

    private fun configureExtensions(project: Project) {
        configureBasePluginExtension(project)

        with(extension.features) {
            if (spring) {
                configureDependencyManagementExtension(project)
            }
        }

        configureJavaExtension(project)
        configureSpringBootExtension(project)
        configureReleaseExtension(project)

        with(extension.artifact) {
            if (publications.isNotEmpty()) {
                configurePublishingExtension(project)
                configureSigningExtension(project)
            }
        }
    }

    private fun configureBasePluginExtension(project: Project) {
        with(project) {
            with(extension) {
                pluginManager.withPlugin(Plugins.BASE) {
                    extensions.configure(BasePluginExtension::class.java) { ext ->
                        with(ext) {
                            archivesName.set(artifact.name)
                        }
                    }
                }
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
                        preTagCommitMessage.set(Releases.PRE_TAG_COMMIT_MESSAGE)
                        newVersionCommitMessage.set(Releases.NEW_VERSION_COMMIT_MESSAGE)
                        versionPatterns = Releases.VERSION_PATTERNS
                        git { git ->
                            with(git) {
                                requireBranch.set(extension.release.requireBranch)
                                pushToRemote.set(extension.release.pushToRemote)
                            }
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
                        with(extension) {
                            publications { publications ->
                                with(artifact) {
                                    if (this.publications.contains(PublicationType.GITHUB)) {
                                        createPublication(project, publications, Publications.GITHUB_PUB_NAME)
                                    }
                                    if (this.publications.contains(PublicationType.OSS)) {
                                        createPublication(project, publications, Publications.OSS_PUB_NAME)
                                    }
                                }
                            }

                            repositories { repos ->
                                createArtifactRepository(
                                    repos = repos,
                                    repoName = Repositories.LOCAL_REPO_NAME,
                                    repoUri = repositories.mavenLocal().url.toString()
                                )

                                with(artifact) {
                                    if (publications.contains(PublicationType.GITHUB)) {
                                        val githubPackagesRepoUsername =
                                            project.getProperty(
                                                Properties.GITHUB_PACKAGES_REPO_USERNAME,
                                                getEnv(EnvVars.GITHUB_USER, "")
                                            )
                                        val githubPackagesRepoPassword =
                                            project.getProperty(
                                                Properties.GITHUB_PACKAGES_REPO_PASSWORD,
                                                getEnv(EnvVars.GITHUB_TOKEN, "")
                                            )

                                        if (githubPackagesRepoUsername.isBlank()) println("WARN: ${Properties.GITHUB_PACKAGES_REPO_USERNAME} property or ${EnvVars.GITHUB_USER} environment variable is not set")
                                        if (githubPackagesRepoPassword.isBlank()) println("WARN: ${Properties.GITHUB_PACKAGES_REPO_PASSWORD} property or ${EnvVars.GITHUB_TOKEN} environment variable is not set")

                                        with(repo) {
                                            createArtifactRepository(
                                                repos = repos,
                                                repoName = Repositories.GITHUB_PACKAGES_REPO_NAME,
                                                repoUri = gitHubPackagesRepoUri,
                                                repoUsername = githubPackagesRepoUsername,
                                                repoPassword = githubPackagesRepoPassword
                                            )
                                        }
                                    }

                                    if (publications.contains(PublicationType.OSS)) {
                                        val ossRepoUsername =
                                            project.getProperty(
                                                Properties.OSS_REPO_USERNAME,
                                                getEnv(EnvVars.OSS_REPO_USERNAME, "")
                                            )
                                        val ossRepoPassword =
                                            project.getProperty(
                                                Properties.OSS_REPO_PASSWORD,
                                                getEnv(EnvVars.OSS_REPO_PASSWORD, "")
                                            )

                                        if (ossRepoUsername.isBlank()) println("WARN: ${Properties.OSS_REPO_USERNAME} property or ${EnvVars.GITHUB_USER} environment variable is not set")
                                        if (ossRepoPassword.isBlank()) println("WARN: ${Properties.OSS_REPO_PASSWORD} property or ${EnvVars.GITHUB_TOKEN} environment variable is not set")

                                        with(repo) {
                                            createArtifactRepository(
                                                repos = repos,
                                                repoName = if (isSnapshotVersion) Repositories.OSS_SNAPSHOTS_REPO_NAME else Repositories.OSS_STAGING_REPO_NAME,
                                                repoUri = if (isSnapshotVersion) ossSnapshotsRepoUri else ossStagingRepoUri,
                                                repoUsername = ossRepoUsername,
                                                repoPassword = ossRepoPassword
                                            )
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

    private fun createPublication(
        project: Project,
        publications: PublicationContainer,
        publicationName: String
    ) {
        with(project) {
            publications.register(publicationName, MavenPublication::class.java) { publication ->
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
    }

    private fun createArtifactRepository(
        repos: RepositoryHandler,
        repoName: String,
        repoUri: String,
        repoUsername: String? = null,
        repoPassword: String? = null
    ) {
        repos.maven { repo ->
            with(repo) {
                name = repoName
                url = URI(repoUri)

                if (repoUsername != null && repoUsername.isNotBlank() &&
                    repoPassword != null && repoPassword.isNotBlank()
                ) {
                    credentials { credential ->
                        with(credential) {
                            username = repoUsername
                            password = repoPassword
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
                        val signingKey = getEnv(EnvVars.GPG_SIGNING_PRIVATE_KEY, "")
                        val signingPassword = getEnv(EnvVars.GPG_SIGNING_PASSWORD, "")

                        if (signingKey.isNotBlank() && signingPassword.isNotBlank()) {
                            println("Environment variables ${EnvVars.GPG_SIGNING_PRIVATE_KEY} and ${EnvVars.GPG_SIGNING_PASSWORD} are set")
                            println("Using in-memory GPG key for signing")
                            useInMemoryPgpKeys(signingKey, signingPassword)
                        } else {
                            println("Environment variables ${EnvVars.GPG_SIGNING_PRIVATE_KEY} and ${EnvVars.GPG_SIGNING_PASSWORD} are not set")
                            println("Defaulting to global Gradle properties file for GPG key for signing")
                        }

                        with(extension.artifact) {
                            if (publications.contains(PublicationType.OSS)) {
                                sign(getExtension<PublishingExtension>().publications.getByName(Publications.OSS_PUB_NAME))
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val PLUGIN_ID = "io.opengood.gradle.config"
    }
}
