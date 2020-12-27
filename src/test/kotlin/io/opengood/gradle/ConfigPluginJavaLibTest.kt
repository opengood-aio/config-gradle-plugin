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
import spec.applyLibraryPluginTest
import spec.applyLombokPluginTest
import spec.applyPluginTest
import spec.applySpringPluginsTest
import spec.configureAfterReleaseBuildTaskTest
import spec.configureBootJarTaskTest
import spec.configureConventionsTest
import spec.configureDependencyUpdatesTaskTest
import spec.configureGradleWrapperTaskTest
import spec.configureJarArtifactTest
import spec.configureJarSigningTest
import spec.configureJarTaskTest
import spec.configureJavaCompileTaskTest
import spec.configureJavadocJarArtifactTest
import spec.configureJavadocJarSigningTest
import spec.configurePublishingExtensionTest
import spec.configureReleaseExtensionTest
import spec.configureSigningExtensionTest
import spec.configureSourcesJarArtifactTest
import spec.configureSourcesJarSigningTest
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

class ConfigPluginJavaLibTest : FunSpec({

    val project = createProject(
        languageType = LanguageType.JAVA,
        projectType = ProjectType.LIB
    )

    include(applyPluginTest(project))

    include(createExtensionTest(project, ProjectType.LIB))

    include(applyJavaPluginTest(project))
    include(applyCommonPluginsTest(project))
    include(applyLibraryPluginTest(project))
    include(applySpringPluginsTest(project))
    include(applyLombokPluginTest(project))
    include(doNotApplyGroovyPluginTest(project))
    include(doNotApplyKotlinPluginsTest(project))
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
    include(configureDependencyUpdatesTaskTest(project))
    include(configureTestTaskTest(project))
    include(configureJarTaskTest(project, isEnabled = true))
    include(configureBootJarTaskTest(project, isEnabled = false))
    include(configureAfterReleaseBuildTaskTest(project))
    include(configureUploadArchivesTaskTest(project))

    include(configureSourcesJarArtifactTest(project))
    include(configureJavadocJarArtifactTest(project))
    include(configureJarArtifactTest(project))

    include(configureReleaseExtensionTest(project))
    include(configurePublishingExtensionTest(project))
    include(configureSigningExtensionTest(project))

    include(configureSourcesJarSigningTest(project))
    include(configureJavadocJarSigningTest(project))
    include(configureJarSigningTest(project))
})
