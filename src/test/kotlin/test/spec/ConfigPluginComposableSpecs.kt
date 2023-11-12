package test.spec

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainAnyOf
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.types.shouldBeTypeOf
import io.opengood.gradle.ConfigPlugin
import io.opengood.gradle.constant.Archives
import io.opengood.gradle.constant.Boms
import io.opengood.gradle.constant.Configurations.Companion.ANNOTATION_PROCESSOR
import io.opengood.gradle.constant.Configurations.Companion.IMPLEMENTATION
import io.opengood.gradle.constant.Configurations.Companion.TEST_ANNOTATION_PROCESSOR
import io.opengood.gradle.constant.Configurations.Companion.TEST_IMPLEMENTATION
import io.opengood.gradle.constant.Dependencies
import io.opengood.gradle.constant.Developers
import io.opengood.gradle.constant.Elements
import io.opengood.gradle.constant.GitHub.Companion.BLOB_ENDPOINT_NAME
import io.opengood.gradle.constant.GitHub.Companion.DEFAULT_BRANCH_NAME
import io.opengood.gradle.constant.GitHub.Companion.OPENGOOD_ORG_NAME
import io.opengood.gradle.constant.GitHub.Companion.OPENGOOD_ORG_URI
import io.opengood.gradle.constant.Jars
import io.opengood.gradle.constant.KotlinOptions
import io.opengood.gradle.constant.Licenses
import io.opengood.gradle.constant.Licenses.Companion.RESOURCE
import io.opengood.gradle.constant.Plugins
import io.opengood.gradle.constant.Publications
import io.opengood.gradle.constant.Releases
import io.opengood.gradle.constant.Repositories
import io.opengood.gradle.constant.Repositories.Companion.GITHUB_PACKAGES_REPO_BASE_URI
import io.opengood.gradle.constant.Repositories.Companion.OSS_SNAPSHOTS_REPO_NAME
import io.opengood.gradle.constant.Repositories.Companion.OSS_STAGING_REPO_NAME
import io.opengood.gradle.constant.Signatures
import io.opengood.gradle.constant.Tasks
import io.opengood.gradle.constant.Tests
import io.opengood.gradle.dependenciesVersions
import io.opengood.gradle.enumeration.PackagingType
import io.opengood.gradle.enumeration.ProjectType
import io.opengood.gradle.enumeration.PublicationType
import io.opengood.gradle.enumeration.ScmProvider
import io.opengood.gradle.enumeration.ScmProvider.GIT
import io.opengood.gradle.extension.openGood
import io.opengood.gradle.getExtension
import io.opengood.gradle.isSnapshotVersion
import io.opengood.gradle.languageType
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import net.researchgate.release.GitAdapter
import net.researchgate.release.ReleaseExtension
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.UnknownTaskException
import org.gradle.api.artifacts.UnknownRepositoryException
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.internal.artifacts.dsl.LazyPublishArtifact
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact
import org.gradle.api.internal.provider.ProviderInternal
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.UnknownPluginException
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPom
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.plugins.signing.Sign
import org.gradle.plugins.signing.SigningExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.dsl.SpringBootExtension
import org.springframework.boot.gradle.tasks.bundling.BootJar
import test.accessField
import test.getBom
import test.getDependencies
import test.getDependency
import test.getPlugin
import test.getPublication
import test.getRepository
import test.getTaskByName
import test.getTaskByType
import test.getTaskByTypeAndName
import test.getTasksDependsOn
import test.hasTaskFinalizedByDependency
import test.then
import java.net.URI

fun applyPluginTest(project: Project) =
    funSpec {
        test("Applies plugin") {
            getPlugin(project, ConfigPlugin.ID).shouldNotBeNull()
            getPlugin(project, ConfigPlugin.ID).shouldBeTypeOf<ConfigPlugin>()
            getPlugin<ConfigPlugin>(project).shouldBeTypeOf<ConfigPlugin>()
        }
    }

