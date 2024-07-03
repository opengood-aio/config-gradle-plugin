package io.opengood.gradle.model

import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.constant.Repositories
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

@AllOpen
class RepoConfiguration(
    project: Project,
) {
    var gitHubPackagesRepoUri by GradleProperty(
        project,
        String::class.java,
        String.format(Repositories.GITHUB_PACKAGES_REPO_URI, project.name),
    )
}
