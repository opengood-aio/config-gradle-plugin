package com.gaig.cnd.gradle

import helper.LanguageType
import helper.createProjectDir
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.specs.WordSpec
import org.gradle.testkit.runner.GradleRunner

class BuildScriptGroovyTest : WordSpec({

    "A Groovy DSL build script with configured plugin ID and properties" should {
        "Lead to a successful build " {
            val projectDir = createProjectDir(LanguageType.GROOVY)
            val buildScript = projectDir.resolve("build.gradle").toFile()
            buildScript.writeText("""
                plugins {
                    id 'com.gaig.cnd.gradle.config'
                }
                
                cnd {
                    testMaxParallelForks = 1
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