fun createExtensionTest(
    project: Project,
    projectType: ProjectType,
) = funSpec {
    test("Creates extension") {
        with(project.openGood()) {
            shouldNotBeNull()

            with(main) {
                languageType shouldBe project.languageType
                main.projectType shouldBe projectType
            }
            with(features) {
                assertj.shouldBeTrue()
                jackson.shouldBeTrue()
                jacksonKotlin.shouldBeTrue()
                junitJupiter.shouldBeTrue()
                kotest.shouldBeTrue()
                kotestSpring.shouldBeTrue()
                kotlinCoroutines.shouldBeTrue()
                lombok.shouldBeTrue()
                mockito.shouldBeTrue()
                mockk.shouldBeTrue()
                spring.shouldBeTrue()
                springMockk.shouldBeTrue()
            }
            with(test) {
                maxParallelForks shouldBe Tests.MAX_PARALLEL_FORKS
                with(frameworks) {
                    java.shouldBeFalse()
                }
            }
            with(release) {
                afterReleaseBuildTasks shouldBe
                    arrayOf(
                        String.format(
                            Tasks.PUBLISH_PUBLICATION,
                            Publications.GITHUB,
                            Repositories.GITHUB_PACKAGES_REPO_NAME,
                        ),
                        String.format(
                            Tasks.PUBLISH_PUBLICATION,
                            Publications.OSS,
                            OSS_SNAPSHOTS_REPO_NAME,
                        ),
                    )
                requireBranch shouldBe Releases.REQUIRE_BRANCH
                pushToRemote shouldBe Releases.PUSH_TO_REMOTE
            }
            with(artifact) {
                name shouldBe project.name
                packaging shouldBe PackagingType.JAR
                description.shouldBeEmpty()
                uri shouldBe "$OPENGOOD_ORG_URI/${project.name}"
                publications shouldBe listOf(PublicationType.GITHUB, PublicationType.OSS)
                with(repo) {
                    ossSnapshotsRepoUri shouldBe Repositories.OSS_SNAPSHOTS_REPO_URI
                    ossStagingRepoUri shouldBe Repositories.OSS_STAGING_REPO_URI
                    gitHubPackagesRepoUri shouldBe "$GITHUB_PACKAGES_REPO_BASE_URI/$OPENGOOD_ORG_NAME/${project.name}"
                }
                with(scm) {
                    provider shouldBe GIT
                    connection shouldBe "${ScmProvider.PROTOCOL}:$provider:$OPENGOOD_ORG_URI/${project.name}"
                    devConnection shouldBe "${ScmProvider.PROTOCOL}:$provider:$OPENGOOD_ORG_URI/${project.name}"
                    uri shouldBe "$OPENGOOD_ORG_URI/${project.name}"
                }
                with(license) {
                    name shouldBe Licenses.NAME
                    uri shouldBe "$OPENGOOD_ORG_URI/${project.name}/$BLOB_ENDPOINT_NAME/$DEFAULT_BRANCH_NAME/$RESOURCE"
                }
                with(developer) {
                    id shouldBe Developers.ID
                    name shouldBe Developers.NAME
                    email shouldBe Developers.EMAIL
                }
            }
        }
    }
}

fun applyGroovyPluginTest(project: Project) =
    funSpec {
        test("Applies Groovy plugin") {
            getPlugin(project, Plugins.GROOVY).shouldNotBeNull()
        }
    }

fun applyJavaPluginTest(project: Project) =
    funSpec {
        test("Applies Java plugin") {
            getPlugin(project, Plugins.JAVA).shouldNotBeNull()
        }
    }

fun applyKotlinPluginsTest(project: Project) =
    funSpec {
        test("Applies Kotlin plugins") {
            getPlugin(project, Plugins.KOTLIN_JVM).shouldNotBeNull()
            getPlugin(project, Plugins.KOTLIN_ALL_OPEN).shouldNotBeNull()
            getPlugin(project, Plugins.KOTLIN_NO_ARG).shouldNotBeNull()
        }
    }

fun applyCommonPluginsTest(project: Project) =
    funSpec {
        test("Applies common plugins") {
            getPlugin(project, Plugins.BASE).shouldNotBeNull()
            getPlugin(project, Plugins.IDEA).shouldNotBeNull()
            getPlugin(project, Plugins.JACOCO).shouldNotBeNull()
            getPlugin(project, Plugins.MAVEN_PUBLISH).shouldNotBeNull()
            getPlugin(project, Plugins.RELEASE).shouldNotBeNull()
            getPlugin(project, Plugins.SIGNING).shouldNotBeNull()
        }
    }

fun applyLibraryPluginTest(project: Project) =
    funSpec {
        test("Applies library plugin") {
            getPlugin(project, Plugins.JAVA_LIBRARY).shouldNotBeNull()
        }
    }

fun applySpringPluginsTest(project: Project) =
    funSpec {
        test("Applies Spring plugins") {
            getPlugin(project, Plugins.SPRING_BOOT).shouldNotBeNull()
            getPlugin(project, Plugins.SPRING_DEPENDENCY_MANAGEMENT).shouldNotBeNull()
        }
    }

fun applyKotlinSpringPluginTest(project: Project) =
    funSpec {
        test("Applies Kotlin Spring plugin") {
            getPlugin(project, Plugins.KOTLIN_SPRING).shouldNotBeNull()
        }
    }

fun applyLombokPluginTest(project: Project) =
    funSpec {
        test("Applies Lombok plugin") {
            getPlugin(project, Plugins.LOMBOK).shouldNotBeNull()
        }
    }

