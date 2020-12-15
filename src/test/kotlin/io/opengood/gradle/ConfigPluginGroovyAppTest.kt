package io.opengood.gradle

import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import spec.*

class ConfigPluginGroovyAppTest : FunSpec({

    val project = createProject(
        languageType = LanguageType.GROOVY,
        projectType = ProjectType.APP
    )

    include(applyPluginTest(project))

    include(createExtensionTest(project, ProjectType.APP))

    include(applyGroovyPluginTest(project))
    include(applyJavaPluginTest(project))
    include(applyCommonPluginsTest(project))
    include(applySpringPluginsTest(project))
    include(doNotApplyKotlinPluginsTest(project))
    include(doNotApplyLibraryPluginTest(project))
    include(doNotApplyKotlinSpringPluginsTest(project))
    include(doNotApplyLombokPluginTest(project))

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

    include(configureGradleWrapperTaskTest(project))
    include(configureProcessResourcesTaskTest(project))
    include(configureJavaCompileTaskTest(project))
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
