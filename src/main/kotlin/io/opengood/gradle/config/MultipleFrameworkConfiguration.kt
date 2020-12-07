package io.opengood.gradle.config

import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

open class MultipleFrameworkConfiguration(
    project: Project
) {
    var kotlin by GradleProperty(project, Boolean::class.java, false)
}
