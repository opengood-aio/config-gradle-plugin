package io.opengood.gradle.config

import groovy.lang.Closure
import io.opengood.gradle.constant.Tests
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.util.ConfigureUtil

open class TestConfiguration(
    project: Project
) {
    var maxParallelForks by GradleProperty(project, Int::class.java, Tests.MAX_PARALLEL_FORKS)
    var testFrameworks = TestFrameworkConfiguration(project)

    open fun testFrameworks(action: Action<in TestFrameworkConfiguration>) =
        action.execute(testFrameworks)

    open fun testFrameworks(closure: Closure<Unit>) =
        ConfigureUtil.configure(closure, testFrameworks)
}
