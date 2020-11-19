package com.gaig.cnd.gradle

import com.gaig.cnd.gradle.constant.*
import com.gaig.cnd.gradle.extension.cnd
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import helper.*
import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.WordSpec
import net.researchgate.release.GitAdapter
import net.researchgate.release.ReleaseExtension
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import java.net.URI

class ConfigPluginGroovyTest : WordSpec({

    "Configuring the Plugin ID" should {
        val project = createProject(LanguageType.GROOVY)

        "Apply the Plugin" {
            getPlugin(project, ConfigPlugin.PLUGIN_ID) shouldNotBe null
            getPlugin(project, ConfigPlugin.PLUGIN_ID).shouldBeTypeOf<ConfigPlugin>()
            getPlugin<ConfigPlugin>(project).shouldBeTypeOf<ConfigPlugin>()
        }

        "Create CND Plugin Extension" {
            project.cnd().gaigRepo() shouldNotBe null
            project.cnd().testMaxParallelForks shouldBe Tests.MAX_PARALLEL_FORKS
        }

        "Apply Shared Plugins" {
            getPlugin(project, Plugins.GROOVY) shouldNotBe null
            getPlugin(project, Plugins.IDEA) shouldNotBe null
            getPlugin(project, Plugins.MAVEN) shouldNotBe null
            getPlugin(project, Plugins.MAVEN_PUBLISH) shouldNotBe null
            getPlugin(project, Plugins.RELEASE) shouldNotBe null
            getPlugin(project, Plugins.SPRING_BOOT) shouldNotBe null
            getPlugin(project, Plugins.SPRING_DEPENDENCY_MANAGEMENT) shouldNotBe null
            getPlugin(project, Plugins.VERSIONS) shouldNotBe null
        }

        "Add Shared Repositories" {
            getRepository(project, project.cnd().gaigRepo().name) shouldNotBe null
        }

        "Add Shared Dependencies" {
            getDependency(project, "implementation", Dependencies.GROOVY) shouldNotBe null
            getDependency(project, "implementation", Dependencies.SPRING_BOOT_CONFIG_PROCESSOR) shouldNotBe null
            getDependency(project, "implementation", Dependencies.SPRING_BOOT_STARTER) shouldNotBe null
            getDependency(project, "testImplementation", Dependencies.SPRING_BOOT_STARTER_TEST) shouldNotBe null
        }

        "Configure Java Compile Task" {
            val task = getTask<JavaCompile>(project, "compileJava")

            task shouldNotBe null
            task.sourceCompatibility shouldBe Versions.JAVA
            task.targetCompatibility shouldBe Versions.JAVA
        }

        "Configure Dependency Updates Task" {
            val task = getTask<DependencyUpdatesTask>(project, "dependencyUpdates")

            task shouldNotBe null
        }

        "Configure Test Task" {
            val task = getTask<Test>(project, "test")

            task shouldNotBe null
            task.testLogging.events shouldBe Tests.LOGGING_EVENTS
            task.testLogging.exceptionFormat shouldBe Tests.EXCEPTION_FORMAT
            task.testLogging.showCauses shouldBe Tests.SHOW_CAUSES
            task.testLogging.showExceptions shouldBe Tests.SHOW_EXCEPTIONS
            task.testLogging.showStackTraces shouldBe Tests.SHOW_STACK_TRACES
            task.maxParallelForks shouldBe Tests.MAX_PARALLEL_FORKS
            task.systemProperties[Tests.INSTANCE_LIFECYCLE_SYS_PROP_NAME] shouldBe Tests.INSTANCE_LIFECYCLE_SYS_PROP_VALUE
        }

        "Configure Publishing Extension" {
            val extension = getExtension<PublishingExtension>(project)

            val gaigReleasesRepo = extension.repositories.getByName(Repositories.GAIG_RELEASES_REPO_NAME) as MavenArtifactRepository
            gaigReleasesRepo.name shouldBe Repositories.GAIG_RELEASES_REPO_NAME
            gaigReleasesRepo.url shouldBe URI(Repositories.GAIG_RELEASES_REPO_URI)

            val mavenLocalRepo = extension.repositories.getByName(Repositories.LOCAL_REPO_NAME) as MavenArtifactRepository
            mavenLocalRepo.name shouldBe Repositories.LOCAL_REPO_NAME
            mavenLocalRepo.url shouldBe project.repositories.mavenLocal().url
        }

        "Configure Release Extension" {
            val extension = getExtension<ReleaseExtension>(project)

            extension.scmAdapters shouldContain GitAdapter::class.java
        }
    }
})
