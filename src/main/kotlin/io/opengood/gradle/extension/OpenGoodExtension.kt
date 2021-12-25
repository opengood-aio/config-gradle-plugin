package io.opengood.gradle.extension

import groovy.lang.Closure
import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.config.ArtifactConfiguration
import io.opengood.gradle.config.FeatureConfiguration
import io.opengood.gradle.config.MainConfiguration
import io.opengood.gradle.config.ReleaseConfiguration
import io.opengood.gradle.config.TestConfiguration
import io.opengood.gradle.extension.OpenGoodExtension.Companion.EXTENSION_NAME
import org.gradle.api.Action
import org.gradle.api.Project

@AllOpen
class OpenGoodExtension(private val project: Project) {
    var main = MainConfiguration(project)
    var features = FeatureConfiguration(project)
    var test = TestConfiguration(project)
    var release = ReleaseConfiguration(project)
    var artifact = ArtifactConfiguration(project)

    fun main(action: Action<in MainConfiguration>) = action.execute(main)

    fun main(closure: Closure<Unit>) = project.configure(main, closure)

    fun features(action: Action<in FeatureConfiguration>) = action.execute(features)

    fun features(closure: Closure<Unit>) = project.configure(features, closure)

    fun test(action: Action<in TestConfiguration>) = action.execute(test)

    fun test(closure: Closure<Unit>) = project.configure(test, closure)

    fun release(action: Action<in ReleaseConfiguration>) = action.execute(release)

    fun release(closure: Closure<Unit>) = project.configure(release, closure)

    fun artifact(action: Action<in ArtifactConfiguration>) = action.execute(artifact)

    fun artifact(closure: Closure<Unit>) = project.configure(artifact, closure)

    companion object {
        const val EXTENSION_NAME = "opengood"
    }
}

fun Project.openGood(): OpenGoodExtension =
    extensions.getByName(EXTENSION_NAME) as? OpenGoodExtension
        ?: throw IllegalStateException("$EXTENSION_NAME is not of the correct type")
