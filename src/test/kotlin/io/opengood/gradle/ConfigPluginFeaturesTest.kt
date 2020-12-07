package io.opengood.gradle

import helper.createProject
import helper.getDependency
import helper.getPlugin
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.opengood.gradle.constant.Dependencies
import io.opengood.gradle.constant.Plugins
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.ProjectType
import org.gradle.api.plugins.UnknownPluginException

class ConfigPluginFeaturesTest : FunSpec({

    val project = createProject(
        languageType = LanguageType.KOTLIN,
        projectType = ProjectType.LIB,
        features = 0
    )

    test("Disables feature plugins") {
        shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.KOTLIN_SPRING) }
        shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.LOMBOK) }
        shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.SPRING_BOOT) }
        shouldThrow<UnknownPluginException> { getPlugin(project, Plugins.SPRING_DEPENDENCY_MANAGEMENT) }
    }

    test("Disables feature dependencies") {
        getDependency(project, "implementation", Dependencies.LOMBOK).shouldBeNull()
        getDependency(project, "implementation", Dependencies.SPRING_BOOT_STARTER).shouldBeNull()
        getDependency(project, "annotationProcessor", Dependencies.LOMBOK).shouldBeNull()
        getDependency(project, "annotationProcessor", Dependencies.SPRING_BOOT_CONFIG_PROCESSOR).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.LOMBOK).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.SPRING_BOOT_STARTER_TEST).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.JUNIT_JUPITER).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.ASSERTJ).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.MOCKITO).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.MOCKITO_JUNIT_JUPITER).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTEST_JUNIT_RUNNER).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTEST_JUNIT_EXTENSIONS).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTEST_SPRING_EXTENSIONS).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.KOTEST_KOIN_EXTENSIONS).shouldBeNull()
        getDependency(project, "testImplementation", Dependencies.MOCKK).shouldBeNull()
        getDependency(project, "testAnnotationProcessor", Dependencies.LOMBOK).shouldBeNull()
    }
})
