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

class ConfigPluginKotlinAppTest : FunSpec({

    val project = createProject(
        languageType = LanguageType.KOTLIN,
        projectType = ProjectType.APP
    )

    val properties = mapOf(
        "projectType" to ProjectType.APP
    )

    include(applyPluginTest(project))

    include(createExtensionTest(project, ProjectType.APP))

    include(applyCommonPluginsTest(project))

    test("Applies specific plugins") {
        getPlugin(project, Plugins.KOTLIN).shouldNotBeNull()
        getPlugin(project, Plugins.KOTLIN_SPRING).shouldNotBeNull()
    }

    include(configureConventionsTest(project))

    include(addRepositoriesTest(project))

    include(addCommonDependenciesTest(project))

    test("Adds specific dependencies") {
        getDependency(project, "implementation", Dependencies.KOTLIN_STD_LIB).shouldNotBeNull()
        getDependency(project, "implementation", Dependencies.KOTLIN_REFLECT).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTEST_JUNIT_RUNNER).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTEST_JUNIT_EXTENSIONS).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTEST_SPRING_EXTENSIONS).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTEST_KOIN_EXTENSIONS).shouldNotBeNull()
        getDependency(project, "testImplementation", Dependencies.MOCKK).shouldNotBeNull()
    }

    include(configureGradleWrapperTaskTest(project))

    include(configureKotlinCompileTaskTest(project))

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
