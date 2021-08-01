package io.opengood.gradle.config

import groovy.lang.Closure
import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.constant.Tests
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Action
import org.gradle.api.Project

@AllOpen
class TestConfiguration(private val project: Project) {
    var maxParallelForks by GradleProperty(project, Int::class.java, Tests.MAX_PARALLEL_FORKS)
    var frameworks = TestFrameworkConfiguration(project)

    fun frameworks(action: Action<in TestFrameworkConfiguration>) = action.execute(frameworks)

    fun frameworks(closure: Closure<Unit>) = project.configure(frameworks, closure)
}
