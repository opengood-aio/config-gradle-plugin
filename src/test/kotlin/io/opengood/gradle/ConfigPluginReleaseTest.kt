package io.opengood.gradle

import helper.ProjectConfig
import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import spec.configurePublishingExtensionTest
import spec.configureSigningExtensionTest

class ConfigPluginReleaseTest : FunSpec({

    val project = createProject(
        ProjectConfig(
            languageType = LanguageType.KOTLIN,
            version = "1.0.0"
        )
    )

    include(configurePublishingExtensionTest(project))
    include(configureSigningExtensionTest(project))
})
