package io.opengood.gradle.config

import io.opengood.gradle.constant.Tests
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

open class TestConfiguration(
    project: Project
) {
    var maxParallelForks by GradleProperty(project, Int::class.java, Tests.MAX_PARALLEL_FORKS)
}
