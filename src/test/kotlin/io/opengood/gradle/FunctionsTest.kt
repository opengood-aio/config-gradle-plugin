package io.opengood.gradle

import helper.createProject
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.extension.OpenGoodExtension
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class FunctionsTest {

    @Test
    fun `getExtensionByName function returns Gradle extension given name`() {
        val project = createProject(languageType = LanguageType.KOTLIN)

        val extension = getExtensionByName<OpenGoodExtension>(project, OpenGoodExtension.EXTENSION_NAME)

        assertThat(extension).isInstanceOf(OpenGoodExtension::class.java)
    }

    @Test
    fun `getExtensionByType function returns Gradle extension given type`() {
        val project = createProject(languageType = LanguageType.KOTLIN)

        val extension = getExtensionByType<OpenGoodExtension>(project)

        assertThat(extension).isInstanceOf(OpenGoodExtension::class.java)
    }

    @Test
    fun `getLanguageType function returns Groovy for Groovy Gradle project`() {
        val project = createProject(languageType = LanguageType.GROOVY)

        assertThat(getLanguageType(project)).isEqualTo(LanguageType.GROOVY)
    }

    @Test
    fun `getLanguageType function returns Java for Java Gradle project`() {
        val project = createProject(languageType = LanguageType.JAVA)

        assertThat(getLanguageType(project)).isEqualTo(LanguageType.JAVA)
    }

    @Test
    fun `getLanguageType function returns Kotlin for Kotlin Gradle project`() {
        val project = createProject(languageType = LanguageType.KOTLIN)

        assertThat(getLanguageType(project)).isEqualTo(LanguageType.KOTLIN)
    }

    @Test
    fun `getLanguageType function throws exception when language cannot be detected for Gradle project`() {
        val project = ProjectBuilder.builder().build()

        assertThrows(IllegalStateException::class.java) { getLanguageType(project) }
    }

    @Test
    fun `getProperty function returns property value when property exists`() {
        val project = createProject(languageType = LanguageType.KOTLIN)

        assertThat(getProperty(project, "name", "none")).isEqualTo("test")
    }

    @Test
    fun `getProperty function returns default value when property does not exist`() {
        val project = createProject(languageType = LanguageType.KOTLIN)

        assertThat(getProperty(project, "none", "none")).isEqualTo("none")
    }

    @Test
    fun `isGroovyProject function returns true for Groovy Gradle project`() {
        val project = createProject(languageType = LanguageType.GROOVY)

        assertThat(isGroovyProject(project)).isTrue
    }

    @Test
    fun `isGroovyProject function returns false for non-Groovy Gradle project`() {
        val project = createProject(languageType = LanguageType.KOTLIN)

        assertThat(isGroovyProject(project)).isFalse
    }

    @Test
    fun `isJavaProject function returns true for Java Gradle project`() {
        val project = createProject(languageType = LanguageType.JAVA)

        assertThat(isJavaProject(project)).isTrue
    }

    @Test
    fun `isJavaProject function returns false for non-Java Gradle project`() {
        val project = createProject(languageType = LanguageType.KOTLIN)

        assertThat(isJavaProject(project)).isFalse
    }

    @Test
    fun `isKotlinProject function returns true for Kotlin Gradle project`() {
        val project = createProject(languageType = LanguageType.KOTLIN)

        assertThat(isKotlinProject(project)).isTrue
    }

    @Test
    fun `isKotlinProject function returns false for non-Kotlin Gradle project`() {
        val project = createProject(languageType = LanguageType.JAVA)

        assertThat(isKotlinProject(project)).isFalse
    }

    @Test
    fun `transform function transforms list of source items into mutable list of converted items`() {
        val source = listOf(1, 2, 3, 4, 5)

        val converted = transform(source, fun(i: Int) : String = i.toString())

        assertThat(converted).isEqualTo(listOf("1", "2", "3", "4", "5"))
    }
}