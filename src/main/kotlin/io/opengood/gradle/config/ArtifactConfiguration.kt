package io.opengood.gradle.config

import io.opengood.gradle.constant.Repositories
import io.opengood.gradle.enumeration.PackagingType
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

class ArtifactConfiguration(
    project: Project,
    repo: RepoConfiguration
) {
    var archiveBaseName by GradleProperty(project, String::class.java, project.name)
    var name by GradleProperty(project, String::class.java, project.name)
    var packaging by GradleProperty(project, PackagingType::class.java, PackagingType.JAR)
    var description by GradleProperty(project, String::class.java, "")
    var uri by GradleProperty(project, String::class.java, "${repo.baseUri}/${repo.name}")
    var snapshotsUri by GradleProperty(project, String::class.java, Repositories.OSS_SNAPSHOTS_REPO_URI)
    var stagingUri by GradleProperty(project, String::class.java, Repositories.OSS_STAGING_REPO_URI)
    val scm = ScmConfiguration(project, repo)

    val licenses = project.container(LicenseConfiguration::class.java) { id ->
        LicenseConfiguration(project, repo, id)
    }

    val developers = project.container(DeveloperConfiguration::class.java) { id ->
        DeveloperConfiguration(project, id)
    }
}
