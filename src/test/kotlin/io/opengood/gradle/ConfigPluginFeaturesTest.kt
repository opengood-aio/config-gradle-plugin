package io.opengood.gradle

import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import test.createProject
import test.enumeration.Features
import test.model.ProjectConfig
import test.spec.doNotAddAssertjDependenciesTest
import test.spec.doNotAddJacksonDependenciesTest
import test.spec.doNotAddJacksonKotlinDependenciesTest
import test.spec.doNotAddJunitJupiterDependenciesTest
import test.spec.doNotAddKotestDependenciesTest
import test.spec.doNotAddKotestSpringDependenciesTest
import test.spec.doNotAddKotlinCoroutinesDependenciesTest
import test.spec.doNotAddLombokDependenciesTest
import test.spec.doNotAddMockitoDependenciesTest
import test.spec.doNotAddMockkDependenciesTest
import test.spec.doNotAddSpringDependenciesTest
import test.spec.doNotAddSpringMockkDependenciesTest
import test.spec.doNotConfigureDependencyManagementExtensionTest

class ConfigPluginFeaturesTest : FunSpec({

    val project = createProject(
        ProjectConfig(
            languageType = LanguageType.KOTLIN,
            features = Features.NONE.flag,
        ),
    )

    include(doNotAddAssertjDependenciesTest(project))
    include(doNotAddJacksonDependenciesTest(project))
    include(doNotAddJacksonKotlinDependenciesTest(project))
    include(doNotAddJunitJupiterDependenciesTest(project))
    include(doNotAddKotestDependenciesTest(project))
    include(doNotAddKotestSpringDependenciesTest(project))
    include(doNotAddKotlinCoroutinesDependenciesTest(project))
    include(doNotAddLombokDependenciesTest(project))
    include(doNotAddMockitoDependenciesTest(project))
    include(doNotAddMockkDependenciesTest(project))
    include(doNotAddSpringDependenciesTest(project))
    include(doNotAddSpringMockkDependenciesTest(project))

    include(doNotConfigureDependencyManagementExtensionTest(project))
})
