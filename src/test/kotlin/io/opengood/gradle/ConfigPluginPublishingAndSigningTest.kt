package io.opengood.gradle

import helper.ProjectConfig
import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import spec.doNotConfigureAfterReleaseBuildTaskTest
import spec.doNotConfigurePublishingExtensionTest
import spec.doNotConfigureSigningExtensionTest

class ConfigPluginPublishingAndSigningTest : FunSpec({

    val project = createProject(
        ProjectConfig(
            languageType = LanguageType.KOTLIN,
            publications = emptyList()
        )
    )

    include(doNotConfigureAfterReleaseBuildTaskTest(project))

    include(doNotConfigurePublishingExtensionTest(project))
    include(doNotConfigureSigningExtensionTest(project))
})
