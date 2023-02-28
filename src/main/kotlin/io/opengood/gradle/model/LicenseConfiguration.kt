package io.opengood.gradle.model

import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.constant.Licenses
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

@AllOpen
class LicenseConfiguration(project: Project) {
    var name by GradleProperty(project, String::class.java, Licenses.NAME)
    var uri by GradleProperty(project, String::class.java, String.format(Licenses.URI, project.name))
}
