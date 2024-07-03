package io.opengood.gradle

import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import test.createProject
import test.enumeration.defaultFeatures
import test.enumeration.kotlinFeatures
import test.model.ProjectConfig
import test.spec.configureMavenCentralPublishingExtensionTest
import test.spec.configureMavenPublishingExtensionTest

class ConfigPluginReleaseTest :
    FunSpec({

        val project =
            createProject(
                ProjectConfig(
                    languageType = LanguageType.KOTLIN,
                    version = "1.0.0",
                    features = defaultFeatures + kotlinFeatures,
                ),
            )

        include(configureMavenCentralPublishingExtensionTest(project))
        include(configureMavenPublishingExtensionTest(project))
    })
