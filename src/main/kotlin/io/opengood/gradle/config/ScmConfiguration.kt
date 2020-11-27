package io.opengood.gradle.config

import io.opengood.gradle.enumeration.ScmProvider
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

class ScmConfiguration(
    project: Project,
    repo: RepoConfiguration
) {
    var provider by GradleProperty(project, ScmProvider::class.java, ScmProvider.GIT)
    var connection by GradleProperty(project, String::class.java, "${ScmProvider.PROTOCOL}:$provider:${repo.baseUri}/${repo.name}")
    var developerConnection by GradleProperty(project, String::class.java, "${ScmProvider.PROTOCOL}:$provider:${repo.baseUri}/${repo.name}")
    var uri by GradleProperty(project, String::class.java, "${repo.baseUri}/${repo.name}")
}
