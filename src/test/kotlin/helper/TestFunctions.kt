package helper

import io.opengood.gradle.ConfigPlugin
import io.opengood.gradle.config.DeveloperConfiguration
import io.opengood.gradle.config.LicenseConfiguration
import io.opengood.gradle.constant.Directories
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
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

fun <T : Any> T.accessField(fieldName: String): Any? =
    javaClass.getDeclaredField(fieldName).let { field ->
        field.isAccessible = true
        return@let field.get(this)
    }

fun createProject(
    languageType: LanguageType,
    name: String = "test",
    group: String = "org.example",
    version: String = "1.0.0-SNAPSHOT",
    projectType: ProjectType = ProjectType.APP
): Project {
    return ProjectBuilder.builder()
        .withName(name)
        .withProjectDir(createProjectDir(languageType).toFile())
        .build().also { project ->
            project.group = group
            project.version = version
            with(project) {
                pluginManager.apply(ConfigPlugin.PLUGIN_ID)
                extensions.configure(OpenGoodExtension::class.java) { ext ->
                    ext.apply {
                        main.projectType = projectType
                        artifact.apply {
                            licenses.add(LicenseConfiguration(project, "license"))
                            developers.add(DeveloperConfiguration(project, "dev"))
                        }
                    }
                }
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

fun getDependency(project: Project, configuration: String, name: String): Dependency =
    project.configurations.getByName(configuration).dependencies
        .takeIf { project.dependencies.create(name) in it }!!.first()

inline fun <reified T : Plugin<*>> getPlugin(project: Project): T =
    project.plugins.getPlugin(T::class.java)

fun getPlugin(project: Project, id: String): Plugin<Any> =
    project.plugins.getPlugin(id)

fun getRepository(project: Project, name: String): ArtifactRepository =
    project.repositories.getByName(name)

inline fun <reified T : Task> getTask(project: Project, name: String): T =
    project.tasks.withType(T::class.java).getByName(name)

inline fun <reified T : Task> getTask(project: Project): T =
    project.tasks.withType(T::class.java).first()