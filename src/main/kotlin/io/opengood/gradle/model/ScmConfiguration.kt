package io.opengood.gradle.model

import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.constant.GitHub
import io.opengood.gradle.enumeration.ScmProvider
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

@AllOpen
class ScmConfiguration(project: Project) {
    var provider by GradleProperty(project, ScmProvider::class.java, ScmProvider.GIT)
    var connection by GradleProperty(project, String::class.java, String.format(ScmProvider.CONNECTION, provider, project.name))
    var devConnection by GradleProperty(project, String::class.java, String.format(ScmProvider.DEV_CONNECTION, provider, project.name))
    var uri by GradleProperty(project, String::class.java, String.format(GitHub.OPENGOOD_REPO_URI, project.name))
}
