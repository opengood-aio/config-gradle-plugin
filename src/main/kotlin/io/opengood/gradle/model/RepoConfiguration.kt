package io.opengood.gradle.model

import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.constant.Repositories
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

@AllOpen
class RepoConfiguration(project: Project) {
    var ossSnapshotsRepoUri by GradleProperty(project, String::class.java, Repositories.OSS_SNAPSHOTS_REPO_URI)
    var ossStagingRepoUri by GradleProperty(project, String::class.java, Repositories.OSS_STAGING_REPO_URI)
    var gitHubPackagesRepoUri by GradleProperty(
        project,
        String::class.java,
        String.format(Repositories.GITHUB_PACKAGES_REPO_URI, project.name),
    )
}
