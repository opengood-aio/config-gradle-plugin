package io.opengood.gradle

import helper.ProjectConfig
import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import spec.addJacksonKotlinDependenciesTest
import spec.addKotestDependenciesTest
import spec.addKotestSpringDependenciesTest
import spec.addKotlinDependenciesTest
import spec.addMockkDependenciesTest
import spec.addRepositoriesTest
import spec.addSpringDependenciesTest
import spec.addSpringMockkDependenciesTest
import spec.applyCommonPluginsTest
import spec.applyJavaPluginTest
import spec.applyKotlinPluginsTest
import spec.applyKotlinSpringPluginsTest
import spec.applyPluginTest
import spec.applySpringPluginsTest
import spec.configureAfterReleaseBuildTaskTest
import spec.configureBootJarResolutionTest
import spec.configureBootJarTaskTest
import spec.configureConventionsTest
import spec.configureDependencyManagementExtensionTest
import spec.configureDependencyResolutionStrategyTest
import spec.configureDependencyUpdatesTaskTest
import spec.configureGradleWrapperTaskTest
import spec.configureJacocoTestReportTaskTest
import spec.configureJarTaskTest
import spec.configureJavaCompileTaskTest
import spec.configureJavaExtensionTest
import spec.configureKotlinCompileTaskTest
import spec.configureProcessResourcesTaskTest
import spec.configurePublishingExtensionTest
import spec.configureReleaseExtensionTest
import spec.configureSigningExtensionTest
import spec.configureSpringBootExtensionTest
import spec.configureTestTaskTest
import spec.createExtensionTest
import spec.doNotAddAssertjDependenciesTest
import spec.doNotAddGroovyDependenciesTest
import spec.doNotAddJunitJupiterDependenciesTest
import spec.doNotAddLombokDependenciesTest
import spec.doNotAddMockitoDependenciesTest
import spec.doNotApplyGroovyPluginTest
import spec.doNotApplyLibraryPluginTest
import spec.doNotApplyLombokPluginTest

class ConfigPluginKotlinAppTest : FunSpec({

    val project = createProject(
        ProjectConfig(
            languageType = LanguageType.KOTLIN,
            projectType = ProjectType.APP
        )
    )

    include(applyPluginTest(project))

    include(createExtensionTest(project, ProjectType.APP))

    include(doNotApplyGroovyPluginTest(project))
    include(applyJavaPluginTest(project))
    include(doNotApplyLombokPluginTest(project))
    include(applyKotlinPluginsTest(project))
    include(applyKotlinSpringPluginsTest(project))
    include(applyCommonPluginsTest(project))
    include(applySpringPluginsTest(project))
    include(doNotApplyLibraryPluginTest(project))

    include(configureConventionsTest(project))

    include(configureDependencyResolutionStrategyTest(project))
    include(configureBootJarResolutionTest(project))

    include(addRepositoriesTest(project))

    include(doNotAddGroovyDependenciesTest(project))
    include(addKotlinDependenciesTest(project))
    include(doNotAddAssertjDependenciesTest(project))
    include(addJacksonKotlinDependenciesTest(project))
    include(doNotAddJunitJupiterDependenciesTest(project))
    include(addKotestDependenciesTest(project))
    include(addKotestSpringDependenciesTest(project))
    include(doNotAddLombokDependenciesTest(project))
    include(doNotAddMockitoDependenciesTest(project))
    include(addMockkDependenciesTest(project))
    include(addSpringDependenciesTest(project))
    include(addSpringMockkDependenciesTest(project))

    include(configureGradleWrapperTaskTest(project))
    include(configureKotlinCompileTaskTest(project))
    include(configureJavaCompileTaskTest(project))
    include(configureProcessResourcesTaskTest(project))
    include(configureDependencyUpdatesTaskTest(project))
    include(configureTestTaskTest(project))
    include(configureJacocoTestReportTaskTest(project))
    include(configureJarTaskTest(project, isEnabled = false))
    include(configureBootJarTaskTest(project, isEnabled = true))
    include(configureAfterReleaseBuildTaskTest(project))

    include(configureDependencyManagementExtensionTest(project))
    include(configureJavaExtensionTest(project))
    include(configureSpringBootExtensionTest(project))
    include(configureReleaseExtensionTest(project))
    include(configurePublishingExtensionTest(project))
    include(configureSigningExtensionTest(project))
})
