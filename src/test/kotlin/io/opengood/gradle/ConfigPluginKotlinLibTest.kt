package io.opengood.gradle

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import helper.*
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotBeBlank
import io.opengood.gradle.constant.*
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.PackagingType
import io.opengood.gradle.enumeration.ProjectType
import io.opengood.gradle.enumeration.ScmProvider
import io.opengood.gradle.extension.opengood
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

class ConfigPluginKotlinLibTest : WordSpec({

    "Configure Plugin" should {
        val project = createProject(
            languageType = LanguageType.KOTLIN,
            projectType = ProjectType.LIB
        )

        include(applyPluginTest(project))

        "Create Plugin Extension" {
            with(project.opengood()) {
                with(main) {
                    languageType shouldBe LanguageType.KOTLIN
                    projectType shouldBe ProjectType.LIB
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
                                id.shouldNotBeBlank()
                                name.shouldBeEmpty()
                                uri.shouldBeEmpty()
                            }
                        }
                    }
                    with(developers) {
                        if (isNotEmpty()) {
                            with(first()) {
                                id.shouldNotBeBlank()
                                name.shouldBeEmpty()
                                email.shouldBeEmpty()
                            }
                        }
                    }
                }
            }
        }

        "Apply Plugins" {
            getPlugin(project, Plugins.BASE).shouldNotBeNull()
            getPlugin(project, Plugins.IDEA).shouldNotBeNull()
            getPlugin(project, Plugins.JAVA_LIBRARY).shouldNotBeNull()
            getPlugin(project, Plugins.KOTLIN).shouldNotBeNull()
            getPlugin(project, Plugins.KOTLIN_SPRING).shouldNotBeNull()
            getPlugin(project, Plugins.MAVEN).shouldNotBeNull()
            getPlugin(project, Plugins.MAVEN_PUBLISH).shouldNotBeNull()
            getPlugin(project, Plugins.SIGNING).shouldNotBeNull()
            getPlugin(project, Plugins.SPRING_BOOT).shouldNotBeNull()
            getPlugin(project, Plugins.SPRING_DEPENDENCY_MANAGEMENT).shouldNotBeNull()
            getPlugin(project, Plugins.VERSIONS).shouldNotBeNull()

            val basePluginConvention = getConvention<BasePluginConvention>(project)
            basePluginConvention.archivesBaseName shouldBe project.name
        }

        "Add Repositories" {
            getRepository(project, project.repositories.mavenCentral().name).shouldNotBeNull()
            getRepository(project, project.repositories.jcenter().name).shouldNotBeNull()
            getRepository(project, project.repositories.gradlePluginPortal().name).shouldNotBeNull()
            getRepository(project, project.repositories.mavenLocal().name).shouldNotBeNull()
        }

        "Add Dependencies" {
            getDependency(project, "implementation", Dependencies.KOTLIN_STD_LIB).shouldNotBeNull()
            getDependency(project, "implementation", Dependencies.KOTLIN_REFLECT).shouldNotBeNull()
            getDependency(project, "implementation", Dependencies.SPRING_BOOT_STARTER).shouldNotBeNull()
            getDependency(project, "annotationProcessor", Dependencies.SPRING_BOOT_CONFIG_PROCESSOR).shouldNotBeNull()
            getDependency(project, "testImplementation", Dependencies.SPRING_BOOT_STARTER_TEST).shouldNotBeNull()
            getDependency(project, "testImplementation", Dependencies.KO_TEST).shouldNotBeNull()
            getDependency(project, "testImplementation", Dependencies.MOCK_K).shouldNotBeNull()
            getDependency(project, "testImplementation", Dependencies.JUNIT_JUPITER).shouldNotBeNull()
            getDependency(project, "testImplementation", Dependencies.ASSERT_J).shouldNotBeNull()
        }

        "Configure Kotlin Compile Task" {
            val task = getTask<KotlinCompile>(project, "compileKotlin")

            with(task) {
                task.shouldNotBeNull()
                kotlinOptions.freeCompilerArgs shouldBe KotlinOptions.FREE_COMPILER_ARGS
                kotlinOptions.jvmTarget shouldBe Versions.JAVA
            }
        }

        "Configure Java Compile Task" {
            val task = getTask<JavaCompile>(project, "compileJava")

            with(task) {
                task.shouldNotBeNull()
                sourceCompatibility shouldBe Versions.JAVA
                targetCompatibility shouldBe Versions.JAVA
            }
        }

        "Configure Dependency Updates Task" {
            val task = getTask<DependencyUpdatesTask>(project, "dependencyUpdates")

            task.shouldNotBeNull()
        }

        "Configure Test Task" {
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

        "Configure Jar Task" {
            val task = getTask<Jar>(project, "jar")

            with(task) {
                task.shouldNotBeNull()
                enabled.shouldBeTrue()
            }
        }

        "Configure Boot Jar Task" {
            val task = getTask<BootJar>(project, "bootJar")

            with(task) {
                task.shouldNotBeNull()
                enabled.shouldBeFalse()
            }
        }

        "Configure Upload Archives Task" {
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
                            name.shouldBeEmpty()
                            url.shouldBeEmpty()
                        }
                        with(developers.first()) {
                            id shouldBe "dev"
                            name.shouldBeEmpty()
                            email.shouldBeEmpty()
                        }
                    }
                }
            }
        }

        "Configure Artifacts" {
            val sourcesJar = getArtifact<ArchivePublishArtifact>(project, "archives", "sources", "jar", "jar")
            with(sourcesJar) {
                sourcesJar.shouldNotBeNull()
                archiveTask.name shouldBe "sourcesJar"
            }

            val javadocJar = getArtifact<ArchivePublishArtifact>(project, "archives", "javadoc", "jar", "jar")
            with(javadocJar) {
                javadocJar.shouldNotBeNull()
                archiveTask.name shouldBe "javadocJar"
            }

            val jar = getArtifact<ArchivePublishArtifact>(project, "archives", "", "jar", "jar")
            with(jar) {
                jar.shouldNotBeNull()
                archiveTask.name shouldBe "jar"
            }
        }

        "Configure Publishing Extension" {
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

        "Configure Signing Extension" {
            val extension = project.getExtension<SigningExtension>()

            extension.shouldNotBeNull()

            val sourcesJar = getArtifact<Signature>(project, "archives", "sources", "jar.asc", "asc")
            with(sourcesJar) {
                sourcesJar.shouldNotBeNull()
                signatureSpec.toString().shouldContain("signArchives")
            }

            val javadocJar = getArtifact<Signature>(project, "archives", "javadoc", "jar.asc", "asc")
            with(javadocJar) {
                javadocJar.shouldNotBeNull()
                signatureSpec.toString().shouldContain("signArchives")
            }

            val jar = getArtifact<Signature>(project, "archives", "", "jar.asc", "asc")
            with(jar) {
                jar.shouldNotBeNull()
                signatureSpec.toString().shouldContain("signArchives")
            }
        }
    }
})
