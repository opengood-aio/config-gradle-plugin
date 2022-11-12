package test.model

import io.opengood.gradle.constant.EnvVars
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import io.opengood.gradle.enumeration.PublicationType
import test.enumeration.defaultFeatures
import test.enumeration.defaultTestFrameworks

data class ProjectConfig(
    val languageType: LanguageType,
    val name: String = "test",
    val group: String = "org.example",
    val version: String = "1.0.0-SNAPSHOT",
    val projectType: ProjectType = ProjectType.APP,
    val publications: List<PublicationType> = listOf(PublicationType.GITHUB, PublicationType.OSS),
    val srcDir: Boolean = true,
    val buildGradle: Boolean = true,
    val settingsGradle: Boolean = true,
    val features: Int = defaultFeatures,
    val testFrameworks: Int = defaultTestFrameworks,
    val credentials: Map<String, String> = mapOf(
        EnvVars.GITHUB_USER to "example",
        EnvVars.GITHUB_TOKEN to "fake",
        EnvVars.OSS_REPO_USERNAME to "example",
        EnvVars.OSS_REPO_PASSWORD to "fake"
    )
)
