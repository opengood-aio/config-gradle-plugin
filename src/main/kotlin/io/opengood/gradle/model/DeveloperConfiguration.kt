package io.opengood.gradle.model

import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.constant.Developers
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

@AllOpen
class DeveloperConfiguration(project: Project) {
    var id by GradleProperty(project, String::class.java, Developers.ID)
    var name by GradleProperty(project, String::class.java, Developers.NAME)
    var email by GradleProperty(project, String::class.java, Developers.EMAIL)
}
