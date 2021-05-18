package io.opengood.gradle

import helper.ProjectConfig
import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import spec.addAssertjDependenciesTest
import spec.addJunitJupiterDependenciesTest
import spec.addLombokDependenciesTest
import spec.addMockitoDependenciesTest
import spec.addRepositoriesTest
import spec.addSpringDependenciesTest
import spec.applyCommonPluginsTest
import spec.applyJavaPluginTest
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
import spec.configureProcessResourcesTaskTest
import spec.configurePublishingExtensionTest
import spec.configureReleaseExtensionTest
import spec.configureSigningExtensionTest
import spec.configureSpringBootExtensionTest
import spec.configureTestTaskTest
import spec.createExtensionTest
import spec.doNotAddGroovyDependenciesTest
import spec.doNotAddJacksonKotlinDependenciesTest
import spec.doNotAddKotestDependenciesTest
import spec.doNotAddKotestSpringDependenciesTest
import spec.doNotAddKotlinCoroutinesDependenciesTest
import spec.doNotAddKotlinDependenciesTest
import spec.doNotAddMockkDependenciesTest
import spec.doNotAddSpringMockkDependenciesTest
import spec.doNotApplyGroovyPluginTest
import spec.doNotApplyKotlinPluginsTest
import spec.doNotApplyKotlinSpringPluginsTest
import spec.doNotApplyLibraryPluginTest

class ConfigPluginJavaAppTest : FunSpec({

    val project = createProject(
        ProjectConfig(
            languageType = LanguageType.JAVA,
            projectType = ProjectType.APP
        )
    )

    include(applyPluginTest(project))

    include(createExtensionTest(project, ProjectType.APP))
    include(doNotApplyGroovyPluginTest(project))
    include(applyJavaPluginTest(project))
    include(applyLombokPluginTest(project))
    include(doNotApplyKotlinPluginsTest(project))
    include(doNotApplyKotlinSpringPluginsTest(project))
    include(applyCommonPluginsTest(project))
    include(applySpringPluginsTest(project))
    include(doNotApplyLibraryPluginTest(project))

    include(configureConventionsTest(project))

    include(configureDependencyResolutionStrategyTest(project))
    include(configureBootJarResolutionTest(project))

    include(addRepositoriesTest(project))

    include(doNotAddGroovyDependenciesTest(project))
    include(doNotAddKotlinDependenciesTest(project))
    include(addAssertjDependenciesTest(project))
    include(doNotAddJacksonKotlinDependenciesTest(project))
    include(addJunitJupiterDependenciesTest(project))
    include(doNotAddKotestDependenciesTest(project))
    include(doNotAddKotestSpringDependenciesTest(project))
    include(doNotAddKotlinCoroutinesDependenciesTest(project))
    include(addLombokDependenciesTest(project))
    include(addMockitoDependenciesTest(project))
    include(doNotAddMockkDependenciesTest(project))
    include(addSpringDependenciesTest(project))
    include(doNotAddSpringMockkDependenciesTest(project))

    include(configureGradleWrapperTaskTest(project))
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
