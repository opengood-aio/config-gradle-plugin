package io.opengood.gradle

import io.opengood.gradle.constant.Directories
import io.opengood.gradle.enumeration.BuildGradleType
import io.opengood.gradle.enumeration.LanguageType
import org.gradle.api.Project
import java.nio.file.Path

internal val Project.buildGradleType: BuildGradleType
    get() =
        when {
            isKotlin -> BuildGradleType.KOTLIN
            else -> BuildGradleType.GROOVY
        }

internal inline fun <reified V : Any> getEnvVar(name: String, default: V): V =
    try {
        System.getenv(name) as V
    } catch (ignored: Exception) {
        default
    }

internal inline fun <reified T : Any> Project.getExtension(): T =
    extensions.getByType(T::class.java)

internal inline fun <reified T : Any> Project.getExtension(name: String): T =
    extensions.getByName(name) as T

internal inline fun <reified V : Any> Project.getProperty(name: String, default: V): V =
    try {
        property(name) as V
    } catch (ignored: Exception) {
        default
    }

internal val Project.isGroovy: Boolean
    get() =
        Path.of(projectDir.absolutePath, Directories.GROOVY_SRC).toFile().exists()

internal val Project.isJava: Boolean
    get() =
        Path.of(projectDir.absolutePath, Directories.JAVA_SRC).toFile().exists()

internal val Project.isKotlin: Boolean
    get() =
        Path.of(projectDir.absolutePath, Directories.KOTLIN_SRC).toFile().exists()

internal val Project.languageType: LanguageType
    get() =
        when {
            isGroovy -> LanguageType.GROOVY
            isJava -> LanguageType.JAVA
            isKotlin -> LanguageType.KOTLIN
            else -> throw IllegalStateException("Unable to detect LanguageType from Gradle project")
        }
