package io.opengood.gradle

import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import test.createProject
import test.model.ProjectConfig
import test.spec.configurePublishingExtensionTest
import test.spec.configureSigningExtensionTest

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
