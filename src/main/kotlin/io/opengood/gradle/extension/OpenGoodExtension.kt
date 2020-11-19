package io.opengood.gradle.extension

import io.opengood.gradle.constant.Tests
import io.opengood.gradle.extension.OpenGoodExtension.Companion.EXTENSION_NAME
import io.opengood.gradle.getLanguageType
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

open class OpenGoodExtension(project: Project) {

    val languageType = getLanguageType(project)
    var testMaxParallelForks by GradleProperty(project, Int::class.java, Tests.MAX_PARALLEL_FORKS)

    companion object {
        const val EXTENSION_NAME = "opengood"
    }
}

fun Project.opengood(): OpenGoodExtension =
    extensions.getByName(EXTENSION_NAME) as? OpenGoodExtension
        ?: throw IllegalStateException("$EXTENSION_NAME is not of the correct type")