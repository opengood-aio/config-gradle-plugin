package io.opengood.gradle

import io.opengood.gradle.constant.Directories
import io.opengood.gradle.enumeration.LanguageType
import org.gradle.api.Project
import java.nio.file.Path

internal inline fun <reified T : Any> getExtensionByName(project: Project, name: String): T =
    project.extensions.getByName(name) as T

internal inline fun <reified T : Any> getExtensionByType(project: Project): T =
    project.extensions.getByType(T::class.java)

internal fun getLanguageType(project: Project): LanguageType =
    when {
        isGroovyProject(project) -> LanguageType.GROOVY
        isJavaProject(project) -> LanguageType.JAVA
        isKotlinProject(project) -> LanguageType.KOTLIN
        else -> throw IllegalStateException("Unable to detect LanguageType from Gradle project")
    }

internal inline fun <reified V : Any> getProperty(project: Project, name: String, default: V): V =
    try {
        project.property(name) as V
    } catch (ignored: Exception) {
        default
    }

internal fun isGroovyProject(project: Project): Boolean =
    Path.of(project.projectDir.absolutePath, Directories.GROOVY_SRC).toFile().exists()

internal fun isJavaProject(project: Project): Boolean =
    Path.of(project.projectDir.absolutePath, Directories.JAVA_SRC).toFile().exists()

internal fun isKotlinProject(project: Project): Boolean =
    Path.of(project.projectDir.absolutePath, Directories.KOTLIN_SRC).toFile().exists()

internal fun <TSource : Any, TConverted : Any> transform(
    items: List<TSource>,
    converter: (source: TSource) -> TConverted
): MutableList<TConverted> {
    val list = mutableListOf<TConverted>()
    items.forEach { item ->
        list.add(converter(item))
    }
    return list
}
