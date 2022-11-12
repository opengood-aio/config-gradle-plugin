package io.opengood.gradle

import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import test.createProject
import test.model.ProjectConfig
import test.spec.doNotConfigureAfterReleaseBuildTaskTest
import test.spec.doNotConfigurePublishingExtensionTest
import test.spec.doNotConfigureSigningExtensionTest

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
