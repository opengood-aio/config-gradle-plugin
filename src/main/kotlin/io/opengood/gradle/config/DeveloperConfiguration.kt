package io.opengood.gradle.config

import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

class DeveloperConfiguration(
    project: Project,
    val id: String
) {
    init {
        if (id.isBlank()) {
            throw IllegalArgumentException("Developer configuration identifier must not be blank")
        }
    }

    var name by GradleProperty(project, String::class.java, "")
    var email by GradleProperty(project, String::class.java, "")
}
