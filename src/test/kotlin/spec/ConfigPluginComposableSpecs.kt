package spec

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import helper.getConvention
import helper.getDependency
import helper.getMavenBom
import helper.getMavenPublication
import helper.getMavenRepository
import helper.getPlugin
import helper.getRepository
import helper.getTaskByName
import helper.getTaskByType
import helper.getTaskByTypeAndName
import helper.hasTaskFinalizedByDependency
import helper.then
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAnyOf
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import io.opengood.gradle.ConfigPlugin
import io.opengood.gradle.constant.Artifacts
import io.opengood.gradle.constant.Boms
import io.opengood.gradle.constant.Dependencies
import io.opengood.gradle.constant.GitHub
import io.opengood.gradle.constant.KotlinOptions
import io.opengood.gradle.constant.Plugins
import io.opengood.gradle.constant.Publications
import io.opengood.gradle.constant.Releases
import io.opengood.gradle.constant.Repositories
import io.opengood.gradle.constant.Tests
import io.opengood.gradle.constant.Versions
import io.opengood.gradle.enumeration.PackagingType
import io.opengood.gradle.enumeration.ProjectType
import io.opengood.gradle.enumeration.ScmProvider
import io.opengood.gradle.extension.opengood
import io.opengood.gradle.getExtension
import io.opengood.gradle.git
import io.opengood.gradle.isSnapshotVersion
import io.opengood.gradle.languageType
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import net.researchgate.release.GitAdapter
import net.researchgate.release.ReleaseExtension
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.UnknownTaskException
import org.gradle.api.artifacts.UnknownRepositoryException
import org.gradle.api.plugins.BasePluginConvention
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.UnknownPluginException
import org.gradle.api.publish.PublishingExtension
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
import java.net.URI

fun applyPluginTest(project: Project) = funSpec {
    test("Applies plugin") {
        getPlugin(project, ConfigPlugin.PLUGIN_ID).shouldNotBeNull()
        getPlugin(project, ConfigPlugin.PLUGIN_ID).shouldBeTypeOf<ConfigPlugin>()
        getPlugin<ConfigPlugin>(project).shouldBeTypeOf<ConfigPlugin>()
    }
}

