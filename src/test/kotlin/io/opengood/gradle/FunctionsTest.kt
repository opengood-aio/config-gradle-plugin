package io.opengood.gradle

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.system.withEnvironment
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.opengood.gradle.enumeration.BuildGradleType
import io.opengood.gradle.enumeration.LanguageType
import test.createProjectBuildGradle
import test.createProjectDir
import java.io.File

class FunctionsTest :
    FunSpec({

        test("getEnvOrDefault returns environment variable value when variable exists") {
            withEnvironment("NAME", "test") {
                getEnvOrDefault("NAME", "none") shouldBe "test"
            }
        }

        test("getEnvOrDefault returns default value when variable does not exist") {
            getEnvOrDefault("NONE", "none") shouldBe "none"
        }

        test("getPathAsFile returns paths as file object") {
            getPathAsFile("/dir", "file.txt") shouldBe File("/dir/file.txt")
        }

        test("hasPath returns true when directory and enumeration as file exists") {
            val projectDir = createProjectDir()
            createProjectBuildGradle(LanguageType.KOTLIN, projectDir)

            hasPath(projectDir.toFile(), BuildGradleType.KOTLIN_DSL).shouldBeTrue()
        }

        test("hasPath returns false when directory and enumeration as file do not exist") {
            val projectDir = createProjectDir()

            hasPath(projectDir.toFile(), BuildGradleType.KOTLIN_DSL).shouldBeFalse()
        }

        test("hasPath returns true when directory and file exists") {
            val projectDir = createProjectDir()
            createProjectBuildGradle(LanguageType.KOTLIN, projectDir)

            hasPath(projectDir.toFile(), listOf(BuildGradleType.KOTLIN_DSL.toString())).shouldBeTrue()
        }

        test("hasPath returns false when directory and file do not exist") {
            val projectDir = createProjectDir()

            hasPath(projectDir.toFile(), listOf(BuildGradleType.KOTLIN_DSL.toString())).shouldBeFalse()
        }
    })