fun doNotApplyGroovyPluginTest(project: Project) =
    funSpec {
        test("Does not apply Groovy plugin") {
            shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.GROOVY).shouldNotBeNull() }
        }
    }

fun doNotApplyKotlinPluginsTest(project: Project) =
    funSpec {
        test("Does not apply Kotlin plugins") {
            shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.KOTLIN_ALL_OPEN).shouldNotBeNull() }
            shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.KOTLIN_JVM).shouldNotBeNull() }
            shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.KOTLIN_NO_ARG).shouldNotBeNull() }
        }
    }

fun doNotApplyLibraryPluginTest(project: Project) =
    funSpec {
        test("Does not apply library plugin") {
            shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.JAVA_LIBRARY).shouldNotBeNull() }
        }
    }

fun doNotApplyKotlinSpringPluginTest(project: Project) =
    funSpec {
        test("Does not apply Kotlin Spring plugin") {
            shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.KOTLIN_SPRING) }
        }
    }

fun doNotApplyLombokPluginTest(project: Project) =
    funSpec {
        test("Does not apply Lombok plugin") {
            shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.LOMBOK) }
        }
    }

fun configureDependencyResolutionStrategyTest(project: Project) =
    funSpec {
        test("Configures dependency resolution strategy") {
            val implementationDependencies = getDependencies(project, IMPLEMENTATION)
            val testImplementationDependencies = getDependencies(project, TEST_IMPLEMENTATION)
            (implementationDependencies + testImplementationDependencies).forEach { dependency ->
                with(project.dependenciesVersions) {
                    if (dependency.group == "org.jetbrains.kotlin") {
                        dependency.version shouldBe getVersion(Dependencies.KOTLIN)
                    }
                    if (dependency.group == "org.jetbrains.kotlinx" && dependency.name == "kotlinx-coroutines-core") {
                        dependency.version shouldBe getVersion(Dependencies.KOTLIN_COROUTINES)
                    }
                    if (dependency.group == "org.yaml") {
                        dependency.version shouldBe getVersion(Dependencies.SNAKE_YAML)
                    }
                }
            }
        }
    }

fun configureBootJarResolutionTest(project: Project) =
    funSpec {
        test("Configures boot jar resolution") {
            with(project.configurations) {
                val apiElements = named(Elements.API)
                val runtimeElements = named(Elements.RUNTIME)

                val jar = getTaskByTypeAndName<Jar>(project, Jars.JAR)
                val bootJar = getTaskByTypeAndName<BootJar>(project, Jars.BOOT_JAR)

                all {
                    val elements = listOf(apiElements, runtimeElements)
                    elements.forEach { element ->
                        with(element.get().outgoing) {
                            artifacts.forEach { artifact ->
                                with(artifact as ArchivePublishArtifact) {
                                    archiveTask shouldNotBe jar
                                    archiveTask shouldBe bootJar
                                }
                            }
                        }
                    }
                }
            }
        }
    }

fun doNotConfigureBootJarResolutionTest(project: Project) =
    funSpec {
        test("Does not configure boot jar resolution") {
            with(project.configurations) {
                val apiElements = named(Elements.API)
                val runtimeElements = named(Elements.RUNTIME)

                all {
                    val elements = listOf(apiElements, runtimeElements)
                    elements.forEach { element ->
                        with(element.get().outgoing) {
                            artifacts.forEach { artifact ->
                                with(artifact as LazyPublishArtifact) {
                                    with(accessField<ProviderInternal<*>>("provider").toString()) {
                                        contains("task '${Jars.JAR}'").shouldBeTrue()
                                        contains("task '${Jars.BOOT_JAR}'").shouldBeFalse()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

fun addRepositoriesTest(project: Project) =
    funSpec {
        test("Adds repositories") {
            with(project.repositories) {
                getRepository(project, mavenCentral().name).shouldNotBeNull()
                getRepository(project, gradlePluginPortal().name).shouldNotBeNull()
                getRepository(project, mavenLocal().name).shouldNotBeNull()
            }
        }
    }

fun addGroovyDependenciesTest(project: Project) =
    funSpec {
        test("Adds Groovy dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.GROOVY)).shouldNotBeNull()
            }
        }
    }

fun addLombokDependenciesTest(project: Project) =
    funSpec {
        test("Adds Lombok dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, ANNOTATION_PROCESSOR, getDependencyAndVersion(Dependencies.LOMBOK)).shouldNotBeNull()
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.LOMBOK)).shouldNotBeNull()
                getDependency(project, TEST_ANNOTATION_PROCESSOR, getDependencyAndVersion(Dependencies.LOMBOK)).shouldNotBeNull()
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.LOMBOK)).shouldNotBeNull()
            }
        }
    }

fun addKotlinDependenciesTest(project: Project) =
    funSpec {
        test("Adds Kotlin dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTLIN_REFLECT)).shouldNotBeNull()
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTLIN_STD_LIB)).shouldNotBeNull()
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTLIN_TEST)).shouldNotBeNull()
            }
        }
    }