fun createExtensionTest(
    project: Project,
    projectType: ProjectType
) = funSpec {
    test("Creates extension") {
        with(project.opengood()) {
            shouldNotBeNull()

            with(main) {
                languageType shouldBe project.languageType
                main.projectType shouldBe projectType
            }
            with(features) {
                spring.shouldBeTrue()
                lombok.shouldBeTrue()
                junit.shouldBeTrue()
                assertj.shouldBeTrue()
                mockito.shouldBeTrue()
                kotest.shouldBeTrue()
                mockk.shouldBeTrue()
                publishing.shouldBeTrue()
            }
            with(test) {
                maxParallelForks shouldBe Tests.MAX_PARALLEL_FORKS
                with(testFrameworks) {
                    java.shouldBeFalse()
                }
            }
            with(release) {
                afterReleaseBuildTasks shouldBe Releases.AFTER_RELEASE_BUILD_TASKS
                requireBranch shouldBe Releases.REQUIRE_BRANCH
                pushToRemote shouldBe Releases.PUSH_TO_REMOTE
            }
            with(artifact) {
                name shouldBe project.name
                packaging shouldBe PackagingType.JAR
                description.shouldBeEmpty()
                uri shouldBe "${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                with(repo) {
                    snapshotsRepoUri shouldBe Repositories.OSS_SNAPSHOTS_REPO_URI
                    stagingRepoUri shouldBe Repositories.OSS_STAGING_REPO_URI
                }
                with(scm) {
                    provider shouldBe ScmProvider.GIT
                    connection shouldBe "${ScmProvider.PROTOCOL}:$provider:${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                    developerConnection shouldBe "${ScmProvider.PROTOCOL}:$provider:${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                    uri shouldBe "${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                }
                with(license) {
                    name shouldBe Artifacts.LICENSE_NAME
                    uri shouldBe "${GitHub.OPENGOOD_ORG_URI}/${project.name}/${GitHub.BLOB_ENDPOINT}/${GitHub.BRANCH}/${Artifacts.LICENSE_RESOURCE}"
                }
                with(developer) {
                    id shouldBe Artifacts.DEVELOPER_ID
                    name shouldBe Artifacts.DEVELOPER_NAME
                    email shouldBe Artifacts.DEVELOPER_EMAIL
                }
            }
        }
    }
}

fun applyGroovyPluginTest(project: Project) = funSpec {
    test("Applies Groovy plugin") {
        getPlugin(project, Plugins.GROOVY).shouldNotBeNull()
    }
}

fun applyJavaPluginTest(project: Project) = funSpec {
    test("Applies Java plugin") {
        getPlugin(project, Plugins.JAVA).shouldNotBeNull()
    }
}

fun applyKotlinPluginsTest(project: Project) = funSpec {
    test("Applies Kotlin plugins") {
        getPlugin(project, Plugins.KOTLIN_JVM).shouldNotBeNull()
        getPlugin(project, Plugins.KOTLIN_ALL_OPEN).shouldNotBeNull()
        getPlugin(project, Plugins.KOTLIN_NO_ARG).shouldNotBeNull()
    }
}

fun applyCommonPluginsTest(project: Project) = funSpec {
    test("Applies common plugins") {
        getPlugin(project, Plugins.BASE).shouldNotBeNull()
        getPlugin(project, Plugins.IDEA).shouldNotBeNull()
        getPlugin(project, Plugins.JACOCO).shouldNotBeNull()
        getPlugin(project, Plugins.MAVEN_PUBLISH).shouldNotBeNull()
        getPlugin(project, Plugins.RELEASE).shouldNotBeNull()
        getPlugin(project, Plugins.SIGNING).shouldNotBeNull()
        getPlugin(project, Plugins.VERSIONS).shouldNotBeNull()
    }
}

fun applyLibraryPluginTest(project: Project) = funSpec {
    test("Applies library plugin") {
        getPlugin(project, Plugins.JAVA_LIBRARY).shouldNotBeNull()
    }
}

fun applySpringPluginsTest(project: Project) = funSpec {
    test("Applies Spring plugins") {
        getPlugin(project, Plugins.SPRING_BOOT).shouldNotBeNull()
        getPlugin(project, Plugins.SPRING_DEPENDENCY_MANAGEMENT).shouldNotBeNull()
    }
}

fun applyKotlinSpringPluginsTest(project: Project) = funSpec {
    test("Applies Kotlin Spring plugins") {
        getPlugin(project, Plugins.KOTLIN_SPRING).shouldNotBeNull()
    }
}

fun applyLombokPluginTest(project: Project) = funSpec {
    test("Applies Lombok plugin") {
        getPlugin(project, Plugins.LOMBOK).shouldNotBeNull()
    }
}

fun doNotApplyGroovyPluginTest(project: Project) = funSpec {
    test("Does not apply Groovy plugin") {
        shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.GROOVY).shouldNotBeNull() }
    }
}

fun dotNotApplyJavaPluginTest(project: Project) = funSpec {
    test("Does not apply Java plugin") {
        shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.JAVA).shouldNotBeNull() }
    }
}

fun doNotApplyKotlinPluginsTest(project: Project) = funSpec {
    test("Does not apply Kotlin plugins") {
        shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.KOTLIN_JVM).shouldNotBeNull() }
        shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.KOTLIN_ALL_OPEN).shouldNotBeNull() }
        shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.KOTLIN_NO_ARG).shouldNotBeNull() }
    }
}

fun doNotApplyLibraryPluginTest(project: Project) = funSpec {
    test("Does not apply library plugin") {
        shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.JAVA_LIBRARY).shouldNotBeNull() }
    }
}

fun doNotApplySpringPluginsTest(project: Project) = funSpec {
    test("Does not apply Spring plugins") {
        shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.SPRING_BOOT) }
        shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.SPRING_DEPENDENCY_MANAGEMENT) }
    }
}

fun doNotApplyKotlinSpringPluginsTest(project: Project) = funSpec {
    test("Does not apply Kotlin Spring plugins") {
        shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.KOTLIN_SPRING) }
    }
}

fun doNotApplyLombokPluginTest(project: Project) = funSpec {
    test("Does not apply Lombok plugin") {
        shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.LOMBOK) }
    }
}

fun configureDependencyResolutionStrategyTest(project: Project) = funSpec {
    test("Configures dependency resolution strategy") {
        project.configurations.all { configuration ->
            with(configuration) {
                resolutionStrategy.eachDependency { resolver ->
                    with(resolver) {
                        if (requested.group == "org.jetbrains.kotlin") {
                            requested.version shouldBe Versions.KOTLIN
                        }
                    }
                }
            }
        }
    }
}

