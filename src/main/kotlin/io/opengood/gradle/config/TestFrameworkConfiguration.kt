package io.opengood.gradle.config

import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

open class TestFrameworkConfiguration(
    project: Project
) {
    var java by GradleProperty(project, Boolean::class.java, false)
}
