package io.opengood.gradle

import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import spec.*

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
    include(doNotApplyKotlinPluginTest(project))
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
    include(configureJarTaskTest(project, isEnabled = false))
    include(configureBootJarTaskTest(project, isEnabled = true))
    include(configureUploadArchivesTaskTest(project))

    include(configureJarArtifactTest(project))

    include(configureSpringBootExtensionTest(project))
    include(configurePublishingExtensionTest(project))
    include(configureSigningExtensionTest(project))

    include(configureJarSigningTest(project))
})
