package io.opengood.gradle.config

import io.opengood.gradle.constant.GitHub
import io.opengood.gradle.enumeration.ScmProvider
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

open class ScmConfiguration(
    project: Project
) {
    var provider by GradleProperty(project, ScmProvider::class.java, ScmProvider.GIT)
    var connection by GradleProperty(
        project,
        String::class.java,
        "${ScmProvider.PROTOCOL}:$provider:${GitHub.OPENGOOD_ORG_URI}/${project.name}"
    )
    var developerConnection by GradleProperty(
        project,
        String::class.java,
        "${ScmProvider.PROTOCOL}:$provider:${GitHub.OPENGOOD_ORG_URI}/${project.name}"
    )
    var uri by GradleProperty(project, String::class.java, "${GitHub.OPENGOOD_ORG_URI}/${project.name}")
}
