package io.opengood.gradle.config

import groovy.lang.Closure
import io.opengood.gradle.constant.GitHub
import io.opengood.gradle.enumeration.PackagingType
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Action
import org.gradle.api.Project

open class ArtifactConfiguration(
    private val project: Project
) {
    var name by GradleProperty(project, String::class.java, project.name)
    var packaging by GradleProperty(project, PackagingType::class.java, PackagingType.JAR)
    var description by GradleProperty(project, String::class.java, "")
    var uri by GradleProperty(project, String::class.java, "${GitHub.OPENGOOD_ORG_URI}/${project.name}")
    var repo = RepoConfiguration(project)
    var scm = ScmConfiguration(project)
    var license = LicenseConfiguration(project)
    var developer = DeveloperConfiguration(project)

    open fun repo(action: Action<in RepoConfiguration>) =
        action.execute(repo)

    open fun repo(closure: Closure<Unit>) =
        project.configure(repo, closure)

    open fun scm(action: Action<in ScmConfiguration>) =
        action.execute(scm)

    open fun scm(closure: Closure<Unit>) =
        project.configure(scm, closure)

    open fun license(action: Action<in LicenseConfiguration>) =
        action.execute(license)

    open fun license(closure: Closure<Unit>) =
        project.configure(license, closure)

    open fun developer(action: Action<in DeveloperConfiguration>) =
        action.execute(developer)

    open fun developer(closure: Closure<Unit>) =
        project.configure(developer, closure)
}
