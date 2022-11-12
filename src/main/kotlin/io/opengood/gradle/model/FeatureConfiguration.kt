package io.opengood.gradle.model

import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

@AllOpen
class FeatureConfiguration(project: Project) {
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
