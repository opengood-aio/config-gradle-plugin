package io.opengood.gradle.model

import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

@AllOpen
class TestFrameworkConfiguration(project: Project) {
    var java by GradleProperty(project, Boolean::class.java, false)
}
