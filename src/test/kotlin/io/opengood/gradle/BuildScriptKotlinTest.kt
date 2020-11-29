package io.opengood.gradle

import helper.createProjectDir
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.specs.WordSpec
import io.opengood.gradle.enumeration.LanguageType
import org.gradle.testkit.runner.GradleRunner

class BuildScriptKotlinTest : WordSpec({

    "Gradle Kotlin DSL build script with configured plugin and properties" should {
        "Lead to successful build " {
            val projectDir = createProjectDir(LanguageType.KOTLIN)
            val buildScript = projectDir.resolve("build.gradle.kts").toFile()
            buildScript.writeText("""
                plugins {
                    id("io.opengood.gradle.config")
                }
              """.trimIndent())

            val result = GradleRunner.create()
                .withProjectDir(projectDir.toFile())
                .withPluginClasspath()
                .withArguments("--info", "--stacktrace")
                .build()

            result.output.shouldContain("BUILD SUCCESSFUL")
        }
    }
})