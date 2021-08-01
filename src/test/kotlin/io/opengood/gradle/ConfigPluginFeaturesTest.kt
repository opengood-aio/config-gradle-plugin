package io.opengood.gradle

import helper.Features
import helper.ProjectConfig
import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import spec.doNotAddAssertjDependenciesTest
import spec.doNotAddJacksonKotlinDependenciesTest
import spec.doNotAddJunitJupiterDependenciesTest
import spec.doNotAddKotestDependenciesTest
import spec.doNotAddKotestSpringDependenciesTest
import spec.doNotAddKotlinCoroutinesDependenciesTest
import spec.doNotAddLombokDependenciesTest
import spec.doNotAddMockitoDependenciesTest
import spec.doNotAddMockkDependenciesTest
import spec.doNotAddSpringDependenciesTest
import spec.doNotAddSpringMockkDependenciesTest
import spec.doNotConfigureDependencyManagementExtensionTest

class ConfigPluginFeaturesTest : FunSpec({

    val project = createProject(
        ProjectConfig(
            languageType = LanguageType.KOTLIN,
            features = Features.NONE.flag
        )
    )

    include(doNotAddAssertjDependenciesTest(project))
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
