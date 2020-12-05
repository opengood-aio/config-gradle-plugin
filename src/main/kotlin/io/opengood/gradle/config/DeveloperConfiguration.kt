package io.opengood.gradle.config

import io.opengood.gradle.constant.Artifacts
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

open class DeveloperConfiguration(
    project: Project
) {
    var id by GradleProperty(project, String::class.java, Artifacts.DEVELOPER_ID)
    var name by GradleProperty(project, String::class.java, Artifacts.DEVELOPER_NAME)
    var email by GradleProperty(project, String::class.java, Artifacts.DEVELOPER_EMAIL)
}
