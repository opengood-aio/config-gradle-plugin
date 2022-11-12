package io.opengood.gradle

import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import test.createProject
import test.model.ProjectConfig
import test.spec.addAssertjDependenciesTest
import test.spec.addGroovyDependenciesTest
import test.spec.addJunitJupiterDependenciesTest
import test.spec.addMockitoDependenciesTest
import test.spec.addRepositoriesTest
import test.spec.addSpringDependenciesTest
import test.spec.applyCommonPluginsTest
import test.spec.applyGroovyPluginTest
import test.spec.applyJavaPluginTest
import test.spec.applyLibraryPluginTest
import test.spec.applyPluginTest
import test.spec.applySpringPluginsTest
import test.spec.configureAfterReleaseBuildTaskTest
import test.spec.configureBasePluginExtensionTest
import test.spec.configureBootJarTaskTest
import test.spec.configureDependencyManagementExtensionTest
import test.spec.configureDependencyResolutionStrategyTest
import test.spec.configureGradleWrapperTaskTest
import test.spec.configureJacocoTestReportTaskTest
import test.spec.configureJarTaskTest
import test.spec.configureJavaCompileTaskTest
import test.spec.configureJavaExtensionTest
import test.spec.configurePublishingExtensionTest
import test.spec.configureReleaseExtensionTest
import test.spec.configureSigningExtensionTest
import test.spec.configureTestTaskTest
import test.spec.createExtensionTest
import test.spec.doNotAddJacksonKotlinDependenciesTest
import test.spec.doNotAddKotestDependenciesTest
import test.spec.doNotAddKotestSpringDependenciesTest
import test.spec.doNotAddKotlinCoroutinesDependenciesTest
import test.spec.doNotAddKotlinDependenciesTest
import test.spec.doNotAddLombokDependenciesTest
import test.spec.doNotAddMockkDependenciesTest
import test.spec.doNotAddSpringMockkDependenciesTest
import test.spec.doNotApplyKotlinPluginsTest
import test.spec.doNotApplyKotlinSpringPluginsTest
import test.spec.doNotApplyLombokPluginTest
import test.spec.doNotConfigureBootJarResolutionTest

class ConfigPluginGroovyLibTest : FunSpec({

    val project = createProject(
        ProjectConfig(
            languageType = LanguageType.GROOVY,
            projectType = ProjectType.LIB
        )
    )

    include(applyPluginTest(project))

    include(createExtensionTest(project, ProjectType.LIB))

    include(applyGroovyPluginTest(project))
    include(applyJavaPluginTest(project))
    include(doNotApplyLombokPluginTest(project))
    include(doNotApplyKotlinPluginsTest(project))
    include(doNotApplyKotlinSpringPluginsTest(project))
    include(applyCommonPluginsTest(project))
    include(applySpringPluginsTest(project))
    include(applyLibraryPluginTest(project))

    include(configureDependencyResolutionStrategyTest(project))
    include(doNotConfigureBootJarResolutionTest(project))

    include(addRepositoriesTest(project))

    include(addGroovyDependenciesTest(project))
    include(doNotAddKotlinDependenciesTest(project))
    include(addAssertjDependenciesTest(project))
    include(doNotAddJacksonKotlinDependenciesTest(project))
    include(addJunitJupiterDependenciesTest(project))
    include(doNotAddKotestDependenciesTest(project))
    include(doNotAddKotestSpringDependenciesTest(project))
    include(doNotAddKotlinCoroutinesDependenciesTest(project))
    include(doNotAddLombokDependenciesTest(project))
    include(addMockitoDependenciesTest(project))
    include(doNotAddMockkDependenciesTest(project))
    include(addSpringDependenciesTest(project))
    include(doNotAddSpringMockkDependenciesTest(project))

    include(configureGradleWrapperTaskTest(project))
    include(configureJavaCompileTaskTest(project))
    include(configureTestTaskTest(project))
    include(configureJacocoTestReportTaskTest(project))
    include(configureJarTaskTest(project, isEnabled = true))
    include(configureBootJarTaskTest(project, isEnabled = false))
    include(configureAfterReleaseBuildTaskTest(project))

    include(configureBasePluginExtensionTest(project))
    include(configureDependencyManagementExtensionTest(project))
    include(configureJavaExtensionTest(project))
    include(configureReleaseExtensionTest(project))
    include(configurePublishingExtensionTest(project))
    include(configureSigningExtensionTest(project))
})
