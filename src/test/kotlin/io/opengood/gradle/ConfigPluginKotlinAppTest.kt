package io.opengood.gradle

import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import spec.*

class ConfigPluginKotlinAppTest : FunSpec({

    val project = createProject(
        languageType = LanguageType.KOTLIN,
        projectType = ProjectType.APP
    )

    include(applyPluginTest(project))

    include(createExtensionTest(project, ProjectType.APP))

    include(applyKotlinPluginsTest(project))
    include(applyJavaPluginTest(project))
    include(applyCommonPluginsTest(project))
    include(applySpringPluginsTest(project))
    include(applyKotlinSpringPluginsTest(project))
    include(doNotApplyGroovyPluginTest(project))
    include(doNotApplyLibraryPluginTest(project))
    include(doNotApplyLombokPluginTest(project))

    include(configureConventionsTest(project))

    include(addRepositoriesTest(project))

    include(addKotlinDependenciesTest(project))
    include(addSpringDependenciesTest(project))
    include(addKotestDependenciesTest(project))
    include(addMockkDependenciesTest(project))
    include(doNotAddGroovyDependenciesTest(project))
    include(doNotAddLombokDependenciesTest(project))
    include(doNotAddJunitDependenciesTest(project))
    include(doNotAddAssertjDependenciesTest(project))
    include(doNotAddMockitoDependenciesTest(project))

    include(configureGradleWrapperTaskTest(project))
    include(configureKotlinCompileTaskTest(project))
    include(configureJavaCompileTaskTest(project))
    include(configureProcessResourcesTaskTest(project))
    include(configureDependencyUpdatesTaskTest(project))
    include(configureTestTaskTest(project))
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