fun addKotlinCoroutinesDependenciesTest(project: Project) =
    funSpec {
        test("Adds Kotlin Coroutines dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTLIN_COROUTINES_CORE)).shouldNotBeNull()
            }
        }
    }

fun addJacksonKotlinDependenciesTest(project: Project) =
    funSpec {
        test("Adds Jackson Kotlin dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.JACKSON_MODULE_KOTLIN)).shouldNotBeNull()
            }
        }
    }

fun addKotestDependenciesTest(project: Project) =
    funSpec {
        test("Adds Kotest dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTEST_RUNNER)).shouldNotBeNull()
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTEST_ASSERTIONS)).shouldNotBeNull()
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTEST_EXTENSIONS)).shouldNotBeNull()
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTEST_PROPERTIES)).shouldNotBeNull()
            }
        }
    }

fun addKotestSpringDependenciesTest(project: Project) =
    funSpec {
        test("Adds Kotest Spring dependencies") {
            with(project.dependenciesVersions) {
                getDependency(
                    project,
                    TEST_IMPLEMENTATION,
                    getDependencyAndVersion(Dependencies.KOTEST_SPRING_EXTENSIONS),
                ).shouldNotBeNull()
            }
        }
    }

fun addMockkDependenciesTest(project: Project) =
    funSpec {
        test("Adds MockK dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.MOCKK_ALL)).shouldNotBeNull()
            }
        }
    }

fun addSpringMockkDependenciesTest(project: Project) =
    funSpec {
        test("Adds Spring MockK dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.SPRING_MOCKK)).shouldNotBeNull()
            }
        }
    }

fun addJacksonDependenciesTest(project: Project) =
    funSpec {
        test("Adds Jackson dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.JACKSON_ANNOTATIONS)).shouldNotBeNull()
            }
        }
    }

fun addSpringDependenciesTest(project: Project) =
    funSpec {
        test("Adds Spring dependencies") {
            with(project.dependenciesVersions) {
                getDependency(
                    project,
                    ANNOTATION_PROCESSOR,
                    getDependencyAndVersion(Dependencies.SPRING_BOOT_CONFIG_PROCESSOR),
                ).shouldNotBeNull()
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.SPRING_BOOT_STARTER)).shouldNotBeNull()
                getDependency(
                    project,
                    TEST_IMPLEMENTATION,
                    getDependencyAndVersion(Dependencies.SPRING_BOOT_STARTER_TEST),
                ).shouldNotBeNull()
            }
        }
    }

fun addSnakeYamlDependenciesTest(project: Project) =
    funSpec {
        test("Adds SnakeYAML dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.SNAKE_YAML)).shouldNotBeNull()
            }
        }
    }

fun addAssertjDependenciesTest(project: Project) =
    funSpec {
        test("Adds AssertJ dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.ASSERTJ_CORE)).shouldNotBeNull()
            }
        }
    }

fun addJunitJupiterDependenciesTest(project: Project) =
    funSpec {
        test("Adds JUnit Jupiter dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.JUNIT_JUPITER_ALL)).shouldNotBeNull()
            }
        }
    }

fun addMockitoDependenciesTest(project: Project) =
    funSpec {
        test("Adds Mockito dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.MOCKITO_CORE)).shouldNotBeNull()
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.MOCKITO_JUNIT_JUPITER)).shouldNotBeNull()
            }
        }
    }

fun doNotAddGroovyDependenciesTest(project: Project) =
    funSpec {
        test("Does not add Groovy dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.GROOVY)).shouldBeNull()
            }
        }
    }

fun doNotAddLombokDependenciesTest(project: Project) =
    funSpec {
        test("Does not add Lombok dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, ANNOTATION_PROCESSOR, getDependencyAndVersion(Dependencies.LOMBOK)).shouldBeNull()
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.LOMBOK)).shouldBeNull()
                getDependency(project, TEST_ANNOTATION_PROCESSOR, getDependencyAndVersion(Dependencies.LOMBOK)).shouldBeNull()
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.LOMBOK)).shouldBeNull()
            }
        }
    }

fun doNotAddKotlinDependenciesTest(project: Project) =
    funSpec {
        test("Does not add Kotlin dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTLIN_REFLECT)).shouldBeNull()
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTLIN_STD_LIB)).shouldBeNull()
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTLIN_TEST)).shouldBeNull()
            }
        }
    }

