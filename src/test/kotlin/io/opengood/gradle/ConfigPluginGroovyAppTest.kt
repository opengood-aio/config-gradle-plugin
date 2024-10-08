package io.opengood.gradle

import io.kotest.core.spec.style.FunSpec
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import test.createProject
import test.enumeration.defaultFeatures
import test.enumeration.javaFeatures
import test.model.ProjectConfig
import test.spec.addApacheCommonsCompressDependenciesTest
import test.spec.addAssertjDependenciesTest
import test.spec.addGroovyDependenciesTest
import test.spec.addJacksonDependenciesTest
import test.spec.addJunitJupiterDependenciesTest
import test.spec.addMockitoDependenciesTest
import test.spec.addRepositoriesTest
import test.spec.addSnakeYamlDependenciesTest
import test.spec.addSpringDependenciesTest
import test.spec.applyCommonPluginsTest
import test.spec.applyGroovyPluginTest
import test.spec.applyJavaPluginTest
import test.spec.applyPluginTest
import test.spec.applySpringPluginsTest
import test.spec.configureAfterReleaseBuildTaskTest
import test.spec.configureBasePluginExtensionTest
import test.spec.configureBootJarResolutionTest
import test.spec.configureBootJarTaskTest
import test.spec.configureDependencyManagementExtensionTest
import test.spec.configureDependencyResolutionStrategyTest
import test.spec.configureGradleWrapperTaskTest
import test.spec.configureJacocoTestReportTaskTest
import test.spec.configureJarTaskTest
import test.spec.configureJavaCompileTaskTest
import test.spec.configureJavaExtensionTest
import test.spec.configureListPluginsTaskTest
import test.spec.configureMavenCentralPublishingExtensionTest
import test.spec.configureMavenPlainJavadocJarTaskTest
import test.spec.configureMavenPublishingExtensionTest
import test.spec.configureProcessResourcesTaskTest
import test.spec.configureReleaseExtensionTest
import test.spec.configureSpringBootExtensionTest
import test.spec.configureTestTaskTest
import test.spec.createExtensionTest
import test.spec.doNotAddJacksonKotlinDependenciesTest
import test.spec.doNotAddKotestDependenciesTest
import test.spec.doNotAddKotestSpringDependenciesTest
import test.spec.doNotAddKotlinCoroutinesDependenciesTest
import test.spec.doNotAddKotlinDependenciesTest
import test.spec.doNotAddLombokDependenciesTest
import test.spec.doNotAddMockkDependenciesTest
import test.spec.doNotAddSpringMockkDependenciesTest
import test.spec.doNotApplyKotlinPluginsTest
import test.spec.doNotApplyKotlinSpringPluginTest
import test.spec.doNotApplyLibraryPluginTest
import test.spec.doNotApplyLombokPluginTest

class ConfigPluginGroovyAppTest :
    FunSpec({

        val project =
            createProject(
                ProjectConfig(
                    languageType = LanguageType.GROOVY,
                    projectType = ProjectType.APP,
                    features = defaultFeatures + javaFeatures,
                ),
            )

        include(applyPluginTest(project))

        include(createExtensionTest(project, ProjectType.APP))

        include(applyGroovyPluginTest(project))
        include(applyJavaPluginTest(project))
        include(doNotApplyLombokPluginTest(project))
        include(doNotApplyKotlinPluginsTest(project))
        include(doNotApplyKotlinSpringPluginTest(project))
        include(applyCommonPluginsTest(project))
        include(applySpringPluginsTest(project))
        include(doNotApplyLibraryPluginTest(project))

        include(configureDependencyResolutionStrategyTest(project))
        include(configureBootJarResolutionTest(project))

        include(addRepositoriesTest(project))

        include(addGroovyDependenciesTest(project))
        include(doNotAddLombokDependenciesTest(project))
        include(doNotAddKotlinDependenciesTest(project))
        include(doNotAddKotlinCoroutinesDependenciesTest(project))
        include(doNotAddJacksonKotlinDependenciesTest(project))
        include(doNotAddKotestDependenciesTest(project))
        include(doNotAddKotestSpringDependenciesTest(project))
        include(doNotAddMockkDependenciesTest(project))
        include(doNotAddSpringMockkDependenciesTest(project))
        include(addJacksonDependenciesTest(project))
        include(addSpringDependenciesTest(project))
        include(addApacheCommonsCompressDependenciesTest(project))
        include(addSnakeYamlDependenciesTest(project))
        include(addAssertjDependenciesTest(project))
        include(addJunitJupiterDependenciesTest(project))
        include(addMockitoDependenciesTest(project))

        include(configureGradleWrapperTaskTest(project))
        include(configureProcessResourcesTaskTest(project))
        include(configureJavaCompileTaskTest(project))
        include(configureTestTaskTest(project))
        include(configureJacocoTestReportTaskTest(project))
        include(configureJarTaskTest(project, isEnabled = false))
        include(configureMavenPlainJavadocJarTaskTest(project, isEnabled = false))
        include(configureBootJarTaskTest(project, isEnabled = true))
        include(configureListPluginsTaskTest(project, isEnabled = true))
        include(configureAfterReleaseBuildTaskTest(project))

        include(configureBasePluginExtensionTest(project))
        include(configureDependencyManagementExtensionTest(project))
        include(configureJavaExtensionTest(project))
        include(configureSpringBootExtensionTest(project))
        include(configureReleaseExtensionTest(project))
        include(configureMavenCentralPublishingExtensionTest(project))
        include(configureMavenPublishingExtensionTest(project))
    })
