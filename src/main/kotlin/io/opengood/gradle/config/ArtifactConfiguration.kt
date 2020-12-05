package io.opengood.gradle.config

import groovy.lang.Closure
import io.opengood.gradle.constant.GitHub
import io.opengood.gradle.enumeration.PackagingType
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.util.ConfigureUtil

open class ArtifactConfiguration(
    project: Project
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
        ConfigureUtil.configure(closure, repo)

    open fun scm(action: Action<in ScmConfiguration>) =
        action.execute(scm)

    open fun scm(closure: Closure<Unit>) =
        ConfigureUtil.configure(closure, scm)

    open fun license(action: Action<in LicenseConfiguration>) =
        action.execute(license)

    open fun license(closure: Closure<Unit>) =
        ConfigureUtil.configure(closure, license)

    open fun developer(action: Action<in DeveloperConfiguration>) =
        action.execute(developer)

    open fun developer(closure: Closure<Unit>) =
        ConfigureUtil.configure(closure, developer)
}
