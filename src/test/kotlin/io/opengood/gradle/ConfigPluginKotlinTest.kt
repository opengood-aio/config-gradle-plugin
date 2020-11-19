package io.opengood.gradle

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import helper.*
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.WordSpec
import io.opengood.gradle.constant.*
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.extension.opengood
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

class ConfigPluginKotlinTest : WordSpec({

    "Configuring the Plugin ID" should {
        val project = createProject(LanguageType.KOTLIN)

        "Apply the Plugin" {
            getPlugin(project, ConfigPlugin.PLUGIN_ID) shouldNotBe null
            getPlugin(project, ConfigPlugin.PLUGIN_ID).shouldBeTypeOf<ConfigPlugin>()
            getPlugin<ConfigPlugin>(project).shouldBeTypeOf<ConfigPlugin>()
        }

        "Create Plugin Extension" {
            project.opengood().languageType shouldBe LanguageType.KOTLIN
            project.opengood().testMaxParallelForks shouldBe Tests.MAX_PARALLEL_FORKS
        }

        "Apply Shared Plugins" {
            getPlugin(project, Plugins.IDEA) shouldNotBe null
            getPlugin(project, Plugins.KOTLIN) shouldNotBe null
            getPlugin(project, Plugins.KOTLIN_SPRING) shouldNotBe null
            getPlugin(project, Plugins.MAVEN) shouldNotBe null
            getPlugin(project, Plugins.MAVEN_PUBLISH) shouldNotBe null
            getPlugin(project, Plugins.SPRING_BOOT) shouldNotBe null
            getPlugin(project, Plugins.SPRING_DEPENDENCY_MANAGEMENT) shouldNotBe null
            getPlugin(project, Plugins.VERSIONS) shouldNotBe null
        }

        "Add Shared Repositories" {
            getRepository(project, project.repositories.mavenCentral().name) shouldNotBe null
            getRepository(project, project.repositories.jcenter().name) shouldNotBe null
            getRepository(project, project.repositories.mavenLocal().name) shouldNotBe null
        }

        "Add Shared Dependencies" {
            getDependency(project, "implementation", Dependencies.KOTLIN_JDK_STD_LIB) shouldNotBe null
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

            task shouldNotBe null
            task.kotlinOptions.freeCompilerArgs shouldBe KotlinOptions.FREE_COMPILER_ARGS
            task.kotlinOptions.jvmTarget shouldBe Versions.JAVA
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

            val mavenLocalRepo = extension.repositories.getByName(Repositories.LOCAL_REPO_NAME) as MavenArtifactRepository
            mavenLocalRepo.name shouldBe Repositories.LOCAL_REPO_NAME
            mavenLocalRepo.url shouldBe project.repositories.mavenLocal().url
        }
    }
})
