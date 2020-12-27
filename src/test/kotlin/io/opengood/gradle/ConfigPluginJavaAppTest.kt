package io.opengood.gradle

import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import spec.addAssertjDependenciesTest
import spec.addJunitDependenciesTest
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
import spec.configureBootJarTaskTest
import spec.configureConventionsTest
import spec.configureDependencyUpdatesTaskTest
import spec.configureGradleWrapperTaskTest
import spec.configureJacocoTestReportTaskTest
import spec.configureJarArtifactTest
import spec.configureJarSigningTest
import spec.configureJarTaskTest
import spec.configureJavaCompileTaskTest
import spec.configureProcessResourcesTaskTest
import spec.configurePublishingExtensionTest
import spec.configureReleaseExtensionTest
import spec.configureSigningExtensionTest
import spec.configureSpringBootExtensionTest
import spec.configureTestTaskTest
import spec.configureUploadArchivesTaskTest
import spec.createExtensionTest
import spec.doNotAddGroovyDependenciesTest
import spec.doNotAddKotestDependenciesTest
import spec.doNotAddKotlinDependenciesTest
import spec.doNotAddMockkDependenciesTest
import spec.doNotApplyGroovyPluginTest
import spec.doNotApplyKotlinPluginsTest
import spec.doNotApplyKotlinSpringPluginsTest
import spec.doNotApplyLibraryPluginTest

class ConfigPluginJavaAppTest : FunSpec({

    val project = createProject(
        languageType = LanguageType.JAVA,
        projectType = ProjectType.APP
    )

    include(applyPluginTest(project))

    include(createExtensionTest(project, ProjectType.APP))

    include(applyJavaPluginTest(project))
    include(applyCommonPluginsTest(project))
    include(applySpringPluginsTest(project))
    include(applyLombokPluginTest(project))
    include(doNotApplyGroovyPluginTest(project))
    include(doNotApplyKotlinPluginsTest(project))
    include(doNotApplyLibraryPluginTest(project))
    include(doNotApplyKotlinSpringPluginsTest(project))

    include(configureConventionsTest(project))

    include(addRepositoriesTest(project))

    include(addSpringDependenciesTest(project))
    include(addLombokDependenciesTest(project))
    include(addJunitDependenciesTest(project))
    include(addAssertjDependenciesTest(project))
    include(addMockitoDependenciesTest(project))
    include(doNotAddGroovyDependenciesTest(project))
    include(doNotAddKotlinDependenciesTest(project))
    include(doNotAddKotestDependenciesTest(project))
    include(doNotAddMockkDependenciesTest(project))

    include(configureGradleWrapperTaskTest(project))
    include(configureJavaCompileTaskTest(project))
    include(configureProcessResourcesTaskTest(project))
    include(configureDependencyUpdatesTaskTest(project))
    include(configureTestTaskTest(project))
    include(configureJacocoTestReportTaskTest(project))
    include(configureJarTaskTest(project, isEnabled = false))
    include(configureBootJarTaskTest(project, isEnabled = true))
    include(configureAfterReleaseBuildTaskTest(project))
    include(configureUploadArchivesTaskTest(project))

    include(configureJarArtifactTest(project))

    include(configureSpringBootExtensionTest(project))
    include(configureReleaseExtensionTest(project))
    include(configurePublishingExtensionTest(project))
    include(configureSigningExtensionTest(project))

    include(configureJarSigningTest(project))
})
