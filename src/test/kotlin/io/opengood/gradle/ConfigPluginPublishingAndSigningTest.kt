package io.opengood.gradle

import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import test.createProject
import test.enumeration.defaultFeatures
import test.enumeration.kotlinFeatures
import test.model.ProjectConfig
import test.spec.doNotConfigureAfterReleaseBuildTaskTest
import test.spec.doNotConfigureMavenCentralPublishingExtensionTest
import test.spec.doNotConfigureMavenPublishingExtensionTest

class ConfigPluginPublishingAndSigningTest :
    FunSpec({

        val project =
            createProject(
                ProjectConfig(
                    languageType = LanguageType.KOTLIN,
                    features = defaultFeatures + kotlinFeatures,
                    publications = emptyList(),
                ),
            )

        include(doNotConfigureAfterReleaseBuildTaskTest(project))

        include(doNotConfigureMavenCentralPublishingExtensionTest(project))
        include(doNotConfigureMavenPublishingExtensionTest(project))
    })
