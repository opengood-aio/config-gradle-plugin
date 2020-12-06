package io.opengood.gradle

import helper.createProject
import helper.getDependency
import helper.getPlugin
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.opengood.gradle.constant.Dependencies
import io.opengood.gradle.constant.Plugins
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import spec.*

class ConfigPluginJavaLibTest : FunSpec({

    val project = createProject(
        languageType = LanguageType.JAVA,
        projectType = ProjectType.LIB
    )

    val properties = mapOf(
        "projectType" to ProjectType.LIB
    )

    include(applyPluginTest(project))

    include(createExtensionTest(project, ProjectType.LIB))

    include(applyCommonPluginsTest(project))

    test("Applies specific plugins") {
        getPlugin(project, Plugins.JAVA).shouldNotBeNull()
        getPlugin(project, Plugins.JAVA_LIBRARY).shouldNotBeNull()
        getPlugin(project, Plugins.LOMBOK).shouldNotBeNull()
    }

    include(configureConventionsTest(project))

    include(addRepositoriesTest(project))

    include(addCommonDependenciesTest(project))

    test("Adds specific dependencies") {
        getDependency(project, "implementation", Dependencies.LOMBOK).shouldNotBeNull()
        getDependency(project, "annotationProcessor", Dependencies.LOMBOK).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.LOMBOK).shouldNotBeNull()
        getDependency(project, "testAnnotationProcessor", Dependencies.LOMBOK).shouldNotBeNull()
    }

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
