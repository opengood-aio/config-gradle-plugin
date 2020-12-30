package io.opengood.gradle

import io.opengood.gradle.constant.Directories
import io.opengood.gradle.enumeration.BuildGradleType
import io.opengood.gradle.enumeration.LanguageType
import net.researchgate.release.GitAdapter
import net.researchgate.release.ReleaseExtension
import org.gradle.api.Project
import java.lang.System.getenv
import java.nio.file.Path

internal val Project.buildGradleType: BuildGradleType
    get() =
        when {
            isKotlin -> BuildGradleType.KOTLIN
            else -> BuildGradleType.GROOVY
        }

internal inline fun <reified V : Any> getEnv(name: String, default: V): V =
    if (getenv().containsKey(name)) getenv(name) as V else default

internal inline fun <reified T : Any> Project.getExtension(): T =
    extensions.getByType(T::class.java)

internal inline fun <reified T : Any> Project.getExtension(name: String): T =
    extensions.getByName(name) as T

internal inline fun <reified V : Any> Project.getProperty(name: String, default: V): V =
    if (hasProperty(name)) property(name) as V else default

internal fun ReleaseExtension.git(config: GitAdapter.GitConfig.() -> Unit) =
    (propertyMissing("git") as GitAdapter.GitConfig).config()

internal val ReleaseExtension.git: GitAdapter.GitConfig
    get() =
        (propertyMissing("git") as GitAdapter.GitConfig)

internal val Project.isGroovy: Boolean
    get() =
        Path.of(projectDir.absolutePath, Directories.GROOVY_SRC).toFile().exists()

internal val Project.isJava: Boolean
    get() =
        Path.of(projectDir.absolutePath, Directories.JAVA_SRC).toFile().exists() ||
            Path.of(projectDir.absolutePath, BuildGradleType.GROOVY.toString()).toFile().exists()

internal val Project.isKotlin: Boolean
    get() =
        Path.of(projectDir.absolutePath, Directories.KOTLIN_SRC).toFile().exists() ||
            Path.of(projectDir.absolutePath, BuildGradleType.KOTLIN.toString()).toFile().exists()

internal val Project.languageType: LanguageType
    get() =
        when {
            isKotlin -> LanguageType.KOTLIN
            isGroovy -> LanguageType.GROOVY
            isJava -> LanguageType.JAVA
            else -> throw IllegalStateException("Unable to detect LanguageType from Gradle project")
        }
