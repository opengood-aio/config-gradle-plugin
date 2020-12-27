package io.opengood.gradle

import helper.Features
import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import spec.doNotAddAssertjDependenciesTest
import spec.doNotAddJunitDependenciesTest
import spec.doNotAddKotestDependenciesTest
import spec.doNotAddLombokDependenciesTest
import spec.doNotAddMockitoDependenciesTest
import spec.doNotAddMockkDependenciesTest
import spec.doNotAddSpringDependenciesTest
import spec.doNotConfigureAfterReleaseBuildTaskTest
import spec.doNotConfigurePublishingExtensionTest
import spec.doNotConfigureSigningExtensionTest
import spec.doNotConfigureUploadArchivesTaskTest

class ConfigPluginFeaturesTest : FunSpec({

    val project = createProject(
        languageType = LanguageType.KOTLIN,
        features = Features.NONE.flag
    )

    include(doNotAddSpringDependenciesTest(project))
    include(doNotAddLombokDependenciesTest(project))
    include(doNotAddJunitDependenciesTest(project))
    include(doNotAddAssertjDependenciesTest(project))
    include(doNotAddMockitoDependenciesTest(project))
    include(doNotAddKotestDependenciesTest(project))
    include(doNotAddMockkDependenciesTest(project))

    include(doNotConfigureAfterReleaseBuildTaskTest(project))
    include(doNotConfigureUploadArchivesTaskTest(project))

    include(doNotConfigurePublishingExtensionTest(project))
    include(doNotConfigureSigningExtensionTest(project))
})