fun configureConventionsTest(project: Project) = funSpec {
    test("Configures conventions") {
        val basePluginConvention = getConvention<BasePluginConvention>(project)

        with(basePluginConvention) {
            shouldNotBeNull()
            archivesBaseName shouldBe project.name
        }
    }
}

fun addRepositoriesTest(project: Project) = funSpec {
    test("Adds repositories") {
        with(project.repositories) {
            getRepository(project, mavenCentral().name).shouldNotBeNull()
            getRepository(project, jcenter().name).shouldNotBeNull()
            getRepository(project, gradlePluginPortal().name).shouldNotBeNull()
            getRepository(project, mavenLocal().name).shouldNotBeNull()
        }
    }
}

fun addGroovyDependenciesTest(project: Project) = funSpec {
    test("Adds Groovy dependencies") {
        getDependency(project, "implementation", Dependencies.GROOVY).shouldNotBeNull()
    }
}

fun addKotlinDependenciesTest(project: Project) = funSpec {
    test("Adds Kotlin dependencies") {
        getDependency(project, "implementation", Dependencies.KOTLIN_STD_LIB).shouldNotBeNull()
        getDependency(project, "implementation", Dependencies.KOTLIN_REFLECT).shouldNotBeNull()
        getDependency(project, "implementation", Dependencies.KOTLIN_COROUTINES).shouldNotBeNull()
        getDependency(project, "implementation", Dependencies.JACKSON_KOTLIN).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTLIN_TEST).shouldNotBeNull()
    }
}

fun addSpringDependenciesTest(project: Project) = funSpec {
    test("Adds Spring dependencies") {
        getDependency(project, "implementation", Dependencies.SPRING_BOOT_STARTER).shouldNotBeNull()
        getDependency(project, "annotationProcessor", Dependencies.SPRING_BOOT_CONFIG_PROCESSOR).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.SPRING_BOOT_STARTER_TEST).shouldNotBeNull()
    }
}

fun addLombokDependenciesTest(project: Project) = funSpec {
    test("Adds Lombok dependencies") {
        getDependency(project, "implementation", Dependencies.LOMBOK).shouldNotBeNull()
        getDependency(project, "annotationProcessor", Dependencies.LOMBOK).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.LOMBOK).shouldNotBeNull()
        getDependency(project, "testAnnotationProcessor", Dependencies.LOMBOK).shouldNotBeNull()
    }
}

fun addJunitDependenciesTest(project: Project) = funSpec {
    test("Adds JUnit dependencies") {
        getDependency(project, "testImplementation", Dependencies.JUNIT_JUPITER).shouldNotBeNull()
    }
}

fun addAssertjDependenciesTest(project: Project) = funSpec {
    test("Adds AssertJ dependencies") {
        getDependency(project, "testImplementation", Dependencies.ASSERTJ).shouldNotBeNull()
    }
}

fun addMockitoDependenciesTest(project: Project) = funSpec {
    test("Adds Mockito dependencies") {
        getDependency(project, "testImplementation", Dependencies.MOCKITO).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.MOCKITO_JUNIT_JUPITER).shouldNotBeNull()
    }
}

fun addKotestDependenciesTest(project: Project) = funSpec {
    test("Adds Kotest dependencies") {
        getDependency(project, "testImplementation", Dependencies.KOTEST_JUNIT_RUNNER).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTEST_JUNIT_EXTENSIONS).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTEST_SPRING_EXTENSIONS).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTEST_KOIN_EXTENSIONS).shouldNotBeNull()
    }
}

fun addMockkDependenciesTest(project: Project) = funSpec {
    test("Adds MockK dependencies") {
        getDependency(project, "testImplementation", Dependencies.MOCKK).shouldNotBeNull()
    }
}

fun addSpringMockkDependenciesTest(project: Project) = funSpec {
    test("Adds Spring MockK dependencies") {
        getDependency(project, "testImplementation", Dependencies.SPRING_MOCKK).shouldNotBeNull()
    }
}

fun doNotAddGroovyDependenciesTest(project: Project) = funSpec {
    test("Does not add Groovy dependencies") {
        getDependency(project, "implementation", Dependencies.GROOVY).shouldBeNull()
    }
}

