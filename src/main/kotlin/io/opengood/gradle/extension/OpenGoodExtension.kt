package io.opengood.gradle.extension

import io.opengood.gradle.constant.GitHub
import io.opengood.gradle.extension.OpenGoodExtension.Companion.EXTENSION_NAME
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import java.net.URI

open class OpenGoodExtension(private val project: Project) {

    var repoName by GradleProperty(project, String::class.java, "")

    fun repo(): MavenArtifactRepository {
        return project.repositories.maven {
            it.name = GitHub.ORG_NAME
            it.url = URI.create(GitHub.ORG_URI)
        }
    }

    companion object {
        const val EXTENSION_NAME = "opengood"
    }
}

fun Project.opengood(): OpenGoodExtension =
    extensions.getByName(EXTENSION_NAME) as? OpenGoodExtension
        ?: throw IllegalStateException("$EXTENSION_NAME is not of the correct type")