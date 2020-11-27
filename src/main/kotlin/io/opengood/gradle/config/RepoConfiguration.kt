package io.opengood.gradle.config

import io.opengood.gradle.constant.GitHub
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

class RepoConfiguration(project: Project) {
    var name by GradleProperty(project, String::class.java, project.name)
    var baseUri by GradleProperty(project, String::class.java, GitHub.OPENGOOD_ORG_REPO_URI)
}
