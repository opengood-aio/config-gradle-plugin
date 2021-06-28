package io.opengood.gradle

import helper.ProjectConfig
import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import spec.addAssertjDependenciesTest
import spec.addGroovyDependenciesTest
import spec.addJunitJupiterDependenciesTest
import spec.addMockitoDependenciesTest
import spec.addRepositoriesTest
import spec.addSpringDependenciesTest
import spec.applyCommonPluginsTest
import spec.applyGroovyPluginTest
import spec.applyJavaPluginTest
import spec.applyPluginTest
import spec.applySpringPluginsTest
import spec.configureAfterReleaseBuildTaskTest
import spec.configureBasePluginTest
import spec.configureBootJarResolutionTest
import spec.configureBootJarTaskTest
import spec.configureDependencyManagementExtensionTest
import spec.configureDependencyResolutionStrategyTest
import spec.configureDependencyUpdatesTaskTest
import spec.configureGradleWrapperTaskTest
import spec.configureJacocoTestReportTaskTest
import spec.configureJarTaskTest
import spec.configureJavaCompileTaskTest
import spec.configureJavaExtensionTest
import spec.configureProcessResourcesTaskTest
import spec.configurePublishingExtensionTest
import spec.configureReleaseExtensionTest
import spec.configureSigningExtensionTest
import spec.configureSpringBootExtensionTest
import spec.configureTestTaskTest
import spec.createExtensionTest
import spec.doNotAddJacksonKotlinDependenciesTest
import spec.doNotAddKotestDependenciesTest
import spec.doNotAddKotestSpringDependenciesTest
import spec.doNotAddKotlinCoroutinesDependenciesTest
import spec.doNotAddKotlinDependenciesTest
import spec.doNotAddLombokDependenciesTest
import spec.doNotAddMockkDependenciesTest
import spec.doNotAddSpringMockkDependenciesTest
import spec.doNotApplyKotlinPluginsTest
import spec.doNotApplyKotlinSpringPluginsTest
import spec.doNotApplyLibraryPluginTest
import spec.doNotApplyLombokPluginTest

class ConfigPluginGroovyAppTest : FunSpec({

    val project = createProject(
        ProjectConfig(
            languageType = LanguageType.GROOVY,
            projectType = ProjectType.APP
        )
    )

    include(applyPluginTest(project))

    include(createExtensionTest(project, ProjectType.APP))

    include(applyGroovyPluginTest(project))
    include(applyJavaPluginTest(project))
    include(doNotApplyLombokPluginTest(project))
    include(doNotApplyKotlinPluginsTest(project))
    include(doNotApplyKotlinSpringPluginsTest(project))
    include(applyCommonPluginsTest(project))
    include(applySpringPluginsTest(project))
    include(doNotApplyLibraryPluginTest(project))

    include(configureDependencyResolutionStrategyTest(project))
    include(configureBootJarResolutionTest(project))

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
    include(configureProcessResourcesTaskTest(project))
    include(configureJavaCompileTaskTest(project))
    include(configureDependencyUpdatesTaskTest(project))
    include(configureTestTaskTest(project))
    include(configureJacocoTestReportTaskTest(project))
    include(configureJarTaskTest(project, isEnabled = false))
    include(configureBootJarTaskTest(project, isEnabled = true))
    include(configureAfterReleaseBuildTaskTest(project))

    include(configureBasePluginTest(project))
    include(configureDependencyManagementExtensionTest(project))
    include(configureJavaExtensionTest(project))
    include(configureSpringBootExtensionTest(project))
    include(configureReleaseExtensionTest(project))
    include(configurePublishingExtensionTest(project))
    include(configureSigningExtensionTest(project))
})
