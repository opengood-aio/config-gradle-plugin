package io.opengood.gradle.extension

import groovy.lang.Closure
import io.opengood.gradle.config.ArtifactConfiguration
import io.opengood.gradle.config.MainConfiguration
import io.opengood.gradle.config.TestConfiguration
import io.opengood.gradle.extension.OpenGoodExtension.Companion.EXTENSION_NAME
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.util.ConfigureUtil

open class OpenGoodExtension(
    project: Project
) {
    var main = MainConfiguration(project)
    var test = TestConfiguration(project)
    var artifact = ArtifactConfiguration(project)

    open fun main(action: Action<in MainConfiguration>) =
        action.execute(main)

    open fun main(closure: Closure<Unit>) =
        ConfigureUtil.configure(closure, main)

    open fun test(action: Action<in TestConfiguration>) =
        action.execute(test)

    open fun test(closure: Closure<Unit>) =
        ConfigureUtil.configure(closure, test)

    open fun artifact(action: Action<in ArtifactConfiguration>) =
        action.execute(artifact)

    open fun artifact(closure: Closure<Unit>) =
        ConfigureUtil.configure(closure, artifact)

    companion object {
        const val EXTENSION_NAME = "opengood"
    }
}

fun Project.opengood(): OpenGoodExtension =
    extensions.getByName(EXTENSION_NAME) as? OpenGoodExtension
        ?: throw IllegalStateException("$EXTENSION_NAME is not of the correct type")