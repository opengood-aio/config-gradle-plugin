package io.opengood.gradle.config

import io.opengood.gradle.constant.Artifacts
import io.opengood.gradle.constant.GitHub
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

class LicenseConfiguration(
    project: Project,
    repo: RepoConfiguration,
    val id: String
) {
    init {
        if (id.isBlank()) {
            throw IllegalArgumentException("License configuration identifier must not be blank")
        }
    }

    var name by GradleProperty(project, String::class.java, Artifacts.LICENSE_NAME)
    var uri by GradleProperty(project, String::class.java, "${repo.baseUri}/${repo.name}/${GitHub.RESOURCE_ENDPOINT}/${GitHub.BRANCH}/${Artifacts.LICENSE_RESOURCE}")
}
