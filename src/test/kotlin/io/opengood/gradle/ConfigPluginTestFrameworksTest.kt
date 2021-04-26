package io.opengood.gradle

import helper.ProjectConfig
import helper.TestFrameworks
import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import spec.addAssertjDependenciesTest
import spec.addJunitJupiterDependenciesTest
import spec.addKotestDependenciesTest
import spec.addKotestSpringDependenciesTest
import spec.addMockitoDependenciesTest
import spec.addMockkDependenciesTest
import spec.addSpringMockkDependenciesTest

class ConfigPluginTestFrameworksTest : FunSpec({

    val project = createProject(
        ProjectConfig(
            languageType = LanguageType.KOTLIN,
            testFrameworks = TestFrameworks.JAVA.flag
        )
    )

    include(addAssertjDependenciesTest(project))
    include(addJunitJupiterDependenciesTest(project))
    include(addKotestDependenciesTest(project))
    include(addKotestSpringDependenciesTest(project))
    include(addMockitoDependenciesTest(project))
    include(addMockkDependenciesTest(project))
    include(addSpringMockkDependenciesTest(project))
})
