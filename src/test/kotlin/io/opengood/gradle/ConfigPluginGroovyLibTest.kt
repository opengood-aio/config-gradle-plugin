package io.opengood.gradle

import helper.createProject
import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import spec.*

class ConfigPluginGroovyLibTest : FunSpec({

    val project = createProject(
        languageType = LanguageType.GROOVY,
        projectType = ProjectType.LIB
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
    include(configureJavaCompileTaskTest(project))
    include(configureDependencyUpdatesTaskTest(project))
    include(configureTestTaskTest(project))
    include(configureJarTaskTest(project, isEnabled = true))
    include(configureBootJarTaskTest(project, isEnabled = false))
    include(configureUploadArchivesTaskTest(project))

    include(configureSourcesJarArtifactTest(project))
    include(configureJavadocJarArtifactTest(project))
    include(configureJarArtifactTest(project))

    include(configurePublishingExtensionTest(project))
    include(configureSigningExtensionTest(project))

    include(configureSourcesJarSigningTest(project))
    include(configureJavadocJarSigningTest(project))
    include(configureJarSigningTest(project))
})
