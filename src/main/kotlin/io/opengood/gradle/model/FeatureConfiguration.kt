package io.opengood.gradle.model

import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.constant.Features
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

@AllOpen
class FeatureConfiguration(project: Project) {
    var assertj by GradleProperty(project, Boolean::class.java, Features.ASSERTJ)
    var jackson by GradleProperty(project, Boolean::class.java, Features.JACKSON)
    var jacksonKotlin by GradleProperty(project, Boolean::class.java, Features.JACKSON_KOTLIN)
    var junitJupiter by GradleProperty(project, Boolean::class.java, Features.JUNIT_JUPITER)
    var kotest by GradleProperty(project, Boolean::class.java, Features.KOTEST)
    var kotestSpring by GradleProperty(project, Boolean::class.java, Features.KOTEST_SPRING)
    var kotlinCoroutines by GradleProperty(project, Boolean::class.java, Features.KOTLIN_COROUTINES)
    var lombok by GradleProperty(project, Boolean::class.java, Features.LOMBOK)
    var mockito by GradleProperty(project, Boolean::class.java, Features.MOCKITO)
    var mockk by GradleProperty(project, Boolean::class.java, Features.MOCKK)
    var spring by GradleProperty(project, Boolean::class.java, Features.SPRING)
    var springMockk by GradleProperty(project, Boolean::class.java, Features.SPRING_MOCKK)
}
