package io.opengood.gradle

import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import test.createProject
import test.enumeration.defaultFeatures
import test.enumeration.kotlinFeatures
import test.model.ProjectConfig
import test.spec.addApacheArtemisDependenciesTest
import test.spec.addApacheCommonsCompressDependenciesTest
import test.spec.addJacksonDependenciesTest
import test.spec.addJacksonKotlinDependenciesTest
import test.spec.addKotestDependenciesTest
import test.spec.addKotestSpringDependenciesTest
import test.spec.addKotlinCoroutinesDependenciesTest
import test.spec.addKotlinDependenciesTest
import test.spec.addMockkDependenciesTest
import test.spec.addRepositoriesTest
import test.spec.addSnakeYamlDependenciesTest
import test.spec.addSpringDependenciesTest
import test.spec.addSpringMockkDependenciesTest
import test.spec.applyCommonPluginsTest
import test.spec.applyJavaPluginTest
import test.spec.applyKotlinPluginsTest
import test.spec.applyKotlinSpringPluginTest
import test.spec.applyLibraryPluginTest
import test.spec.applyPluginTest
import test.spec.applySpringPluginsTest
import test.spec.configureAfterReleaseBuildTaskTest
import test.spec.configureBasePluginExtensionTest
import test.spec.configureBootJarTaskTest
import test.spec.configureDependencyManagementExtensionTest
import test.spec.configureDependencyResolutionStrategyTest
import test.spec.configureGradleWrapperTaskTest
import test.spec.configureJacocoTestReportTaskTest
import test.spec.configureJarTaskTest
import test.spec.configureJavaCompileTaskTest
import test.spec.configureJavaExtensionTest
import test.spec.configureKotlinCompileTaskTest
import test.spec.configureListPluginsTaskTest
import test.spec.configureMavenCentralPublishingExtensionTest
import test.spec.configureMavenPlainJavadocJarTaskTest
import test.spec.configureMavenPublishingExtensionTest
import test.spec.configureReleaseExtensionTest
import test.spec.configureTestTaskTest
import test.spec.createExtensionTest
import test.spec.doNotAddAssertjDependenciesTest
import test.spec.doNotAddGroovyDependenciesTest
import test.spec.doNotAddJunitJupiterDependenciesTest
import test.spec.doNotAddLombokDependenciesTest
import test.spec.doNotAddMockitoDependenciesTest
import test.spec.doNotApplyGroovyPluginTest
import test.spec.doNotApplyLombokPluginTest
import test.spec.doNotConfigureBootJarResolutionTest

class ConfigPluginKotlinLibTest :
    FunSpec({

        val project =
            createProject(
                ProjectConfig(
                    languageType = LanguageType.KOTLIN,
                    projectType = ProjectType.LIB,
                    features = defaultFeatures + kotlinFeatures,
                ),
            )

        include(applyPluginTest(project))

        include(createExtensionTest(project, ProjectType.LIB))

        include(doNotApplyGroovyPluginTest(project))
        include(applyJavaPluginTest(project))
        include(doNotApplyLombokPluginTest(project))
        include(applyKotlinPluginsTest(project))
        include(applyKotlinSpringPluginTest(project))
        include(applyCommonPluginsTest(project))
        include(applySpringPluginsTest(project))
        include(applyLibraryPluginTest(project))

        include(configureDependencyResolutionStrategyTest(project))
        include(doNotConfigureBootJarResolutionTest(project))

        include(addRepositoriesTest(project))

        include(doNotAddGroovyDependenciesTest(project))
        include(doNotAddLombokDependenciesTest(project))
        include(addKotlinDependenciesTest(project))
        include(addKotlinCoroutinesDependenciesTest(project))
        include(addJacksonKotlinDependenciesTest(project))
        include(addKotestDependenciesTest(project))
        include(addKotestSpringDependenciesTest(project))
        include(addMockkDependenciesTest(project))
        include(addSpringMockkDependenciesTest(project))
        include(addJacksonDependenciesTest(project))
        include(addSpringDependenciesTest(project))
        include(addApacheArtemisDependenciesTest(project))
        include(addApacheCommonsCompressDependenciesTest(project))
        include(addSnakeYamlDependenciesTest(project))
        include(doNotAddAssertjDependenciesTest(project))
        include(doNotAddJunitJupiterDependenciesTest(project))
        include(doNotAddMockitoDependenciesTest(project))

        include(configureGradleWrapperTaskTest(project))
        include(configureKotlinCompileTaskTest(project))
        include(configureJavaCompileTaskTest(project))
        include(configureTestTaskTest(project))
        include(configureJacocoTestReportTaskTest(project))
        include(configureJarTaskTest(project, isEnabled = true))
        include(configureMavenPlainJavadocJarTaskTest(project, isEnabled = false))
        include(configureBootJarTaskTest(project, isEnabled = false))
        include(configureListPluginsTaskTest(project, isEnabled = true))
        include(configureAfterReleaseBuildTaskTest(project))

        include(configureBasePluginExtensionTest(project))
        include(configureDependencyManagementExtensionTest(project))
        include(configureJavaExtensionTest(project))
        include(configureReleaseExtensionTest(project))
        include(configureMavenCentralPublishingExtensionTest(project))
        include(configureMavenPublishingExtensionTest(project))
    })