fun doNotAddKotlinCoroutinesDependenciesTest(project: Project) =
    funSpec {
        test("Does not add Kotlin Coroutines dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTLIN_COROUTINES_CORE)).shouldBeNull()
            }
        }
    }

fun doNotAddJacksonKotlinDependenciesTest(project: Project) =
    funSpec {
        test("Does not add Jackson Kotlin dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.JACKSON_MODULE_KOTLIN)).shouldBeNull()
            }
        }
    }

fun doNotAddKotestDependenciesTest(project: Project) =
    funSpec {
        test("Does not add Kotest dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTEST_RUNNER)).shouldBeNull()
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTEST_ASSERTIONS)).shouldBeNull()
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTEST_EXTENSIONS)).shouldBeNull()
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTEST_PROPERTIES)).shouldBeNull()
            }
        }
    }

fun doNotAddKotestSpringDependenciesTest(project: Project) =
    funSpec {
        test("Does not add Kotest Spring dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.KOTEST_SPRING_EXTENSIONS)).shouldBeNull()
            }
        }
    }

fun doNotAddMockkDependenciesTest(project: Project) =
    funSpec {
        test("Does not add MockK dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.MOCKK_ALL)).shouldBeNull()
            }
        }
    }

fun doNotAddSpringMockkDependenciesTest(project: Project) =
    funSpec {
        test("Does not add Spring MockK dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.SPRING_MOCKK)).shouldBeNull()
            }
        }
    }

fun doNotAddJacksonDependenciesTest(project: Project) =
    funSpec {
        test("Does not add Jackson dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.JACKSON_ANNOTATIONS)).shouldBeNull()
            }
        }
    }

fun doNotAddSpringDependenciesTest(project: Project) =
    funSpec {
        test("Does not add Spring dependencies") {
            with(project.dependenciesVersions) {
                getDependency(
                    project,
                    ANNOTATION_PROCESSOR,
                    getDependencyAndVersion(Dependencies.SPRING_BOOT_CONFIG_PROCESSOR),
                ).shouldBeNull()
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.SPRING_BOOT_STARTER)).shouldBeNull()
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.SPRING_BOOT_STARTER_TEST)).shouldBeNull()
            }
        }
    }

fun doNotAddSnakeYamlDependenciesTest(project: Project) =
    funSpec {
        test("Does not add SnakeYAML dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, IMPLEMENTATION, getDependencyAndVersion(Dependencies.SNAKE_YAML)).shouldBeNull()
            }
        }
    }

fun doNotAddAssertjDependenciesTest(project: Project) =
    funSpec {
        test("Does not add AssertJ dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.ASSERTJ_CORE)).shouldBeNull()
            }
        }
    }

fun doNotAddJunitJupiterDependenciesTest(project: Project) =
    funSpec {
        test("Does not add JUnit Jupiter dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.JUNIT_JUPITER_ALL)).shouldBeNull()
            }
        }
    }

fun doNotAddMockitoDependenciesTest(project: Project) =
    funSpec {
        test("Does not add Mockito dependencies") {
            with(project.dependenciesVersions) {
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.MOCKITO_CORE)).shouldBeNull()
                getDependency(project, TEST_IMPLEMENTATION, getDependencyAndVersion(Dependencies.MOCKITO_JUNIT_JUPITER)).shouldBeNull()
            }
        }
    }

fun configureGradleWrapperTaskTest(project: Project) =
    funSpec {
        test("Configures Gradle Wrapper task") {
            val task = getTaskByType<Wrapper>(project)

            with(task) {
                shouldNotBeNull()
                distributionType shouldBe DistributionType.ALL
            }
        }
    }

fun configureKotlinCompileTaskTest(project: Project) =
    funSpec {
        test("Configures Kotlin Compile task") {
            val task = getTaskByTypeAndName<KotlinCompile>(project, Tasks.COMPILE_KOTLIN)

            with(task) {
                shouldNotBeNull()
                with(kotlinOptions) {
                    with(project.dependenciesVersions) {
                        freeCompilerArgs shouldBe KotlinOptions.FREE_COMPILER_ARGS
                        jvmTarget shouldBe getVersion(Dependencies.JAVA)
                    }
                }
            }
        }
    }

fun configureJavaCompileTaskTest(project: Project) =
    funSpec {
        test("Configures Java Compile task") {
            val task = getTaskByTypeAndName<JavaCompile>(project, Tasks.COMPILE_JAVA)

            with(task) {
                shouldNotBeNull()
                with(project.dependenciesVersions) {
                    sourceCompatibility shouldBe getVersion(Dependencies.JAVA)
                    targetCompatibility shouldBe getVersion(Dependencies.JAVA)
                }
            }
        }
    }

