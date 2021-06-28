package helper

import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType

data class ProjectConfig(
    val languageType: LanguageType,
    val name: String = "test",
    val group: String = "org.example",
    val version: String = "1.0.0-SNAPSHOT",
    val projectType: ProjectType = ProjectType.APP,
    val srcDir: Boolean = true,
    val buildGradle: Boolean = true,
    val settingsGradle: Boolean = true,
    val features: Int = defaultFeatures,
    val testFrameworks: Int = defaultTestFrameworks
)
