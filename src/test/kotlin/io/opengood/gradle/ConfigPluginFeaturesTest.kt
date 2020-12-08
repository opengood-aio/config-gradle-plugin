package io.opengood.gradle

import helper.Features
import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import spec.*

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
})
