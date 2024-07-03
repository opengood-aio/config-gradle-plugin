package io.opengood.gradle.model

import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.determineFeatureEnabled
import io.opengood.gradle.enumeration.Feature
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

@AllOpen
class FeatureConfiguration(
    project: Project,
) {
    var assertj by GradleProperty(project, Boolean::class.java, project.determineFeatureEnabled(Feature.ASSERTJ))
    var jackson by GradleProperty(project, Boolean::class.java, project.determineFeatureEnabled(Feature.JACKSON))
    var jacksonKotlin by GradleProperty(project, Boolean::class.java, project.determineFeatureEnabled(Feature.JACKSON_KOTLIN))
    var junitJupiter by GradleProperty(project, Boolean::class.java, project.determineFeatureEnabled(Feature.JUNIT_JUPITER))
    var kotest by GradleProperty(project, Boolean::class.java, project.determineFeatureEnabled(Feature.KOTEST))
    var kotestSpring by GradleProperty(project, Boolean::class.java, project.determineFeatureEnabled(Feature.KOTEST_SPRING))
    var kotlinCoroutines by GradleProperty(project, Boolean::class.java, project.determineFeatureEnabled(Feature.KOTLIN_COROUTINES))
    var lombok by GradleProperty(project, Boolean::class.java, project.determineFeatureEnabled(Feature.LOMBOK))
    var mockito by GradleProperty(project, Boolean::class.java, project.determineFeatureEnabled(Feature.MOCKITO))
    var mockk by GradleProperty(project, Boolean::class.java, project.determineFeatureEnabled(Feature.MOCKK))
    var spring by GradleProperty(project, Boolean::class.java, project.determineFeatureEnabled(Feature.SPRING))
    var springMockk by GradleProperty(project, Boolean::class.java, project.determineFeatureEnabled(Feature.SPRING_MOCKK))
}
