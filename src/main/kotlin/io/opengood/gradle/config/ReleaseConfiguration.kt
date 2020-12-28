package io.opengood.gradle.config

import io.opengood.gradle.constant.Releases
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

open class ReleaseConfiguration(
    project: Project
) {
    var afterReleaseBuildTasks by GradleProperty(project, Array<Any>::class.java, Releases.AFTER_RELEASE_BUILD_TASKS)
    var requireBranch by GradleProperty(project, String::class.java, Releases.REQUIRE_BRANCH)
    var pushToRemote by GradleProperty(project, String::class.java, Releases.PUSH_TO_REMOTE)
}
