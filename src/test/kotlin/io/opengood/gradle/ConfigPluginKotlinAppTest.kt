package io.opengood.gradle

import helper.ProjectConfig
import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import spec.addAssertjDependenciesTest
import spec.addJacksonKotlinDependenciesTest
import spec.addJunitJupiterDependenciesTest
import spec.addKotestDependenciesTest
import spec.addKotestSpringDependenciesTest
import spec.addKotlinDependenciesTest
import spec.addLombokDependenciesTest
import spec.addMockitoDependenciesTest
import spec.addMockkDependenciesTest
import spec.addRepositoriesTest
import spec.addSpringDependenciesTest
import spec.addSpringMockkDependenciesTest
import spec.applyCommonPluginsTest
import spec.applyJavaPluginTest
import spec.applyKotlinPluginsTest
import spec.applyKotlinSpringPluginsTest
import spec.applyLombokPluginTest
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
import spec.doNotAddJacksonKotlinDependenciesTest
import spec.doNotAddJunitJupiterDependenciesTest
import spec.doNotAddKotestDependenciesTest
import spec.doNotAddKotestSpringDependenciesTest
import spec.doNotAddKotlinDependenciesTest
import spec.doNotAddLombokDependenciesTest
import spec.doNotAddMockitoDependenciesTest
import spec.doNotAddMockkDependenciesTest
import spec.doNotAddSpringMockkDependenciesTest
import spec.doNotApplyGroovyPluginTest
import spec.doNotApplyKotlinPluginsTest
import spec.doNotApplyKotlinSpringPluginsTest
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
