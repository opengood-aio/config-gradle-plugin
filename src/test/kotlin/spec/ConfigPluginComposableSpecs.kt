package spec

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import helper.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import io.opengood.gradle.ConfigPlugin
import io.opengood.gradle.constant.*
import io.opengood.gradle.enumeration.PackagingType
import io.opengood.gradle.enumeration.ProjectType
import io.opengood.gradle.enumeration.ScmProvider
import io.opengood.gradle.extension.opengood
import io.opengood.gradle.getExtension
import io.opengood.gradle.languageType
import org.gradle.api.Project
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact
import org.gradle.api.plugins.BasePluginConvention
import org.gradle.api.plugins.UnknownPluginException
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.Upload
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType
import org.gradle.internal.impldep.org.apache.maven.model.Model
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.plugins.signing.Signature
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.dsl.SpringBootExtension
import org.springframework.boot.gradle.tasks.bundling.BootJar

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
            }
            with(test) {
                maxParallelForks shouldBe Tests.MAX_PARALLEL_FORKS
                with(multipleFrameworks) {
                    kotlin.shouldBeFalse()
                }
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
    }
}

fun applyCommonPluginsTest(project: Project) = funSpec {
    test("Applies common plugins") {
        getPlugin(project, Plugins.BASE).shouldNotBeNull()
        getPlugin(project, Plugins.IDEA).shouldNotBeNull()
        getPlugin(project, Plugins.MAVEN).shouldNotBeNull()
        getPlugin(project, Plugins.MAVEN_PUBLISH).shouldNotBeNull()
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

fun configureConventionsTest(project: Project) = funSpec {
    test("Configures conventions") {
        val basePluginConvention = getConvention<BasePluginConvention>(project)
        basePluginConvention.archivesBaseName shouldBe project.name
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

fun doNotAddGroovyDependenciesTest(project: Project) = funSpec {
    test("Does not add Groovy dependencies") {
        getDependency(project, "implementation", Dependencies.GROOVY).shouldBeNull()
    }
}

fun doNotAddKotlinDependenciesTest(project: Project) = funSpec {
    test("Does not add Kotlin dependencies") {
        getDependency(project, "implementation", Dependencies.KOTLIN_STD_LIB).shouldBeNull()
        getDependency(project, "implementation", Dependencies.KOTLIN_REFLECT).shouldBeNull()
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

fun configureGradleWrapperTaskTest(project: Project) = funSpec {
    test("Configures Gradle Wrapper task") {
        val task = getTask<Wrapper>(project)

        with(task) {
            task.shouldNotBeNull()
            distributionType shouldBe DistributionType.ALL
        }
    }
}

fun configureKotlinCompileTaskTest(project: Project) = funSpec {
    test("Configures Kotlin Compile task") {
        val task = getTask<KotlinCompile>(project, "compileKotlin")

        with(task) {
            task.shouldNotBeNull()
            kotlinOptions.freeCompilerArgs shouldBe KotlinOptions.FREE_COMPILER_ARGS
            kotlinOptions.jvmTarget shouldBe Versions.JAVA
        }
    }
}

fun configureJavaCompileTaskTest(project: Project) = funSpec {
    test("Configures Java Compile task") {
        val task = getTask<JavaCompile>(project, "compileJava")

        with(task) {
            task.shouldNotBeNull()
            sourceCompatibility shouldBe Versions.JAVA
            targetCompatibility shouldBe Versions.JAVA
        }
    }
}

fun configureProcessResourcesTaskTest(project: Project) = funSpec {
    test("Configures Process Resources task") {
        val task = getTask<ProcessResources>(project)

        with(task) {
            task.shouldNotBeNull()
            destinationDir.absolutePath shouldContain "resources/main"
        }
    }
}

fun configureDependencyUpdatesTaskTest(project: Project) = funSpec {
    test("Configures Dependency Updates task") {
        val task = getTask<DependencyUpdatesTask>(project, "dependencyUpdates")

        task.shouldNotBeNull()
    }
}

fun configureTestTaskTest(project: Project) = funSpec {
    test("Configures Test task") {
        val task = getTask<Test>(project, "test")

        with(task) {
            task.shouldNotBeNull()
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

fun configureJarTaskTest(project: Project, isEnabled: Boolean) = funSpec {
    test("Configures Jar task") {
        val task = getTask<Jar>(project, "jar")

        with(task) {
            task.shouldNotBeNull()
            enabled shouldBe isEnabled
        }
    }
}

fun configureBootJarTaskTest(project: Project, isEnabled: Boolean) = funSpec {
    test("Configures Boot Jar task") {
        val task = getTask<BootJar>(project, "bootJar")

        with(task) {
            task.shouldNotBeNull()
            enabled shouldBe isEnabled
        }
    }
}

fun configureUploadArchivesTaskTest(project: Project) = funSpec {
    test("Configures Upload Archives task") {
        val task = getTask<Upload>(project)

        with(task) {
            task.shouldNotBeNull()

            with(getMavenDeployer(repositories)) {
                with(snapshotRepository) {
                    url shouldBe Repositories.OSS_SNAPSHOTS_REPO_URI
                    with(authentication) {
                        userName.shouldBeEmpty()
                        password.shouldBeEmpty()
                    }
                }
                with(repository) {
                    url shouldBe Repositories.OSS_STAGING_REPO_URI
                    with(authentication) {
                        userName.shouldBeEmpty()
                        password.shouldBeEmpty()
                    }
                }

                val pom = pom.effectivePom.model as Model
                with(pom) {
                    groupId shouldBe project.group
                    artifactId shouldBe project.name
                    version shouldBe project.version
                    name shouldBe project.name
                    packaging shouldBe PackagingType.JAR.toString()
                    description.shouldBeEmpty()
                    url shouldBe "${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                    with(scm) {
                        connection shouldBe "${ScmProvider.PROTOCOL}:${ScmProvider.GIT}:${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                        developerConnection shouldBe "${ScmProvider.PROTOCOL}:${ScmProvider.GIT}:${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                        url shouldBe "${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                    }
                    with(licenses.first()) {
                        name shouldBe Artifacts.LICENSE_NAME
                        url shouldBe "${GitHub.OPENGOOD_ORG_URI}/${project.name}/${GitHub.BLOB_ENDPOINT}/${GitHub.BRANCH}/${Artifacts.LICENSE_RESOURCE}"
                    }
                    with(developers.first()) {
                        id shouldBe Artifacts.DEVELOPER_ID
                        name shouldBe Artifacts.DEVELOPER_NAME
                        email shouldBe Artifacts.DEVELOPER_EMAIL
                    }
                }
            }
        }
    }
}

fun configureSourcesJarArtifactTest(project: Project) = funSpec {
    test("Configures sources jar artifacts") {
        val sourcesJar = getArtifact<ArchivePublishArtifact>(project, "archives", "sources", "jar", "jar")
        with(sourcesJar) {
            sourcesJar.shouldNotBeNull()
            archiveTask.name shouldBe "sourcesJar"
        }
    }
}

fun configureJavadocJarArtifactTest(project: Project) = funSpec {
    test("Configures Javadoc jar artifacts") {
        val javadocJar = getArtifact<ArchivePublishArtifact>(project, "archives", "javadoc", "jar", "jar")
        with(javadocJar) {
            javadocJar.shouldNotBeNull()
            archiveTask.name shouldBe "javadocJar"
        }
    }
}

fun configureJarArtifactTest(project: Project) = funSpec {
    test("Configures jar artifacts") {
        val jar = getArtifact<ArchivePublishArtifact>(project, "archives", "", "jar", "jar")
        with(jar) {
            jar.shouldNotBeNull()
            archiveTask.name shouldBe "jar"
        }
    }
}

fun configureSpringBootExtensionTest(project: Project) = funSpec {
    test("Configures Spring Boot extension") {
        val extension = project.getExtension<SpringBootExtension>()

        extension.shouldNotBeNull()
    }
}

fun configurePublishingExtensionTest(project: Project) = funSpec {
    test("Configures Publishing extension") {
        val extension = project.getExtension<PublishingExtension>()

        extension.shouldNotBeNull()

        val mavenJavaPublication = getMavenPublication(extension, "mavenJava")
        with(mavenJavaPublication) {
            artifacts.shouldNotBeNull()
        }

        val mavenLocalRepo = getMavenRepository(extension, Repositories.LOCAL_REPO_NAME)
        with(mavenLocalRepo) {
            name shouldBe Repositories.LOCAL_REPO_NAME
            url shouldBe project.repositories.mavenLocal().url
        }
    }
}

fun configureSigningExtensionTest(project: Project) = funSpec {
    test("Configures Signing extension") {
        project.getExtension<SigningExtension>().shouldNotBeNull()
    }
}

fun configureSourcesJarSigningTest(project: Project) = funSpec {
    test("Configures sources jar signing") {
        val sourcesJar = getArtifact<Signature>(project, "archives", "sources", "jar.asc", "asc")
        with(sourcesJar) {
            sourcesJar.shouldNotBeNull()
            signatureSpec.toString().shouldContain("signArchives")
        }
    }
}

fun configureJavadocJarSigningTest(project: Project) = funSpec {
    test("Configures Javadoc jar signing") {
        val javadocJar = getArtifact<Signature>(project, "archives", "javadoc", "jar.asc", "asc")
        with(javadocJar) {
            javadocJar.shouldNotBeNull()
            signatureSpec.toString().shouldContain("signArchives")
        }
    }
}

fun configureJarSigningTest(project: Project) = funSpec {
    test("Configures jar signing") {
        val jar = getArtifact<Signature>(project, "archives", "", "jar.asc", "asc")
        with(jar) {
            jar.shouldNotBeNull()
            signatureSpec.toString().shouldContain("signArchives")
        }
    }
}