fun configureProcessResourcesTaskTest(project: Project) =
    funSpec {
        test("Configures Process Resources task") {
            val task = getTaskByType<ProcessResources>(project)

            with(task) {
                shouldNotBeNull()
                destinationDir.absolutePath shouldContain "resources/main"
            }
        }
    }

fun configureTestTaskTest(project: Project) =
    funSpec {
        test("Configures Test task") {
            val task = getTaskByTypeAndName<Test>(project, Tasks.TEST)

            with(task) {
                shouldNotBeNull()
                enabled.shouldBeTrue()
                onlyIf.shouldNotBeNull()
                hasTaskFinalizedByDependency(task, Tasks.JACOCO_TEST_REPORT).shouldBeTrue()
                jvmArgs.containsAll(listOf(Tests.JVM_ARGS_ADD_OPENS, Tests.JVM_ARGS_ADD_OPENS_JAVA_UTIL))
                with(testLogging) {
                    events shouldBe Tests.LOGGING_EVENTS
                    exceptionFormat shouldBe Tests.EXCEPTION_FORMAT
                    showCauses shouldBe Tests.SHOW_CAUSES
                    showExceptions shouldBe Tests.SHOW_EXCEPTIONS
                    showStackTraces shouldBe Tests.SHOW_STACK_TRACES
                }
                maxParallelForks shouldBe Tests.MAX_PARALLEL_FORKS
                systemProperties[Tests.INSTANCE_LIFECYCLE_SYS_PROP_NAME] shouldBe Tests.INSTANCE_LIFECYCLE_SYS_PROP_VALUE
            }
        }
    }

fun configureJacocoTestReportTaskTest(project: Project) =
    funSpec {
        test("Configures Jacoco Test Report task") {
            val task = getTaskByTypeAndName<JacocoReport>(project, Tasks.JACOCO_TEST_REPORT)

            with(task) {
                shouldNotBeNull()
                with(reports) {
                    shouldNotBeNull()
                    xml.required.get().shouldBeTrue()
                    html.required.get().shouldBeFalse()
                }
            }
        }
    }

fun configureJarTaskTest(
    project: Project,
    isEnabled: Boolean,
) = funSpec {
    test("Configures Jar task") {
        val task = getTaskByTypeAndName<Jar>(project, Jars.JAR)

        with(task) {
            shouldNotBeNull()
            enabled shouldBe isEnabled
            archiveClassifier.get() shouldBe Archives.CLASSIFIER
        }
    }
}

fun configureBootJarTaskTest(
    project: Project,
    isEnabled: Boolean,
) = funSpec {
    test("Configures Boot Jar task") {
        val task = getTaskByTypeAndName<BootJar>(project, Jars.BOOT_JAR)

        with(task) {
            shouldNotBeNull()
            enabled shouldBe isEnabled
        }
    }
}

fun configureAfterReleaseBuildTaskTest(project: Project) =
    funSpec {
        test("Configures After Release Build task") {
            val task = getTaskByName(project, Tasks.AFTER_RELEASE_BUILD)

            with(task) {
                shouldNotBeNull()

                val afterReleaseBuildTasks = mutableListOf<Any>()

                with(project.openGood().artifact) {
                    if (publications.contains(PublicationType.GITHUB)) {
                        afterReleaseBuildTasks.add(
                            String.format(
                                Tasks.PUBLISH_PUBLICATION,
                                Publications.GITHUB,
                                Repositories.GITHUB_PACKAGES_REPO_NAME,
                            ),
                        )
                    }
                    if (publications.contains(PublicationType.OSS)) {
                        afterReleaseBuildTasks.add(
                            String.format(
                                Tasks.PUBLISH_PUBLICATION,
                                Publications.OSS,
                                if (project.isSnapshotVersion) OSS_SNAPSHOTS_REPO_NAME else OSS_STAGING_REPO_NAME,
                            ),
                        )
                    }
                }

                getTasksDependsOn(task).shouldContainAll(afterReleaseBuildTasks)
            }
        }
    }

fun doNotConfigureAfterReleaseBuildTaskTest(project: Project) =
    funSpec {
        test("Does not configure After Release Build task") {
            val task = getTaskByName(project, Tasks.AFTER_RELEASE_BUILD)

            with(task) {
                shouldNotBeNull()

                val afterReleaseBuildTasks = mutableListOf<Any>()

                with(project.openGood().artifact) {
                    if (publications.contains(PublicationType.GITHUB)) {
                        afterReleaseBuildTasks.add(
                            String.format(
                                Tasks.PUBLISH_PUBLICATION,
                                Publications.GITHUB,
                                Repositories.GITHUB_PACKAGES_REPO_NAME,
                            ),
                        )
                    }
                    if (publications.contains(PublicationType.OSS)) {
                        afterReleaseBuildTasks.add(
                            String.format(
                                Tasks.PUBLISH_PUBLICATION,
                                Publications.OSS,
                                if (project.isSnapshotVersion) OSS_SNAPSHOTS_REPO_NAME else OSS_STAGING_REPO_NAME,
                            ),
                        )
                    }
                }

                dependsOn.shouldBeEmpty()
            }
        }
    }

