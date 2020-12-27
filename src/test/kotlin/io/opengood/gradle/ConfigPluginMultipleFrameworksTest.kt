package io.opengood.gradle

import helper.MultipleFrameworks
import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import spec.addAssertjDependenciesTest
import spec.addJunitDependenciesTest
import spec.addKotestDependenciesTest
import spec.addMockitoDependenciesTest
import spec.addMockkDependenciesTest

class ConfigPluginMultipleFrameworksTest : FunSpec({

    val project = createProject(
        languageType = LanguageType.KOTLIN,
        multipleFrameworks = MultipleFrameworks.KOTLIN.flag
    )

    include(addJunitDependenciesTest(project))
    include(addAssertjDependenciesTest(project))
    include(addMockitoDependenciesTest(project))
    include(addKotestDependenciesTest(project))
    include(addMockkDependenciesTest(project))
})
