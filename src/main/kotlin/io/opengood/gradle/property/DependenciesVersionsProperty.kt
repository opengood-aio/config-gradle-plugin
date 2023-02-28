package io.opengood.gradle.property

import io.opengood.gradle.constant.Boms
import io.opengood.gradle.constant.Dependencies
import io.opengood.gradle.constant.Resources
import java.util.Properties

class DependenciesVersionsProperty {

    private val mappings = mapOf(
        Dependencies.ASSERTJ to listOf(
            Dependencies.ASSERTJ_CORE,
        ),
        Dependencies.JUNIT_JUPITER to listOf(
            Dependencies.JUNIT_JUPITER_ALL,
        ),
        Dependencies.KOTEST to listOf(
            Dependencies.KOTEST_RUNNER,
            Dependencies.KOTEST_ASSERTIONS,
            Dependencies.KOTEST_EXTENSIONS,
            Dependencies.KOTEST_PROPERTIES,
        ),
        Dependencies.KOTEST_SPRING to listOf(
            Dependencies.KOTEST_SPRING_EXTENSIONS,
        ),
        Dependencies.KOTLIN to listOf(
            Boms.KOTLIN,
            Dependencies.KOTLIN_REFLECT,
            Dependencies.KOTLIN_STD_LIB,
            Dependencies.KOTLIN_TEST,
        ),
        Dependencies.KOTLIN_COROUTINES to listOf(
            Dependencies.KOTLIN_COROUTINES_CORE,
        ),
        Dependencies.MOCKITO to listOf(
            Dependencies.MOCKITO_CORE,
            Dependencies.MOCKITO_JUNIT_JUPITER,
        ),
        Dependencies.MOCKK to listOf(
            Dependencies.MOCKK_ALL,
        ),
    )

    private val properties = Properties()

    init {
        val file = this::class.java.classLoader.getResourceAsStream(Resources.VERSIONS_PROPERTIES)
        properties.load(file)
    }

    fun getDependencyAndVersion(groupAndName: String) =
        "$groupAndName:${getVersion(groupAndName)}"

    fun getVersion(groupAndName: String): String {
        val key = formatPropertyKey(mapPropertyKey(groupAndName))
        return if (properties.containsKey(key)) {
            properties.getProperty(key)
        } else {
            throw IllegalStateException("Unable to find version for dependency: '$groupAndName'")
        }
    }

    private fun formatPropertyKey(groupAndName: String): String =
        DEPENDENCY_KEY_PREFIX + groupAndName.replace(DEPENDENCY_ATTRIBUTE_SEPARATOR, DEPENDENCY_KEY_SEPARATOR)

    private fun mapPropertyKey(groupAndName: String): String =
        mappings
            .filter { it.value.contains(groupAndName) }
            .map { it.key }
            .firstOrNull() ?: groupAndName

    companion object {
        const val DEPENDENCY_ATTRIBUTE_SEPARATOR = ":"
        const val DEPENDENCY_KEY_PREFIX = "version."
        const val DEPENDENCY_KEY_SEPARATOR = ".."
    }
}