fun configureBasePluginExtensionTest(project: Project) =
    funSpec {
        test("Configures Base Plugin extension") {
            val extension = project.getExtension<BasePluginExtension>()

            with(extension) {
                shouldNotBeNull()
                archivesName.get() shouldBe project.name
            }
        }
    }

fun configureDependencyManagementExtensionTest(project: Project) =
    funSpec {
        test("Configures Dependency Management extension") {
            val extension = project.getExtension<DependencyManagementExtension>()

            with(project.dependenciesVersions) {
                with(extension) {
                    shouldNotBeNull()
                    getBom(extension, getDependencyAndVersion(Boms.KOTLIN)).shouldNotBeNull()
                }
            }
        }
    }

fun configureJavaExtensionTest(project: Project) =
    funSpec {
        test("Configures Java extension") {
            val extension = project.getExtension<JavaPluginExtension>()

            with(extension) {
                shouldNotBeNull()
                getTaskByName(project, Tasks.JAVADOC_JAR).shouldNotBeNull()
                getTaskByName(project, Tasks.SOURCES_JAR).shouldNotBeNull()
            }
        }
    }

fun configureSpringBootExtensionTest(project: Project) =
    funSpec {
        test("Configures Spring Boot extension") {
            val extension = project.getExtension<SpringBootExtension>()

            with(extension) {
                shouldNotBeNull()
            }
        }
    }

fun configureReleaseExtensionTest(project: Project) =
    funSpec {
        test("Configures Release extension") {
            val extension = project.getExtension<ReleaseExtension>()

            with(extension) {
                shouldNotBeNull()
                scmAdapters shouldContain GitAdapter::class.java
                preTagCommitMessage.get() shouldBe Releases.PRE_TAG_COMMIT_MESSAGE
                newVersionCommitMessage.get() shouldBe Releases.NEW_VERSION_COMMIT_MESSAGE
                versionPatterns shouldBe Releases.VERSION_PATTERNS
                with(git) {
                    requireBranch.set(Releases.REQUIRE_BRANCH)
                    pushToRemote.set(Releases.PUSH_TO_REMOTE)
                }
            }
        }
    }

fun configurePublishingExtensionTest(project: Project) =
    funSpec {
        test("Configures Publishing extension") {
            val extension = project.getExtension<PublishingExtension>()

            with(extension) {
                shouldNotBeNull()

                val assertPublication = fun(publication: MavenPublication) {
                    with(publication) {
                        shouldNotBeNull()
                        artifacts.shouldNotBeNull()

                        with(pom as DefaultMavenPom) {
                            groupId shouldBe project.group
                            artifactId shouldBe project.name
                            version shouldBe project.version
                            name.get() shouldBe project.name
                            packaging shouldBe PackagingType.JAR.toString()
                            description.get() shouldBe ""
                            url.get() shouldBe "$OPENGOOD_ORG_URI/${project.name}"
                            with(scm) {
                                connection.get() shouldBe "${ScmProvider.PROTOCOL}:$GIT:$OPENGOOD_ORG_URI/${project.name}"
                                developerConnection.get() shouldBe "${ScmProvider.PROTOCOL}:$GIT:$OPENGOOD_ORG_URI/${project.name}"
                                url.get() shouldBe "$OPENGOOD_ORG_URI/${project.name}"
                            }
                            with(licenses.first()) {
                                name.get() shouldBe Licenses.NAME
                                url.get() shouldBe "$OPENGOOD_ORG_URI/${project.name}/$BLOB_ENDPOINT_NAME/$DEFAULT_BRANCH_NAME/$RESOURCE"
                            }
                            with(developers.first()) {
                                id.get() shouldBe Developers.ID
                                name.get() shouldBe Developers.NAME
                                email.get() shouldBe Developers.EMAIL
                            }
                        }
                    }
                }

                val assertRepository = fun(
                    repo: MavenArtifactRepository,
                    repoName: String,
                    repoUri: URI,
                    hasCredentials: Boolean,
                    publicationName: String?,
                ) {
                    with(repo) {
                        shouldNotBeNull()
                        name shouldBe repoName
                        url shouldBe repoUri
                        if (hasCredentials) {
                            with(credentials) {
                                username.shouldNotBeBlank()
                                password.shouldNotBeBlank()
                            }
                        }
                    }

                    if (publicationName != null) {
                        getTaskByName(
                            project,
                            String.format(Tasks.PUBLISH_PUBLICATION, publicationName, repoName),
                        ).shouldNotBeNull()
                    }
                }

                with(project.openGood().artifact) {
                    assertRepository(
                        getRepository(extension, Repositories.LOCAL_REPO_NAME),
                        Repositories.LOCAL_REPO_NAME,
                        project.repositories.mavenLocal().url,
                        false,
                        null,
                    )

                    if (publications.contains(PublicationType.GITHUB)) {
                        assertPublication(getPublication(extension, Publications.GITHUB))

                        assertRepository(
                            getRepository(extension, Repositories.GITHUB_PACKAGES_REPO_NAME),
                            Repositories.GITHUB_PACKAGES_REPO_NAME,
                            URI("$GITHUB_PACKAGES_REPO_BASE_URI/$OPENGOOD_ORG_NAME/${project.name}"),
                            true,
                            Publications.GITHUB,
                        )
                    }
                    if (publications.contains(PublicationType.OSS)) {
                        assertPublication(getPublication(extension, Publications.OSS))

                        val ossRepoName =
                            ((project.isSnapshotVersion) then { OSS_SNAPSHOTS_REPO_NAME })
                                ?: OSS_STAGING_REPO_NAME
                        val ossRepoUri =
                            ((project.isSnapshotVersion) then { Repositories.OSS_SNAPSHOTS_REPO_URI })
                                ?: Repositories.OSS_STAGING_REPO_URI

                        assertRepository(
                            getRepository(extension, ossRepoName),
                            ossRepoName,
                            URI(ossRepoUri),
                            true,
                            Publications.OSS,
                        )
                    }
                }
            }
        }
    }

