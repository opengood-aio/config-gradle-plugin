package io.opengood.gradle.config

import io.opengood.gradle.constant.Artifacts
import io.opengood.gradle.constant.GitHub
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

open class LicenseConfiguration(
    project: Project
) {
    var name by GradleProperty(project, String::class.java, Artifacts.LICENSE_NAME)
    var uri by GradleProperty(
        project,
        String::class.java,
        "${GitHub.OPENGOOD_ORG_URI}/${project.name}/${GitHub.RESOURCE_ENDPOINT}/${GitHub.BRANCH}/${Artifacts.LICENSE_RESOURCE}"
    )
}
