package io.opengood.gradle

import helper.createProject
import io.opengood.gradle.enumeration.LanguageType
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class FunctionsTest {

    @Test
    fun `getLanguageType function returns Groovy for Groovy Gradle project`() {
        val project = createProject(LanguageType.GROOVY)

        assertThat(getLanguageType(project)).isEqualTo(LanguageType.GROOVY)
    }

    @Test
    fun `getLanguageType function returns Java for Java Gradle project`() {
        val project = createProject(LanguageType.JAVA)

        assertThat(getLanguageType(project)).isEqualTo(LanguageType.JAVA)
    }

    @Test
    fun `getLanguageType function returns Kotlin for Kotlin Gradle project`() {
        val project = createProject(LanguageType.KOTLIN)

        assertThat(getLanguageType(project)).isEqualTo(LanguageType.KOTLIN)
    }

    @Test
    fun `getLanguageType function throws exception when language cannot be detected for Gradle project`() {
        val project = ProjectBuilder.builder().build()

        assertThrows(IllegalStateException::class.java) { getLanguageType(project) }
    }

    @Test
    fun `isGroovyProject function returns true for Groovy Gradle project`() {
        val project = createProject(LanguageType.GROOVY)

        assertThat(isGroovyProject(project)).isTrue
    }

    @Test
    fun `isGroovyProject function returns false for non-Groovy Gradle project`() {
        val project = createProject(LanguageType.KOTLIN)

        assertThat(isGroovyProject(project)).isFalse
    }

    @Test
    fun `isJavaProject function returns true for Java Gradle project`() {
        val project = createProject(LanguageType.JAVA)

        assertThat(isJavaProject(project)).isTrue
    }

    @Test
    fun `isJavaProject function returns false for non-Java Gradle project`() {
        val project = createProject(LanguageType.KOTLIN)

        assertThat(isJavaProject(project)).isFalse
    }

    @Test
    fun `isKotlinProject function returns true for Kotlin Gradle project`() {
        val project = createProject(LanguageType.KOTLIN)

        assertThat(isKotlinProject(project)).isTrue
    }

    @Test
    fun `isKotlinProject function returns false for non-Kotlin Gradle project`() {
        val project = createProject(LanguageType.JAVA)

        assertThat(isKotlinProject(project)).isFalse
    }
}