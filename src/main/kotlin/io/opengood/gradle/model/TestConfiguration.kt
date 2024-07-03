package io.opengood.gradle.model

import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.constant.Tests
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

@AllOpen
class TestConfiguration(
    private val project: Project,
) {
    var maxParallelForks by GradleProperty(project, Int::class.java, Tests.MAX_PARALLEL_FORKS)
}
