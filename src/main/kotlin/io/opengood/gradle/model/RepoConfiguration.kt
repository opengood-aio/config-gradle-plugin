package io.opengood.gradle.model

import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.constant.Repositories.Companion.GITHUB_PACKAGES_REPO_URI
import io.opengood.gradle.constant.Repositories.Companion.MAVEN_CENTRAL_PORTAL_SNAPSHOTS_REPO_URI
import io.opengood.gradle.constant.Repositories.Companion.MAVEN_CENTRAL_PORTAL_STAGING_REPO_URI
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

@AllOpen
class RepoConfiguration(
    project: Project,
) {
    var mavenCentralPortalSnapshotsRepoUri by GradleProperty(project, String::class.java, MAVEN_CENTRAL_PORTAL_SNAPSHOTS_REPO_URI)
    var mavenCentralPortalStagingRepoUri by GradleProperty(project, String::class.java, MAVEN_CENTRAL_PORTAL_STAGING_REPO_URI)
    var gitHubPackagesRepoUri by GradleProperty(project, String::class.java, String.format(GITHUB_PACKAGES_REPO_URI, project.name))
}
