package io.opengood.gradle.config

import io.opengood.gradle.constant.Repositories
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

open class RepoConfiguration(
    project: Project
) {
    var snapshotsRepoUri by GradleProperty(project, String::class.java, Repositories.OSS_SNAPSHOTS_REPO_URI)
    var stagingRepoUri by GradleProperty(project, String::class.java, Repositories.OSS_STAGING_REPO_URI)
}
