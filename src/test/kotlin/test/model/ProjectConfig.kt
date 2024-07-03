package test.model

import io.opengood.gradle.constant.EnvVars
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import io.opengood.gradle.enumeration.PublicationType
import test.enumeration.defaultFeatures

data class ProjectConfig(
    val languageType: LanguageType,
    val name: String = "test",
    val group: String = "io.test",
    val version: String = "1.0.0",
    val srcDir: Boolean = true,
    val buildGradle: Boolean = true,
    val settingsGradle: Boolean = true,
    val projectType: ProjectType = ProjectType.APP,
    val features: Int = defaultFeatures,
    val publications: List<PublicationType> = listOf(PublicationType.GITHUB, PublicationType.MAVEN_CENTRAL_PORTAL),
    val credentials: Map<String, String> =
        mapOf(
            EnvVars.GITHUB_USER to "example",
            EnvVars.GITHUB_TOKEN to "fake",
        ),
)
