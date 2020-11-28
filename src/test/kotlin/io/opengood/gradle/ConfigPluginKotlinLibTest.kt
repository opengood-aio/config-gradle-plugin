package io.opengood.gradle

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import helper.*
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.WordSpec
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

        "Apply Plugin" {
            getPlugin(project, ConfigPlugin.PLUGIN_ID) shouldNotBe null
            getPlugin(project, ConfigPlugin.PLUGIN_ID).shouldBeTypeOf<ConfigPlugin>()
            getPlugin<ConfigPlugin>(project).shouldBeTypeOf<ConfigPlugin>()
        }

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
                    description shouldBe ""
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
                                id shouldNotBe null
                                name shouldBe ""
                                uri shouldBe ""
                            }
                        }
                    }
                    with(developers) {
                        if (isNotEmpty()) {
                            with(first()) {
                                id shouldNotBe null
                                name shouldBe ""
                                email shouldBe ""
                            }
                        }
                    }
                }
            }
        }

        "Apply Plugins" {
            getPlugin(project, Plugins.BASE) shouldNotBe null
            getPlugin(project, Plugins.IDEA) shouldNotBe null
            getPlugin(project, Plugins.JAVA_LIBRARY) shouldNotBe null
            getPlugin(project, Plugins.KOTLIN) shouldNotBe null
            getPlugin(project, Plugins.KOTLIN_SPRING) shouldNotBe null
            getPlugin(project, Plugins.MAVEN) shouldNotBe null
            getPlugin(project, Plugins.MAVEN_PUBLISH) shouldNotBe null
            getPlugin(project, Plugins.SIGNING) shouldNotBe null
            getPlugin(project, Plugins.SPRING_BOOT) shouldNotBe null
            getPlugin(project, Plugins.SPRING_DEPENDENCY_MANAGEMENT) shouldNotBe null
            getPlugin(project, Plugins.VERSIONS) shouldNotBe null

            val basePluginConvention = getConvention<BasePluginConvention>(project)
            basePluginConvention.archivesBaseName shouldBe project.name
        }

        "Add Repositories" {
            getRepository(project, project.repositories.mavenCentral().name) shouldNotBe null
            getRepository(project, project.repositories.jcenter().name) shouldNotBe null
            getRepository(project, project.repositories.gradlePluginPortal().name) shouldNotBe null
            getRepository(project, project.repositories.mavenLocal().name) shouldNotBe null
        }

        "Add Dependencies" {
            getDependency(project, "implementation", Dependencies.KOTLIN_STD_LIB) shouldNotBe null
            getDependency(project, "implementation", Dependencies.KOTLIN_REFLECT) shouldNotBe null
            getDependency(project, "implementation", Dependencies.SPRING_BOOT_STARTER) shouldNotBe null
            getDependency(project, "annotationProcessor", Dependencies.SPRING_BOOT_CONFIG_PROCESSOR) shouldNotBe null
            getDependency(project, "testImplementation", Dependencies.SPRING_BOOT_STARTER_TEST) shouldNotBe null
            getDependency(project, "testImplementation", Dependencies.KOTLIN_TEST) shouldNotBe null
            getDependency(project, "testImplementation", Dependencies.MOCK_K) shouldNotBe null
            getDependency(project, "testImplementation", Dependencies.JUNIT_JUPITER) shouldNotBe null
            getDependency(project, "testImplementation", Dependencies.ASSERT_J) shouldNotBe null
        }

        "Configure Kotlin Compile Task" {
            val task = getTask<KotlinCompile>(project, "compileKotlin")

            with(task) {
                task shouldNotBe null
                kotlinOptions.freeCompilerArgs shouldBe KotlinOptions.FREE_COMPILER_ARGS
                kotlinOptions.jvmTarget shouldBe Versions.JAVA
            }
        }

        "Configure Java Compile Task" {
            val task = getTask<JavaCompile>(project, "compileJava")

            with(task) {
                task shouldNotBe null
                sourceCompatibility shouldBe Versions.JAVA
                targetCompatibility shouldBe Versions.JAVA
            }
        }

        "Configure Dependency Updates Task" {
            val task = getTask<DependencyUpdatesTask>(project, "dependencyUpdates")

            task shouldNotBe null
        }

        "Configure Test Task" {
            val task = getTask<Test>(project, "test")

            with(task) {
                task shouldNotBe null
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
                task shouldNotBe null
                enabled shouldBe true
            }
        }

        "Configure Boot Jar Task" {
            val task = getTask<BootJar>(project, "bootJar")

            with(task) {
                task shouldNotBe null
                enabled shouldBe false
            }
        }

        "Configure Upload Archives Task" {
            val task = getTask<Upload>(project)

            with(task) {
                task shouldNotBe null

                with(getMavenDeployer(repositories)) {
                    with(snapshotRepository) {
                        url shouldBe Repositories.OSS_SNAPSHOTS_REPO_URI
                        with(authentication) {
                            userName shouldBe ""
                            password shouldBe ""
                        }
                    }
                    with(repository) {
                        url shouldBe Repositories.OSS_STAGING_REPO_URI
                        with(authentication) {
                            userName shouldBe ""
                            password shouldBe ""
                        }
                    }

                    val pom = pom.effectivePom.model as Model
                    with(pom) {
                        groupId shouldBe project.group
                        artifactId shouldBe project.name
                        version shouldBe project.version
                        name shouldBe project.name
                        packaging shouldBe PackagingType.JAR.toString()
                        description shouldBe ""
                        url shouldBe "${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                        with(scm) {
                            connection shouldBe "${ScmProvider.PROTOCOL}:${ScmProvider.GIT}:${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                            developerConnection shouldBe "${ScmProvider.PROTOCOL}:${ScmProvider.GIT}:${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                            url shouldBe "${GitHub.OPENGOOD_ORG_URI}/${project.name}"
                        }
                        with(licenses.first()) {
                            name shouldBe ""
                            url shouldBe ""
                        }
                        with(developers.first()) {
                            id shouldBe "dev"
                            name shouldBe ""
                            email shouldBe ""
                        }
                    }
                }
            }
        }

        "Configure Artifacts" {
            val sourcesJar = getArtifact<ArchivePublishArtifact>(project, "archives", "sources", "jar", "jar")
            with(sourcesJar) {
                sourcesJar shouldNotBe null
                archiveTask.name shouldBe "sourcesJar"
            }

            val javadocJar = getArtifact<ArchivePublishArtifact>(project, "archives", "javadoc", "jar", "jar")
            with(javadocJar) {
                javadocJar shouldNotBe null
                archiveTask.name shouldBe "javadocJar"
            }

            val jar = getArtifact<ArchivePublishArtifact>(project, "archives", "", "jar", "jar")
            with(jar) {
                jar shouldNotBe null
                archiveTask.name shouldBe "jar"
            }
        }

        "Configure Publishing Extension" {
            val extension = project.getExtension<PublishingExtension>()

            extension shouldNotBe null

            val mavenJavaPublication = getMavenPublication(extension, "mavenJava")
            with(mavenJavaPublication) {
                artifacts shouldNotBe null
            }

            val mavenLocalRepo = getMavenRepository(extension, Repositories.LOCAL_REPO_NAME)
            with(mavenLocalRepo) {
                name shouldBe Repositories.LOCAL_REPO_NAME
                url shouldBe project.repositories.mavenLocal().url
            }
        }

        "Configure Signing Extension" {
            val extension = project.getExtension<SigningExtension>()

            extension shouldNotBe null

            val sourcesJar = getArtifact<Signature>(project, "archives", "sources", "jar.asc", "asc")
            with(sourcesJar) {
                sourcesJar shouldNotBe null
                signatureSpec.toString().shouldContain("signArchives")
            }

            val javadocJar = getArtifact<Signature>(project, "archives", "javadoc", "jar.asc", "asc")
            with(javadocJar) {
                javadocJar shouldNotBe null
                signatureSpec.toString().shouldContain("signArchives")
            }

            val jar = getArtifact<Signature>(project, "archives", "", "jar.asc", "asc")
            with(jar) {
                jar shouldNotBe null
                signatureSpec.toString().shouldContain("signArchives")
            }
        }
    }
})
