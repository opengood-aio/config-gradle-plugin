package io.opengood.gradle.config

import io.opengood.gradle.annotation.AllOpen
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import io.opengood.gradle.languageType
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

@AllOpen
class MainConfiguration(project: Project) {
    val languageType: LanguageType = project.languageType
    var projectType by GradleProperty(project, ProjectType::class.java, ProjectType.APP)
}
