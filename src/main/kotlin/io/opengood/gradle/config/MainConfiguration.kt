package io.opengood.gradle.config

import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import io.opengood.gradle.property.GradleProperty
import org.gradle.api.Project

class MainConfiguration(
    project: Project,
    val languageType: LanguageType
) {
    var projectType by GradleProperty(project, ProjectType::class.java, ProjectType.APP)
}
