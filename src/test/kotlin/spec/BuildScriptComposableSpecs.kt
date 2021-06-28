package spec

import helper.createProjectDir
import helper.createProjectSrcDir
import helper.getBuildGradleFile
import helper.getSettingsGradleFile
import io.kotest.core.spec.style.wordSpec
import io.kotest.matchers.string.shouldContain
import io.opengood.gradle.enumeration.LanguageType
import io.opengood.gradle.extension.OpenGoodExtension
import org.gradle.testkit.runner.GradleRunner

fun buildScriptTest(languageType: LanguageType) = wordSpec {

    "Gradle ${languageType.toString().toLowerCase().capitalize()} DSL build script with configured plugin" should {
        "Lead to successful build" {
            val projectDir = createProjectDir()
            createProjectSrcDir(languageType, projectDir)

            val settingsGradleFile = projectDir.resolve(getSettingsGradleFile(languageType)).toFile()
            settingsGradleFile.writeText(
                """
                plugins {
                    id("io.opengood.gradle.config")
                }
                """.trimIndent()
            )

            val buildGradleFile = projectDir.resolve(getBuildGradleFile(languageType)).toFile()
            buildGradleFile.writeText(
                """
                import io.opengood.gradle.enumeration.PackagingType
                import io.opengood.gradle.enumeration.ProjectType
                import io.opengood.gradle.enumeration.ScmProvider
                
                plugins {
                    id("io.opengood.gradle.config")
                }
                
                opengood {
                    main {
                        projectType = ProjectType.APP
                    }
                    features {
                        publishing = false
                        assertj = false
                        jacksonKotlin = false
                        junitJupiter = false
                        kotest = false
                        kotestSpring = false
                        kotlinCoroutines = false
                        lombok = false
                        mockito = false
                        mockk = false
                        spring = false
                        springMockk = false
                    }
                    test {
                        maxParallelForks = 1
                        frameworks {
                            java = true
                        }
                    }
                    release {
                        afterReleaseBuildTasks = ${if (languageType == LanguageType.KOTLIN) "arrayOf<Any>(\"publishPlugins\")" else "[\"publishPlugins\"]"}
                        requireBranch = "main"
                        pushToRemote = "origin"
                    }
                    artifact {
                        name = "test"
                        packaging = PackagingType.JAR
                        description = "description"
                        uri = "https://artifact.uri"
                        repo {
                            snapshotsRepoUri = "https://snapshots.uri"
                            stagingRepoUri = "https://staging.uri"
                        }
                        scm {
                            provider = ScmProvider.GIT
                            connection = "scm:git:https://repo.uri/project"
                            developerConnection = "scm:git:https://repo.uri/project"
                            uri = "https://repo.uri/project"
                        }
                        license {
                            name = "MIT"
                            uri = "https://repo.uri/project/LICENSE"
                        }
                        developer {
                            id = "developer"
                            name = "Developer"
                            email = "dev@example.org"
                        }
                    }
                }
                """.trimIndent()
            )

            val result = GradleRunner.create()
                .withProjectDir(projectDir.toFile())
                .withPluginClasspath()
                .withArguments("--info", "--stacktrace")
                .build()

            println(result.output)

            result.output.shouldContain("Applying ${OpenGoodExtension.EXTENSION_NAME} settings...")
            result.output.shouldContain("Applying ${OpenGoodExtension.EXTENSION_NAME} project configuration...")
            result.output.shouldContain("BUILD SUCCESSFUL")
        }
    }
}
