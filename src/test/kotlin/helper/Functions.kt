package helper

import io.opengood.gradle.ConfigPlugin
import io.opengood.gradle.constant.Directories
import io.opengood.gradle.enumeration.BuildGradleType
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.extension.OpenGoodExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.internal.tasks.DefaultTaskDependency
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.testfixtures.ProjectBuilder
import java.nio.file.Files
import java.nio.file.Path

internal fun createProject(config: ProjectConfig): Project {
    return with(config) {
        val projectDir = createProjectDir()
        ProjectBuilder.builder()
            .withName(name)
            .withProjectDir(projectDir.toFile())
            .build().also { project ->
                project.group = group
                project.version = version
                with(project) {
                    srcDir then { createProjectSrcDir(languageType, projectDir) }
                    buildGradle then { createProjectBuildGradle(languageType, projectDir) }

                    pluginManager.apply(ConfigPlugin.PLUGIN_ID)
                    extensions.configure(OpenGoodExtension::class.java) { ext ->
                        ext.main.projectType = projectType
                        ext.features = getFeatures(project, features)
                        ext.test.testFrameworks = getTestFrameworks(project, testFrameworks)
                    }
                }
                (project as ProjectInternal).evaluate()
            }
    }
}

internal fun createProjectBuildGradle(languageType: LanguageType, projectDir: Path) =
    projectDir.resolve(getBuildGradleFile(languageType)).toFile().createNewFile()

internal fun createProjectDir(): Path =
    Files.createTempDirectory("")

internal fun createProjectSrcDir(languageType: LanguageType, projectDir: Path) =
    when (languageType) {
        LanguageType.GROOVY -> projectDir.resolve(Directories.GROOVY_SRC).toFile().mkdirs()
        LanguageType.JAVA -> projectDir.resolve(Directories.JAVA_SRC).toFile().mkdirs()
        LanguageType.KOTLIN -> projectDir.resolve(Directories.KOTLIN_SRC).toFile().mkdirs()
    }

internal fun getBuildGradleFile(languageType: LanguageType): String =
    when (languageType) {
        LanguageType.KOTLIN -> BuildGradleType.KOTLIN.toString()
        else -> BuildGradleType.GROOVY.toString()
    }

internal inline fun <reified T : Any> getConvention(project: Project): T =
    project.convention.getPlugin(T::class.java)

internal fun getDependency(project: Project, configuration: String, name: String): Dependency? =
    project.configurations.getByName(configuration).dependencies
        .firstOrNull { it == project.dependencies.create(name) }

internal fun getMavenPublication(extension: PublishingExtension, name: String): MavenPublication =
    extension.publications.getByName(name) as MavenPublication

internal fun getMavenRepository(extension: PublishingExtension, name: String): MavenArtifactRepository =
    extension.repositories.getByName(name) as MavenArtifactRepository

internal inline fun <reified T : Plugin<*>> getPlugin(project: Project): T =
    project.plugins.getPlugin(T::class.java)

internal fun getPlugin(project: Project, id: String): Plugin<Any> =
    project.plugins.getPlugin(id)

internal fun getRepository(project: Project, name: String): ArtifactRepository =
    project.repositories.getByName(name)

internal fun getTaskByName(project: Project, name: String): Task =
    project.tasks.getByName(name)

internal inline fun <reified T : Task> getTaskByType(project: Project): T =
    project.tasks.withType(T::class.java).first()

internal inline fun <reified T : Task> getTaskByTypeAndName(project: Project, name: String): T =
    project.tasks.withType(T::class.java).getByName(name)

internal fun hasTaskFinalizedByDependency(task: Task, name: String): Boolean =
    (task.finalizedBy as DefaultTaskDependency).mutableValues.contains(name)

infix fun <T> Boolean.then(param: () -> T): T? =
    if (this) param() else null
