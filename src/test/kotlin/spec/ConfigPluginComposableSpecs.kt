package spec

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import helper.*
import helper.expectedProperty
import helper.getConvention
import helper.getDependency
import helper.getPlugin
import helper.getRepository
import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeTypeOf
import io.opengood.gradle.ConfigPlugin
import io.opengood.gradle.constant.*
import io.opengood.gradle.enumeration.PackagingType
import io.opengood.gradle.enumeration.ScmProvider
import io.opengood.gradle.extension.opengood
import io.opengood.gradle.getExtension
import io.opengood.gradle.languageType
import org.gradle.api.Project
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact
import org.gradle.api.plugins.BasePluginConvention
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.Upload
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.internal.impldep.org.apache.maven.model.Model
import org.gradle.plugins.signing.Signature
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
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
    properties: Map<String, Any>
) = funSpec {
    test("Creates extension") {
        with(project.opengood()) {
            with(main) {
                languageType shouldBe project.languageType
                projectType shouldBe expectedProperty(properties, "projectType")
            }
            with(repo) {
                name shouldBe project.name
                baseUri shouldBe GitHub.OPENGOOD_ORG_URI
            }
            with(test) {
                maxParallelForks shouldBe Tests.MAX_PARALLEL_FORKS
            }
            with(artifact) {
                archiveBaseName shouldBe project.name
                name shouldBe project.name
                packaging shouldBe PackagingType.JAR
                description.shouldBeEmpty()
                uri shouldBe "${repo.baseUri}/${repo.name}"
                snapshotsUri shouldBe Repositories.OSS_SNAPSHOTS_REPO_URI
                stagingUri shouldBe Repositories.OSS_STAGING_REPO_URI
                with(scm) {
                    provider shouldBe ScmProvider.GIT
                    connection shouldBe "${ScmProvider.PROTOCOL}:$provider:${repo.baseUri}/${repo.name}"
                    developerConnection shouldBe "${ScmProvider.PROTOCOL}:$provider:${repo.baseUri}/${repo.name}"
                    uri shouldBe "${repo.baseUri}/${repo.name}"
                }
                with(licenses) {
                    if (isNotEmpty()) {
                        with(first()) {
                            id shouldBe Artifacts.LICENSE_ID
                            name shouldBe Artifacts.LICENSE_NAME
                            uri shouldBe "${repo.baseUri}/${repo.name}/${GitHub.RESOURCE_ENDPOINT}/${GitHub.BRANCH}/${Artifacts.LICENSE_RESOURCE}"
                        }
                    }
                }
                with(developers) {
                    if (isNotEmpty()) {
                        with(first()) {
                            id shouldBe Artifacts.DEVELOPER_ID
                            name shouldBe Artifacts.DEVELOPER_NAME
                            email shouldBe Artifacts.DEVELOPER_EMAIL
                        }
                    }
                }
            }
        }
    }
}

fun applyCommonPluginsTest(project: Project) = funSpec {
    test("Applies common plugins") {
        getPlugin(project, Plugins.BASE).shouldNotBeNull()
        getPlugin(project, Plugins.IDEA).shouldNotBeNull()
        getPlugin(project, Plugins.MAVEN).shouldNotBeNull()
        getPlugin(project, Plugins.MAVEN_PUBLISH).shouldNotBeNull()
        getPlugin(project, Plugins.SIGNING).shouldNotBeNull()
        getPlugin(project, Plugins.SPRING_BOOT).shouldNotBeNull()
        getPlugin(project, Plugins.SPRING_DEPENDENCY_MANAGEMENT).shouldNotBeNull()
        getPlugin(project, Plugins.VERSIONS).shouldNotBeNull()
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
        getRepository(project, project.repositories.mavenCentral().name).shouldNotBeNull()
        getRepository(project, project.repositories.jcenter().name).shouldNotBeNull()
        getRepository(project, project.repositories.gradlePluginPortal().name).shouldNotBeNull()
        getRepository(project, project.repositories.mavenLocal().name).shouldNotBeNull()
    }
}

fun addCommonDependenciesTest(project: Project) = funSpec {
    test("Adds common dependencies") {
        getDependency(project, "implementation", Dependencies.SPRING_BOOT_STARTER).shouldNotBeNull()
        getDependency(project, "annotationProcessor", Dependencies.SPRING_BOOT_CONFIG_PROCESSOR).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.SPRING_BOOT_STARTER_TEST).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.JUNIT_JUPITER).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.ASSERT_J).shouldNotBeNull()
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
                        url shouldBe "${GitHub.OPENGOOD_ORG_URI}/${project.name}/${GitHub.RESOURCE_ENDPOINT}/${GitHub.BRANCH}/${Artifacts.LICENSE_RESOURCE}"
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
    test("Configure artifacts") {
        val sourcesJar = getArtifact<ArchivePublishArtifact>(project, "archives", "sources", "jar", "jar")
        with(sourcesJar) {
            sourcesJar.shouldNotBeNull()
            archiveTask.name shouldBe "sourcesJar"
        }
    }
}

fun configureJavadocJarArtifactTest(project: Project) = funSpec {
    val javadocJar = getArtifact<ArchivePublishArtifact>(project, "archives", "javadoc", "jar", "jar")
    with(javadocJar) {
        javadocJar.shouldNotBeNull()
        archiveTask.name shouldBe "javadocJar"
    }
}

fun configureJarArtifactTest(project: Project) = funSpec {
    val jar = getArtifact<ArchivePublishArtifact>(project, "archives", "", "jar", "jar")
    with(jar) {
        jar.shouldNotBeNull()
        archiveTask.name shouldBe "jar"
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
        val extension = project.getExtension<SigningExtension>()

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
