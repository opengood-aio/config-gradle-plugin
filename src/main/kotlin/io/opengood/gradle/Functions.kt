package com.gaig.cnd.gradle

import com.gaig.cnd.gradle.constant.Directories
import org.gradle.api.Project
import java.nio.file.Path

fun isGroovyProject(project: Project): Boolean {
    return Path.of(project.projectDir.absolutePath, Directories.GROOVY_SRC).toFile().exists()
}

fun isJavaProject(project: Project): Boolean {
    return Path.of(project.projectDir.absolutePath, Directories.JAVA_SRC).toFile().exists()
}

fun isKotlinProject(project: Project): Boolean {
    return Path.of(project.projectDir.absolutePath, Directories.KOTLIN_SRC).toFile().exists()
}