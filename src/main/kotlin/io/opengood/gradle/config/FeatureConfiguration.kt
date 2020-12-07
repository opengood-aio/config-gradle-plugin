package io.opengood.gradle.config

import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

open class FeatureConfiguration(
    project: Project
) {
    var spring by GradleProperty(project, Boolean::class.java, true)
    var lombok by GradleProperty(project, Boolean::class.java, true)
    var junit by GradleProperty(project, Boolean::class.java, true)
    var assertj by GradleProperty(project, Boolean::class.java, true)
    var mockito by GradleProperty(project, Boolean::class.java, true)
    var kotest by GradleProperty(project, Boolean::class.java, true)
    var mockk by GradleProperty(project, Boolean::class.java, true)
}
