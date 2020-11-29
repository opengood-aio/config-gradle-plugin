package io.opengood.gradle

import helper.createProject
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.extensions.system.withEnvironment
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.extension.OpenGoodExtension
import io.opengood.gradle.extension.opengood
import org.gradle.testfixtures.ProjectBuilder

class FunctionsTest : WordSpec({

    "Plugin Functions and Properties" should {
        "getEnvVar returns environment variable value when variable exists" {
            withEnvironment("NAME", "test") {
                getEnvVar("NAME", "none") shouldBe "test"
            }
        }

        "getEnvVar returns default value when variable does not exist" {
            getEnvVar("NONE", "none") shouldBe "none"
        }

        "getExtension returns Gradle extension given name" {
            val project = createProject(languageType = LanguageType.KOTLIN)

            val extension = project.getExtension<OpenGoodExtension>(OpenGoodExtension.EXTENSION_NAME)

            extension shouldBeSameInstanceAs project.opengood()
        }

        "getExtension returns Gradle extension given type" {
            val project = createProject(languageType = LanguageType.KOTLIN)

            val extension = project.getExtension<OpenGoodExtension>()

            extension shouldBeSameInstanceAs project.opengood()
        }

        "getProperty returns property value when property exists" {
            val project = createProject(languageType = LanguageType.KOTLIN)

            project.getProperty("name", "none") shouldBe "test"
        }

        "getProperty returns default value when property does not exist" {
            val project = createProject(languageType = LanguageType.KOTLIN)

            project.getProperty("none", "none") shouldBe "none"
        }

        "isGroovy returns true for Groovy Gradle project" {
            val project = createProject(languageType = LanguageType.GROOVY)

            project.isGroovy.shouldBeTrue()
        }

        "isGroovy returns false for non-Groovy Gradle project" {
            val project = createProject(languageType = LanguageType.KOTLIN)

            project.isGroovy.shouldBeFalse()
        }

        "isJava returns true for Java Gradle project" {
            val project = createProject(languageType = LanguageType.JAVA)

            project.isJava.shouldBeTrue()
        }

        "isJava returns false for non-Java Gradle project" {
            val project = createProject(languageType = LanguageType.KOTLIN)

            project.isJava.shouldBeFalse()
        }

        "isKotlin returns true for Kotlin Gradle project" {
            val project = createProject(languageType = LanguageType.KOTLIN)

            project.isKotlin.shouldBeTrue()
        }

        "isKotlin returns false for non-Kotlin Gradle project" {
            val project = createProject(languageType = LanguageType.JAVA)

            project.isKotlin.shouldBeFalse()
        }

        "languageType returns Groovy for Groovy Gradle project" {
            val project = createProject(languageType = LanguageType.GROOVY)

            project.languageType shouldBe LanguageType.GROOVY
        }

        "languageType returns Java for Java Gradle project" {
            val project = createProject(languageType = LanguageType.JAVA)

            project.languageType shouldBe LanguageType.JAVA
        }

        "languageType returns Kotlin for Kotlin Gradle project" {
            val project = createProject(languageType = LanguageType.KOTLIN)

            project.languageType shouldBe LanguageType.KOTLIN
        }

        "languageType throws exception when language cannot be detected for Gradle project" {
            val project = ProjectBuilder.builder().build()

            shouldThrow<IllegalStateException> { project.languageType }
        }
    }
})