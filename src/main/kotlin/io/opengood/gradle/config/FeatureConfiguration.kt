package io.opengood.gradle.config

import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

open class FeatureConfiguration(
    project: Project
) {
    var publishing by GradleProperty(project, Boolean::class.java, true)
    var assertj by GradleProperty(project, Boolean::class.java, true)
    var jacksonKotlin by GradleProperty(project, Boolean::class.java, true)
    var junitJupiter by GradleProperty(project, Boolean::class.java, true)
    var kotest by GradleProperty(project, Boolean::class.java, true)
    var kotestSpring by GradleProperty(project, Boolean::class.java, true)
    var kotlinCoroutines by GradleProperty(project, Boolean::class.java, true)
    var lombok by GradleProperty(project, Boolean::class.java, true)
    var mockito by GradleProperty(project, Boolean::class.java, true)
    var mockk by GradleProperty(project, Boolean::class.java, true)
    var spring by GradleProperty(project, Boolean::class.java, true)
    var springMockk by GradleProperty(project, Boolean::class.java, true)
}
