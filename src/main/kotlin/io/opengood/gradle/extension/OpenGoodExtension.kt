package io.opengood.gradle.extension

import io.opengood.gradle.config.ArtifactConfiguration
import io.opengood.gradle.config.MainConfiguration
import io.opengood.gradle.config.RepoConfiguration
import io.opengood.gradle.config.TestConfiguration
import io.opengood.gradle.extension.OpenGoodExtension.Companion.EXTENSION_NAME
import org.gradle.api.Project

open class OpenGoodExtension(
    project: Project
) {
    val main = MainConfiguration(project)
    val repo = RepoConfiguration(project)
    val test = TestConfiguration(project)
    val artifact = ArtifactConfiguration(project, RepoConfiguration(project))

    companion object {
        const val EXTENSION_NAME = "opengood"
    }
}

fun Project.opengood(): OpenGoodExtension =
    extensions.getByName(EXTENSION_NAME) as? OpenGoodExtension
        ?: throw IllegalStateException("$EXTENSION_NAME is not of the correct type")