package io.opengood.gradle

import io.opengood.gradle.constant.Versions
import io.opengood.gradle.enumeration.BuildGradleType
import io.opengood.gradle.enumeration.Feature
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.enumeration.SettingsGradleType
import io.opengood.gradle.enumeration.SrcDirType
import io.opengood.gradle.property.DependenciesVersionsProperty
import org.gradle.api.Project

internal val Project.buildGradleType: BuildGradleType
    get() =
        when {
            isGroovyDsl -> BuildGradleType.GROOVY_DSL
            isKotlinDsl -> BuildGradleType.KOTLIN_DSL
            else -> throw IllegalStateException("Unable to detect BuildGradleType from Gradle project")
        }

internal val Project.dependenciesVersions: DependenciesVersionsProperty
    get() = DependenciesVersionsProperty()

internal fun Project.determineFeatureEnabled(feature: Feature): Boolean =
    when (feature) {
        Feature.JACKSON,
        Feature.SPRING,
        -> true

        else -> {
            if (!isKotlin) {
                when (feature) {
                    Feature.ASSERTJ,
                    Feature.JUNIT_JUPITER,
                    Feature.LOMBOK,
                    Feature.MOCKITO,
                    -> true

                    else -> false
                }
            } else {
                when (feature) {
                    Feature.JACKSON_KOTLIN,
                    Feature.KOTEST,
                    Feature.KOTEST_SPRING,
                    Feature.KOTLIN_COROUTINES,
                    Feature.MOCKK,
                    Feature.SPRING_MOCKK,
                    -> true

                    else -> false
                }
            }
        }
    }

internal inline fun <reified T : Any> Project.getExtension(): T = extensions.getByType(T::class.java)

internal inline fun <reified T : Any> Project.getExtension(name: String): T = extensions.getByName(name) as T

internal inline fun <reified V : Any> Project.getPropertyOrDefault(
    name: String,
    default: V,
): V = if (hasProperty(name)) property(name) as V else default

internal val Project.isGroovy: Boolean
    get() = hasPath(projectDir, SrcDirType.GROOVY.values)

internal val Project.isGroovyDsl: Boolean
    get() =
        hasPath(projectDir, BuildGradleType.GROOVY_DSL) ||
            hasPath(projectDir, SettingsGradleType.GROOVY_DSL)

internal val Project.isJava: Boolean
    get() = hasPath(projectDir, SrcDirType.JAVA.values) || isGroovyDsl

internal val Project.isKotlin: Boolean
    get() = hasPath(projectDir, SrcDirType.KOTLIN.values) || isKotlinDsl

internal val Project.isKotlinDsl: Boolean
    get() =
        hasPath(projectDir, BuildGradleType.KOTLIN_DSL) ||
            hasPath(projectDir, SettingsGradleType.KOTLIN_DSL)

internal val Project.isSnapshotVersion: Boolean
    get() = version.toString().endsWith(Versions.SNAPSHOT)

internal val Project.languageType: LanguageType
    get() =
        when {
            isGroovy -> LanguageType.GROOVY
            isJava -> LanguageType.JAVA
            isKotlin -> LanguageType.KOTLIN
            else -> throw IllegalStateException("Unable to detect LanguageType from Gradle project")
        }