fun doNotAddKotlinDependenciesTest(project: Project) = funSpec {
    test("Does not add Kotlin dependencies") {
        getDependency(project, "implementation", Dependencies.KOTLIN_STD_LIB).shouldBeNull()
        getDependency(project, "implementation", Dependencies.KOTLIN_REFLECT).shouldBeNull()
        getDependency(project, "implementation", Dependencies.KOTLIN_COROUTINES).shouldBeNull()
        getDependency(project, "implementation", Dependencies.JACKSON_KOTLIN).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTLIN_TEST).shouldBeNull()
    }
}

fun doNotAddSpringDependenciesTest(project: Project) = funSpec {
    test("Does not add Spring dependencies") {
        getDependency(project, "implementation", Dependencies.SPRING_BOOT_STARTER).shouldBeNull()
        getDependency(project, "annotationProcessor", Dependencies.SPRING_BOOT_CONFIG_PROCESSOR).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.SPRING_BOOT_STARTER_TEST).shouldBeNull()
    }
}

fun doNotAddLombokDependenciesTest(project: Project) = funSpec {
    test("Does not add Lombok dependencies") {
        getDependency(project, "implementation", Dependencies.LOMBOK).shouldBeNull()
        getDependency(project, "annotationProcessor", Dependencies.LOMBOK).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.LOMBOK).shouldBeNull()
        getDependency(project, "testAnnotationProcessor", Dependencies.LOMBOK).shouldBeNull()
    }
}

fun doNotAddJunitDependenciesTest(project: Project) = funSpec {
    test("Does not add JUnit dependencies") {
        getDependency(project, "testImplementation", Dependencies.JUNIT_JUPITER).shouldBeNull()
    }
}

fun doNotAddAssertjDependenciesTest(project: Project) = funSpec {
    test("Does not add AssertJ dependencies") {
        getDependency(project, "testImplementation", Dependencies.ASSERTJ).shouldBeNull()
    }
}

fun doNotAddMockitoDependenciesTest(project: Project) = funSpec {
    test("Does not add Mockito dependencies") {
        getDependency(project, "testImplementation", Dependencies.MOCKITO).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.MOCKITO_JUNIT_JUPITER).shouldBeNull()
    }
}

fun doNotAddKotestDependenciesTest(project: Project) = funSpec {
    test("Does not add Kotest dependencies") {
        getDependency(project, "testImplementation", Dependencies.KOTEST_JUNIT_RUNNER).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTEST_JUNIT_EXTENSIONS).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTEST_SPRING_EXTENSIONS).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTEST_KOIN_EXTENSIONS).shouldBeNull()
    }
}

fun doNotAddMockkDependenciesTest(project: Project) = funSpec {
    test("Does not add MockK dependencies") {
        getDependency(project, "testImplementation", Dependencies.MOCKK).shouldBeNull()
    }
}

fun doNotAddSpringMockkDependenciesTest(project: Project) = funSpec {
    test("Does not add Spring MockK dependencies") {
        getDependency(project, "testImplementation", Dependencies.SPRING_MOCKK).shouldBeNull()
    }
}

fun configureGradleWrapperTaskTest(project: Project) = funSpec {
    test("Configures Gradle Wrapper task") {
        val task = getTaskByType<Wrapper>(project)

        with(task) {
            shouldNotBeNull()
            distributionType shouldBe DistributionType.ALL
        }
    }
}

fun configureKotlinCompileTaskTest(project: Project) = funSpec {
    test("Configures Kotlin Compile task") {
        val task = getTaskByTypeAndName<KotlinCompile>(project, "compileKotlin")

        with(task) {
            shouldNotBeNull()
            kotlinOptions.freeCompilerArgs shouldBe KotlinOptions.FREE_COMPILER_ARGS
            kotlinOptions.jvmTarget shouldBe Versions.JAVA
        }
    }
}

fun configureJavaCompileTaskTest(project: Project) = funSpec {
    test("Configures Java Compile task") {
        val task = getTaskByTypeAndName<JavaCompile>(project, "compileJava")

        with(task) {
            shouldNotBeNull()
            sourceCompatibility shouldBe Versions.JAVA
            targetCompatibility shouldBe Versions.JAVA
        }
    }
}

fun configureProcessResourcesTaskTest(project: Project) = funSpec {
    test("Configures Process Resources task") {
        val task = getTaskByType<ProcessResources>(project)

        with(task) {
            shouldNotBeNull()
            destinationDir.absolutePath shouldContain "resources/main"
        }
    }
}

fun configureDependencyUpdatesTaskTest(project: Project) = funSpec {
    test("Configures Dependency Updates task") {
        val task = getTaskByTypeAndName<DependencyUpdatesTask>(project, "dependencyUpdates")

        with(task) {
            shouldNotBeNull()
        }
    }
}

