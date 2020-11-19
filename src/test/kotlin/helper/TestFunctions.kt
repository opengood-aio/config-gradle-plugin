package helper

import io.opengood.gradle.ConfigPlugin
import io.opengood.gradle.constant.Directories
import io.opengood.gradle.constant.Tests
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.extension.OpenGoodExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.testfixtures.ProjectBuilder
import java.nio.file.Files
import java.nio.file.Path

fun createProject(
    languageType: LanguageType,
    testMaxParallelForks: Int = Tests.MAX_PARALLEL_FORKS
): Project {
    return ProjectBuilder.builder()
        .withProjectDir(createProjectDir(languageType).toFile())
        .build().also { project ->
            project.pluginManager.apply(ConfigPlugin.PLUGIN_ID)
            project.extensions.configure(OpenGoodExtension::class.java) { ext ->
                ext.testMaxParallelForks = testMaxParallelForks
            }
            (project as ProjectInternal).evaluate()
        }
}

fun createProjectDir(languageType: LanguageType): Path {
    val projectDir = Files.createTempDirectory("")
    when (languageType) {
        LanguageType.GROOVY -> projectDir.resolve(Directories.GROOVY_SRC).toFile().mkdirs()
        LanguageType.JAVA -> projectDir.resolve(Directories.JAVA_SRC).toFile().mkdirs()
        LanguageType.KOTLIN -> projectDir.resolve(Directories.KOTLIN_SRC).toFile().mkdirs()
    }
    return projectDir
}

fun getDependency(project: Project, configuration: String, name: String): Dependency {
    return project.configurations.getByName(configuration).dependencies
        .takeIf { it.contains(project.dependencies.create(name)) }!!.first()
}

inline fun <reified T : Any> getExtension(project: Project): T {
    return project.extensions.getByType(T::class.java)
}

inline fun <reified T : Plugin<*>> getPlugin(project: Project): T {
    return project.plugins.getPlugin(T::class.java)
}

fun getPlugin(project: Project, id: String): Plugin<Any> {
    return project.plugins.getPlugin(id)
}

fun getRepository(project: Project, name: String): ArtifactRepository {
    return project.repositories.getByName(name)
}

inline fun <reified T : Task> getTask(project: Project, name: String): T {
    return project.tasks.withType(T::class.java).getByName(name)
}