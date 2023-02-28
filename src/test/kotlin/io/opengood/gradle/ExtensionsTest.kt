package io.opengood.gradle

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.opengood.gradle.constant.Versions
import io.opengood.gradle.enumeration.BuildGradleType
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.extension.OpenGoodExtension
import io.opengood.gradle.extension.openGood
import org.gradle.testfixtures.ProjectBuilder
import test.createProject
import test.model.ProjectConfig

class ExtensionsTest : FunSpec({

    test("buildGradleType returns Groovy for Groovy Gradle project") {
        val project = createProject(ProjectConfig(languageType = LanguageType.GROOVY))

        project.buildGradleType shouldBe BuildGradleType.GROOVY_DSL
    }

    test("buildGradleType returns Groovy for Java Gradle project") {
        val project = createProject(ProjectConfig(languageType = LanguageType.JAVA))

        project.buildGradleType shouldBe BuildGradleType.GROOVY_DSL
    }

    test("buildGradleType returns Kotlin for Kotlin Gradle project") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN))

        project.buildGradleType shouldBe BuildGradleType.KOTLIN_DSL
    }

    test("dependenciesVersions returns dependencies versions property") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN))

        project.dependenciesVersions.shouldNotBeNull()
    }

    test("getExtension returns Gradle extension given name") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN))

        val extension = project.getExtension<OpenGoodExtension>(OpenGoodExtension.EXTENSION_NAME)

        extension shouldBeSameInstanceAs project.openGood()
    }

    test("getExtension returns Gradle extension given type") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN))

        val extension = project.getExtension<OpenGoodExtension>()

        extension shouldBeSameInstanceAs project.openGood()
    }

    test("getPropertyOrDefault returns property value when property exists") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN))

        project.getPropertyOrDefault("name", "none") shouldBe "test"
    }

    test("getPropertyOrDefault returns default value when property does not exist") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN))

        project.getPropertyOrDefault("none", "none") shouldBe "none"
    }

    test("isGroovy returns true when Groovy source directory exists") {
        val project = createProject(ProjectConfig(languageType = LanguageType.GROOVY))

        project.isGroovy.shouldBeTrue()
    }

    test("isGroovy returns false when Groovy source directory does not exist") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN))

        project.isGroovy.shouldBeFalse()
    }

    test("isGroovyDsl returns true for Groovy Gradle project when Groovy source directory and build Gradle file exist") {
        val project = createProject(ProjectConfig(languageType = LanguageType.GROOVY))

        project.isGroovyDsl.shouldBeTrue()
    }

    test("isGroovyDsl returns false for non-Groovy Gradle project when Groovy source directory and build Gradle file do not exist") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN))

        project.isGroovyDsl.shouldBeFalse()
    }

    test("isJava returns true for Java Gradle project when Java source directory and Groovy build Gradle file exist") {
        val project = createProject(ProjectConfig(languageType = LanguageType.JAVA))

        project.isJava.shouldBeTrue()
    }

    test("isJava returns true for Java Gradle project when Java source directory exists") {
        val project = createProject(ProjectConfig(languageType = LanguageType.JAVA, buildGradle = false))

        project.isJava.shouldBeTrue()
    }

    test("isJava returns true for Java Gradle project when Groovy build Gradle file exists") {
        val project = createProject(ProjectConfig(languageType = LanguageType.JAVA, srcDir = false))

        project.isJava.shouldBeTrue()
    }

    test("isJava returns false for non-Java Gradle project when Groovy build Gradle file does not exist") {
        val project = createProject(ProjectConfig(languageType = LanguageType.GROOVY, buildGradle = false, settingsGradle = false))

        project.isJava.shouldBeFalse()
    }

    test("isKotlin returns true when Kotlin source directory exists") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN))

        project.isKotlin.shouldBeTrue()
    }

    test("isKotlin returns false when Kotlin source directory does not exist") {
        val project = createProject(ProjectConfig(languageType = LanguageType.JAVA))

        project.isKotlin.shouldBeTrue()
    }

    test("isKotlinDsl returns true for Kotlin Gradle project when Kotlin source directory and build Gradle file exist") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN, buildGradle = false))

        project.isKotlinDsl.shouldBeTrue()
    }

    test("isKotlinDsl returns true for Kotlin Gradle project when Kotlin source directory exists") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN, buildGradle = false))

        project.isKotlinDsl.shouldBeTrue()
    }

    test("isKotlinDsl returns true for Kotlin Gradle project when Kotlin build Gradle file exists") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN, srcDir = false))

        project.isKotlinDsl.shouldBeTrue()
    }

    test("isKotlinDsl returns false for non-Kotlin Gradle project when Kotlin source directory and build Gradle file do not exist") {
        val project = createProject(ProjectConfig(languageType = LanguageType.JAVA))

        project.isKotlinDsl.shouldBeFalse()
    }

    test("isSnapshotVersion returns true when project version property contains ${Versions.SNAPSHOT}") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN))

        project.isSnapshotVersion.shouldBeTrue()
    }

    test("isSnapshotVersion returns false when project version property does not contain ${Versions.SNAPSHOT}") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN, version = "1.0.0"))

        project.isSnapshotVersion.shouldBeFalse()
    }

    test("languageType returns Groovy for Groovy Gradle project when Groovy source directory and build Gradle file exist") {
        val project = createProject(ProjectConfig(languageType = LanguageType.GROOVY))

        project.languageType shouldBe LanguageType.GROOVY
    }

    test("languageType returns Groovy for Groovy Gradle project when Groovy source directory exists") {
        val project = createProject(ProjectConfig(languageType = LanguageType.GROOVY, buildGradle = false))

        project.languageType shouldBe LanguageType.GROOVY
    }

    test("languageType returns Java for Java Gradle project when Java source directory and Groovy build Gradle file exist") {
        val project = createProject(ProjectConfig(languageType = LanguageType.JAVA))

        project.languageType shouldBe LanguageType.JAVA
    }

    test("languageType returns Java for Java Gradle project when Groovy build Gradle file exist") {
        val project = createProject(ProjectConfig(languageType = LanguageType.JAVA, srcDir = false))

        project.languageType shouldBe LanguageType.JAVA
    }

    test("languageType returns Java for Java Gradle project when Java source directory exists") {
        val project = createProject(ProjectConfig(languageType = LanguageType.JAVA, buildGradle = false))

        project.languageType shouldBe LanguageType.JAVA
    }

    test("languageType returns Kotlin for Kotlin Gradle project when Kotlin source directory and build Gradle file exist") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN))

        project.languageType shouldBe LanguageType.KOTLIN
    }

    test("languageType returns Kotlin for Kotlin Gradle project when Kotlin source directory exists") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN, buildGradle = false))

        project.languageType shouldBe LanguageType.KOTLIN
    }

    test("languageType returns Kotlin for Kotlin Gradle project when Kotlin build Gradle file exists") {
        val project = createProject(ProjectConfig(languageType = LanguageType.KOTLIN, srcDir = false))

        project.languageType shouldBe LanguageType.KOTLIN
    }

    test("languageType throws exception when language cannot be detected for Gradle project") {
        val project = ProjectBuilder.builder().build()

        shouldThrow<IllegalStateException> { project.languageType }
    }
})
