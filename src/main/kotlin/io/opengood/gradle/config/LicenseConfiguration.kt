package io.opengood.gradle.config

import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

class LicenseConfiguration(
    project: Project,
    val id: String
) {
    init {
        if (id.isBlank()) {
            throw IllegalArgumentException("License configuration identifier must not be blank")
        }
    }

    var name by GradleProperty(project, String::class.java, "")
    var uri by GradleProperty(project, String::class.java, "")
}
