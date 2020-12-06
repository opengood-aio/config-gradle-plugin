package spec

import helper.createProjectDir
import helper.getBuildGradleFile
import io.kotest.core.spec.style.wordSpec
import io.kotest.matchers.string.shouldContain
import io.opengood.gradle.enumeration.LanguageType
import org.gradle.testkit.runner.GradleRunner

fun buildScriptTest(languageType: LanguageType) = wordSpec {

    "Gradle ${languageType.toString().toLowerCase().capitalize()} DSL build script with configured plugin" should {
        "Lead to successful build " {
            val projectDir = createProjectDir(languageType)
            val buildScript = projectDir.resolve(getBuildGradleFile(languageType)).toFile()
            buildScript.writeText("""
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
                    test {
                        maxParallelForks = 1
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
              """.trimIndent())

            val result = GradleRunner.create()
                .withProjectDir(projectDir.toFile())
                .withPluginClasspath()
                .withArguments("--info", "--stacktrace")
                .build()

            result.output.shouldContain("BUILD SUCCESSFUL")
        }
    }
}
