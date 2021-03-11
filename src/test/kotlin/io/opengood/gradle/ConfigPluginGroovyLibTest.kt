package io.opengood.gradle

import helper.ProjectConfig
import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import spec.addAssertjDependenciesTest
import spec.addGroovyDependenciesTest
import spec.addJunitDependenciesTest
import spec.addMockitoDependenciesTest
import spec.addRepositoriesTest
import spec.addSpringDependenciesTest
import spec.applyCommonPluginsTest
import spec.applyGroovyPluginTest
import spec.applyJavaPluginTest
import spec.applyLibraryPluginTest
import spec.applyPluginTest
import spec.applySpringPluginsTest
import spec.configureAfterReleaseBuildTaskTest
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
import spec.configurePublishingExtensionTest
import spec.configureReleaseExtensionTest
import spec.configureSigningExtensionTest
import spec.configureTestTaskTest
import spec.createExtensionTest
import spec.doNotAddKotestDependenciesTest
import spec.doNotAddKotlinDependenciesTest
import spec.doNotAddLombokDependenciesTest
import spec.doNotAddMockkDependenciesTest
import spec.doNotAddSpringMockkDependenciesTest
import spec.doNotApplyKotlinPluginsTest
import spec.doNotApplyKotlinSpringPluginsTest
import spec.doNotApplyLombokPluginTest

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
    include(applyCommonPluginsTest(project))
    include(applyLibraryPluginTest(project))
    include(applySpringPluginsTest(project))
    include(doNotApplyKotlinPluginsTest(project))
    include(doNotApplyKotlinSpringPluginsTest(project))
    include(doNotApplyLombokPluginTest(project))

    include(configureDependencyResolutionStrategyTest(project))

    include(configureConventionsTest(project))

    include(addRepositoriesTest(project))

    include(addGroovyDependenciesTest(project))
    include(addSpringDependenciesTest(project))
    include(addJunitDependenciesTest(project))
    include(addAssertjDependenciesTest(project))
    include(addMockitoDependenciesTest(project))
    include(doNotAddKotlinDependenciesTest(project))
    include(doNotAddLombokDependenciesTest(project))
    include(doNotAddKotestDependenciesTest(project))
    include(doNotAddMockkDependenciesTest(project))
    include(doNotAddSpringMockkDependenciesTest(project))

    include(configureGradleWrapperTaskTest(project))
    include(configureJavaCompileTaskTest(project))
    include(configureDependencyUpdatesTaskTest(project))
    include(configureTestTaskTest(project))
    include(configureJacocoTestReportTaskTest(project))
    include(configureJarTaskTest(project, isEnabled = true))
    include(configureBootJarTaskTest(project, isEnabled = false))
    include(configureAfterReleaseBuildTaskTest(project))

    include(configureDependencyManagementExtensionTest(project))
    include(configureJavaExtensionTest(project))
    include(configureReleaseExtensionTest(project))
    include(configurePublishingExtensionTest(project))
    include(configureSigningExtensionTest(project))
})