fun configureSigningExtensionTest(project: Project) =
    funSpec {
        test("Configures Signing extension") {
            val extension = project.getExtension<SigningExtension>()

            with(extension) {
                shouldNotBeNull()

                val assertPublicationSigning = fun(task: Sign) {
                    with(task) {
                        shouldNotBeNull()
                        signatures.map { it.toString() }.shouldContainAnyOf(
                            "Signature ${Signatures.POM}",
                            "Signature ${String.format(Signatures.JAR, project.name, project.version)}",
                            "Signature ${String.format(Signatures.JAVADOC, project.name, project.version)}",
                            "Signature ${String.format(Signatures.SOURCES, project.name, project.version)}",
                        )
                    }
                }

                with(project.openGood().artifact) {
                    if (publications.contains(PublicationType.OSS)) {
                        assertPublicationSigning(
                            getTaskByTypeAndName(
                                project,
                                String.format(Tasks.SIGN_PUBLICATION, Publications.OSS),
                            ),
                        )
                    }
                }
            }
        }
    }

fun doNotConfigureDependencyManagementExtensionTest(project: Project) =
    funSpec {
        test("Does not configure Dependency Management extension") {
            val extension = project.getExtension<DependencyManagementExtension>()

            with(extension) {
                shouldNotBeNull()
                getBom(extension, Boms.KOTLIN).shouldBeNull()
            }
        }
    }

fun doNotConfigurePublishingExtensionTest(project: Project) =
    funSpec {
        test("Does not configure Publishing extension") {
            val extension = project.getExtension<PublishingExtension>()

            with(extension) {
                shouldNotBeNull()
                shouldThrow<UnknownRepositoryException> { getRepository(extension, Repositories.LOCAL_REPO_NAME) }
                shouldThrow<UnknownDomainObjectException> { getPublication(extension, Publications.GITHUB) }
                shouldThrow<UnknownDomainObjectException> { getPublication(extension, Publications.OSS) }

                shouldThrow<UnknownRepositoryException> {
                    getRepository(
                        extension,
                        Repositories.GITHUB_PACKAGES_REPO_NAME,
                    )
                }
                shouldThrow<UnknownRepositoryException> {
                    getRepository(
                        extension,
                        OSS_SNAPSHOTS_REPO_NAME,
                    )
                }
                shouldThrow<UnknownRepositoryException> {
                    getRepository(
                        extension,
                        OSS_STAGING_REPO_NAME,
                    )
                }
            }
        }
    }

fun doNotConfigureSigningExtensionTest(project: Project) =
    funSpec {
        test("Does not configure Signing extension") {
            val extension = project.getExtension<SigningExtension>()

            with(extension) {
                shouldNotBeNull()

                shouldThrow<UnknownTaskException> {
                    getTaskByTypeAndName<Sign>(
                        project,
                        String.format(Tasks.SIGN_PUBLICATION, Publications.GITHUB),
                    )
                }
                shouldThrow<UnknownTaskException> {
                    getTaskByTypeAndName<Sign>(
                        project,
                        String.format(Tasks.SIGN_PUBLICATION, Publications.OSS),
                    )
                }
            }
        }
    }
