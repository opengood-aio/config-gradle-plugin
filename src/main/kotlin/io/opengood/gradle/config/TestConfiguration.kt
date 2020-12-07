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
    var multipleFrameworks = MultipleFrameworkConfiguration(project)

    open fun multipleFrameworks(action: Action<in MultipleFrameworkConfiguration>) =
        action.execute(multipleFrameworks)

    open fun multipleFrameworks(closure: Closure<Unit>) =
        ConfigureUtil.configure(closure, multipleFrameworks)
}
