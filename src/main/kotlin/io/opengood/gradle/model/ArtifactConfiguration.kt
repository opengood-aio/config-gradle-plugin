package io.opengood.gradle.model

import groovy.lang.Closure
import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.constant.Artifacts
import io.opengood.gradle.constant.GitHub
import io.opengood.gradle.enumeration.PackagingType
import io.opengood.gradle.enumeration.PublicationType
import io.opengood.gradle.property.GradleListProperty
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Action
import org.gradle.api.Project

@AllOpen
class ArtifactConfiguration(private val project: Project) {
    var name by GradleProperty(project, String::class.java, project.name)
    var packaging by GradleProperty(project, PackagingType::class.java, PackagingType.JAR)
    var description by GradleProperty(project, String::class.java, Artifacts.DESCRIPTION)
    var uri by GradleProperty(project, String::class.java, String.format(GitHub.OPENGOOD_REPO_URI, project.name))
    var publications by GradleListProperty(project, PublicationType::class.java, listOf(PublicationType.OSS))
    var repo = RepoConfiguration(project)
    var scm = ScmConfiguration(project)
    var license = LicenseConfiguration(project)
    var developer = DeveloperConfiguration(project)

    fun repo(action: Action<in RepoConfiguration>) = action.execute(repo)

    fun repo(closure: Closure<Unit>) = project.configure(repo, closure)

    fun scm(action: Action<in ScmConfiguration>) = action.execute(scm)

    fun scm(closure: Closure<Unit>) = project.configure(scm, closure)

    fun license(action: Action<in LicenseConfiguration>) = action.execute(license)

    fun license(closure: Closure<Unit>) = project.configure(license, closure)

    fun developer(action: Action<in DeveloperConfiguration>) = action.execute(developer)

    fun developer(closure: Closure<Unit>) = project.configure(developer, closure)
}
