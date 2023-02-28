package io.opengood.gradle

import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import test.createProject
import test.enumeration.TestFrameworks
import test.model.ProjectConfig
import test.spec.addAssertjDependenciesTest
import test.spec.addJunitJupiterDependenciesTest
import test.spec.addKotestDependenciesTest
import test.spec.addKotestSpringDependenciesTest
import test.spec.addKotlinCoroutinesDependenciesTest
import test.spec.addMockitoDependenciesTest
import test.spec.addMockkDependenciesTest
import test.spec.addSpringMockkDependenciesTest

class ConfigPluginTestFrameworksTest : FunSpec({

    val project = createProject(
        ProjectConfig(
            languageType = LanguageType.KOTLIN,
            testFrameworks = TestFrameworks.JAVA.flag,
        ),
    )

    include(addKotlinCoroutinesDependenciesTest(project))
    include(addKotestDependenciesTest(project))
    include(addKotestSpringDependenciesTest(project))
    include(addMockkDependenciesTest(project))
    include(addSpringMockkDependenciesTest(project))
    include(addAssertjDependenciesTest(project))
    include(addJunitJupiterDependenciesTest(project))
    include(addMockitoDependenciesTest(project))
})
