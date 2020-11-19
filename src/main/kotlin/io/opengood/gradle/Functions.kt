package io.opengood.gradle

import io.opengood.gradle.constant.Directories
import io.opengood.gradle.enumeration.LanguageType
import org.gradle.api.Project
import java.lang.IllegalStateException
import java.nio.file.Path

fun getLanguageType(project: Project): LanguageType {
    return when {
        isGroovyProject(project) -> LanguageType.GROOVY
        isJavaProject(project) -> LanguageType.JAVA
        isKotlinProject(project) -> LanguageType.KOTLIN
        else -> throw IllegalStateException("Unable to detect LanguageType from Gradle project")
    }
}

fun isGroovyProject(project: Project): Boolean {
    return Path.of(project.projectDir.absolutePath, Directories.GROOVY_SRC).toFile().exists()
}

fun isJavaProject(project: Project): Boolean {
    return Path.of(project.projectDir.absolutePath, Directories.JAVA_SRC).toFile().exists()
}

fun isKotlinProject(project: Project): Boolean {
    return Path.of(project.projectDir.absolutePath, Directories.KOTLIN_SRC).toFile().exists()
}