fun configureTestTaskTest(project: Project) = funSpec {
    test("Configures Test task") {
        val task = getTaskByTypeAndName<Test>(project, "test")

        with(task) {
            shouldNotBeNull()
            hasTaskFinalizedByDependency(task, "jacocoTestReport").shouldBeTrue()
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

fun configureJacocoTestReportTaskTest(project: Project) = funSpec {
    test("Configures Jacoco Test Report task") {
        val task = getTaskByTypeAndName<JacocoReport>(project, "jacocoTestReport")

        with(task) {
            shouldNotBeNull()
            with(reports) {
                shouldNotBeNull()
                xml.isEnabled.shouldBeTrue()
                html.isEnabled.shouldBeFalse()
            }
        }
    }
}

fun configureJarTaskTest(project: Project, isEnabled: Boolean) = funSpec {
    test("Configures Jar task") {
        val task = getTaskByTypeAndName<Jar>(project, "jar")

        with(task) {
            shouldNotBeNull()
            enabled shouldBe isEnabled
        }
    }
}

fun configureBootJarTaskTest(project: Project, isEnabled: Boolean) = funSpec {
    test("Configures Boot Jar task") {
        val task = getTaskByTypeAndName<BootJar>(project, "bootJar")

        with(task) {
            shouldNotBeNull()
            enabled shouldBe isEnabled
        }
    }
}

fun configureAfterReleaseBuildTaskTest(project: Project) = funSpec {
    test("Configures After Release Build task") {
        val task = getTaskByName(project, "afterReleaseBuild")

        with(task) {
            shouldNotBeNull()
            dependsOn.contains(Releases.AFTER_RELEASE_BUILD_TASKS).shouldBeTrue()
        }
    }
}

fun doNotConfigureAfterReleaseBuildTaskTest(project: Project) = funSpec {
    test("Does not configure After Release Build task") {
        val task = getTaskByName(project, "afterReleaseBuild")

        with(task) {
            shouldNotBeNull()
            dependsOn.contains(Releases.AFTER_RELEASE_BUILD_TASKS).shouldBeFalse()
        }
    }
}

fun configureDependencyManagementExtensionTest(project: Project) = funSpec {
    test("Configures Dependency Management extension") {
        val extension = project.getExtension<DependencyManagementExtension>()

        with(extension) {
            shouldNotBeNull()
            getMavenBom(extension, Boms.KOTLIN).shouldNotBeNull()
        }
    }
}

fun configureJavaExtensionTest(project: Project) = funSpec {
    test("Configures Java extension") {
        val extension = project.getExtension<JavaPluginExtension>()

        with(extension) {
            shouldNotBeNull()
            getTaskByName(project, "javadocJar").shouldNotBeNull()
            getTaskByName(project, "sourcesJar").shouldNotBeNull()
        }
    }
}

fun configureSpringBootExtensionTest(project: Project) = funSpec {
    test("Configures Spring Boot extension") {
        val extension = project.getExtension<SpringBootExtension>()

        with(extension) {
            shouldNotBeNull()
        }
    }
}

fun configureReleaseExtensionTest(project: Project) = funSpec {
    test("Configures Release extension") {
        val extension = project.getExtension<ReleaseExtension>()

        with(extension) {
            shouldNotBeNull()
            scmAdapters shouldContain GitAdapter::class.java
            preTagCommitMessage shouldBe Releases.PRE_TAG_COMMIT_MESSAGE
            newVersionCommitMessage shouldBe Releases.NEW_VERSION_COMMIT_MESSAGE
            versionPatterns shouldBe Releases.VERSION_PATTERNS
            with(git) {
                requireBranch = Releases.REQUIRE_BRANCH
                pushToRemote = Releases.PUSH_TO_REMOTE
            }
        }
    }
}

fun configurePublishingExtensionTest(project: Project) = funSpec {
    test("Configures Publishing extension") {
        val extension = project.getExtension<PublishingExtension>()

        with(extension) {
            shouldNotBeNull()

            val publication = getMavenPublication(extension, Publications.OSS_PUB_NAME)
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
                    url.get() shouldBe "${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                    with(scm) {
                        connection.get() shouldBe "${ScmProvider.PROTOCOL}:${ScmProvider.GIT}:${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                        developerConnection.get() shouldBe "${ScmProvider.PROTOCOL}:${ScmProvider.GIT}:${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                        url.get() shouldBe "${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                    }
                    with(licenses.first()) {
                        name.get() shouldBe Artifacts.LICENSE_NAME
                        url.get() shouldBe "${GitHub.OPENGOOD_ORG_URI}/${project.name}/${GitHub.BLOB_ENDPOINT}/${GitHub.BRANCH}/${Artifacts.LICENSE_RESOURCE}"
                    }
                    with(developers.first()) {
                        id.get() shouldBe Artifacts.DEVELOPER_ID
                        name.get() shouldBe Artifacts.DEVELOPER_NAME
                        email.get() shouldBe Artifacts.DEVELOPER_EMAIL
                    }
                }
            }

            val mavenLocalRepo = getMavenRepository(extension, Repositories.LOCAL_REPO_NAME)
            with(mavenLocalRepo) {
                shouldNotBeNull()
                name shouldBe Repositories.LOCAL_REPO_NAME
                url shouldBe project.repositories.mavenLocal().url
            }

            val ossRepoName = (project.isSnapshotVersion) then { Repositories.OSS_SNAPSHOTS_REPO_NAME }
                ?: Repositories.OSS_STAGING_REPO_NAME
            val ossRepoUri = URI(
                (project.isSnapshotVersion) then { Repositories.OSS_SNAPSHOTS_REPO_URI }
                    ?: Repositories.OSS_STAGING_REPO_URI
            )

            val ossRepo = getMavenRepository(extension, ossRepoName)
            with(ossRepo) {
                shouldNotBeNull()
                name shouldBe ossRepoName
                url shouldBe ossRepoUri
                with(credentials) {
                    username.shouldBeEmpty()
                    password.shouldBeEmpty()
                }
            }

            getTaskByName(
                project,
                "publish${Publications.OSS_PUB_NAME}PublicationTo${ossRepoName}Repository"
            ).shouldNotBeNull()
        }
    }
}

fun configureSigningExtensionTest(project: Project) = funSpec {
    test("Configures Signing extension") {
        val extension = project.getExtension<SigningExtension>()

        with(extension) {
            shouldNotBeNull()

            val task = getTaskByTypeAndName<Sign>(project, "sign${Publications.OSS_PUB_NAME}Publication")
            with(task) {
                shouldNotBeNull()
                signatures.map { it.toString() }.shouldContainAnyOf(
                    "Signature pom-default.xml.asc:xml.asc:asc:null",
                    "Signature ${project.name}-${project.version}.jar.asc:jar.asc:asc:asc:null",
                    "Signature ${project.name}-${project.version}-javadoc.jar.asc:jar.asc:asc:asc:null",
                    "Signature ${project.name}-${project.version}-sources.jar.asc:jar.asc:asc:asc:null"
                )
            }

            getTaskByName(project, "sign${Publications.OSS_PUB_NAME}Publication").shouldNotBeNull()
        }
    }
}

fun doNotConfigureDependencyManagementExtensionTest(project: Project) = funSpec {
    test("Does not configure Dependency Management extension") {
        val extension = project.getExtension<DependencyManagementExtension>()

        with(extension) {
            shouldNotBeNull()
            getMavenBom(extension, Boms.KOTLIN).shouldBeNull()
        }
    }
}

fun doNotConfigurePublishingExtensionTest(project: Project) = funSpec {
    test("Does not configure Publishing extension") {
        val extension = project.getExtension<PublishingExtension>()

        with(extension) {
            shouldNotBeNull()
            shouldThrow<UnknownDomainObjectException> { getMavenPublication(extension, Publications.OSS_PUB_NAME) }
            shouldThrow<UnknownRepositoryException> { getMavenRepository(extension, Repositories.LOCAL_REPO_NAME) }
            shouldThrow<UnknownRepositoryException> {
                getMavenRepository(
                    extension,
                    Repositories.OSS_SNAPSHOTS_REPO_NAME
                )
            }
            shouldThrow<UnknownRepositoryException> {
                getMavenRepository(
                    extension,
                    Repositories.OSS_STAGING_REPO_NAME
                )
            }
        }
    }
}

fun doNotConfigureSigningExtensionTest(project: Project) = funSpec {
    test("Does not configure Signing extension") {
        val extension = project.getExtension<SigningExtension>()

        with(extension) {
            shouldNotBeNull()

            shouldThrow<UnknownTaskException> {
                getTaskByTypeAndName<Sign>(
                    project,
                    "sign${Publications.OSS_PUB_NAME}Publication"
                )
            }
        }
    }
}
