import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import net.researchgate.release.GitAdapter.GitConfig
import net.researchgate.release.ReleaseExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType
import org.gradle.internal.logging.text.StyledTextOutput
import org.gradle.internal.logging.text.StyledTextOutput.Style
import org.gradle.internal.logging.text.StyledTextOutputFactory
import org.gradle.kotlin.dsl.support.serviceOf
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.regex.Matcher

plugins {
    kotlin("jvm") version "1.4.31"
    id("java-gradle-plugin")
    id("jacoco")
    id("maven-publish")
    id("com.github.ben-manes.versions") version "0.38.0"
    id("net.researchgate.release") version "2.8.1"
    id("com.gradle.plugin-publish") version "0.13.0"
}

group = "io.opengood.gradle"

gradlePlugin {
    plugins {
        create("opengood-config") {
            id = "io.opengood.gradle.config"
            implementationClass = "io.opengood.gradle.ConfigPlugin"
            displayName = "OpenGood Config Gradle Plugin"
            description = "Gradle plugin providing centralized configuration of OpenGood Gradle projects"
        }
    }
}

val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion
val javaVersion = JavaVersion.VERSION_11
val jvmTargetVersion = "11"

object Versions {
    const val JCOLOR = "5.0.1"
    const val KOTEST = "4.4.3"
    const val LOMBOK_PLUGIN = "3.3.0"
    const val MOCKK = "1.11.0"
    const val RELEASE_PLUGIN = "2.8.1"
    const val SPRING_BOOT_PLUGIN = "2.4.4"
    const val SPRING_DEPENDENCY_MANAGEMENT_PLUGIN = "1.0.11.RELEASE"
    const val VERSIONS_PLUGIN = "0.38.0"
}

java.apply {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion(kotlinVersion)
            because("Incompatibilities with older Kotlin versions")
        }
    }
}

repositories {
    mavenCentral()
    jcenter()
    gradlePluginPortal()
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleApi())

    implementation(kotlin("gradle-plugin:$kotlinVersion"))
    implementation(kotlin("allopen:$kotlinVersion"))
    implementation(kotlin("noarg:$kotlinVersion"))

    implementation("org.springframework.boot:spring-boot-gradle-plugin:${Versions.SPRING_BOOT_PLUGIN}")
    implementation("io.spring.gradle:dependency-management-plugin:${Versions.SPRING_DEPENDENCY_MANAGEMENT_PLUGIN}")
    implementation("io.franzbecker:gradle-lombok:${Versions.LOMBOK_PLUGIN}")
    implementation("net.researchgate:gradle-release:${Versions.RELEASE_PLUGIN}")
    implementation("com.github.ben-manes:gradle-versions-plugin:${Versions.VERSIONS_PLUGIN}")

    implementation("com.diogonunes:JColor:${Versions.JCOLOR}")

    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5:${Versions.KOTEST}")
    testImplementation("io.kotest:kotest-extensions-junit5:${Versions.KOTEST}")
    testImplementation("io.mockk:mockk:${Versions.MOCKK}")
}

val out: StyledTextOutput = project.serviceOf<StyledTextOutputFactory>().create("colored-output")

with(tasks) {
    withType<Wrapper> {
        distributionType = DistributionType.ALL
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = jvmTargetVersion
        }
    }

    withType<DependencyUpdatesTask> {
        val isDependencyVersionNotStable = fun(version: String): Boolean {
            val stableKeywords = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
            val regex = "^[0-9,.v-]+(-r)?$".toRegex()
            val isStable = stableKeywords || regex.matches(version)
            return isStable.not()
        }

        resolutionStrategy {
            componentSelection {
                all {
                    if (isDependencyVersionNotStable(candidate.version) && !isDependencyVersionNotStable(currentVersion)) {
                        reject("Release candidate")
                    }
                }
            }
        }
    }

    withType<Test> {
        finalizedBy("jacocoTestReport")
        useJUnitPlatform()

        testLogging {
            events = setOf(PASSED, SKIPPED, FAILED, STANDARD_ERROR)
            exceptionFormat = TestExceptionFormat.FULL
            showCauses = true
            showExceptions = true
            showStackTraces = true
        }

        maxParallelForks = Runtime.getRuntime().availableProcessors() / 2 + 1
        systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")

        doFirst {
            with(out.style(Style.ProgressStatus)) {
                println("***************************************************")
                println(" >> Running Tests")
                println("***************************************************")
            }
        }

        addTestListener(object : TestListener {
            override fun beforeSuite(suite: TestDescriptor) {}
            override fun beforeTest(testDescriptor: TestDescriptor) {}
            override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
            override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                if (suite.parent == null) {
                    val output =
                        "Results: ${result.resultType} " +
                            "(" +
                            "${result.testCount} tests, " +
                            "${result.successfulTestCount} successes, " +
                            "${result.failedTestCount} failures, " +
                            "${result.skippedTestCount} skipped" +
                            ")"
                    val startItem = "| "
                    val endItem = " |"
                    val repeatLength = startItem.length + output.length + endItem.length
                    out.style(if (result.failedTestCount == 0L) Style.SuccessHeader else Style.FailureHeader).println(
                        """
                    |
                    |${"-".repeat(repeatLength)}
                    |$startItem$output$endItem
                    |${"-".repeat(repeatLength)}
                    |
                    """.trimMargin()
                    )
                }
            }
        })

        doLast {
            with(out.style(Style.ProgressStatus)) {
                println("***************************************************")
                println(" >> Tests FINISHED")
                println("***************************************************")
            }
        }
    }

    jacocoTestReport {
        reports {
            xml.isEnabled = true
            html.isEnabled = false
        }
    }

    create("setupPublishPlugins") {
        doLast {
            val publishKey = System.getenv("GRADLE_PUBLISH_KEY")
            val publishSecret = System.getenv("GRADLE_PUBLISH_SECRET")

            if (publishKey.isNotBlank() && publishSecret.isNotBlank()) {
                println("Environment variables GRADLE_PUBLISH_KEY and GRADLE_PUBLISH_SECRET are set")
                println("Using in-memory Gradle key and secret for plugin publishing")
            } else {
                println("Environment variables GRADLE_PUBLISH_KEY and GRADLE_PUBLISH_SECRET are not set")
                println("Defaulting to global Gradle properties file for plugin publishing")
            }

            System.setProperty("gradle.publish.key", publishKey)
            System.setProperty("gradle.publish.secret", publishSecret)
        }
    }

    getByName("release") {
        dependsOn("setupPublishPlugins")
    }

    getByName("afterReleaseBuild") {
        dependsOn("publishPlugins")
    }
}

fun ReleaseExtension.git(config: GitConfig.() -> Unit) =
    (propertyMissing("git") as GitConfig).config()

release {
    preTagCommitMessage = "[Gradle Release] - pre tag commit: "
    newVersionCommitMessage = "[Gradle Release] - new version commit: "
    versionPatterns = mapOf(
        """[.]*\.(\d+)\.(\d+)[.]*""" to KotlinClosure2<Matcher, Project, String>({ matcher, project ->
            matcher.replaceAll(".${(matcher.group(1)).toString().toInt() + 1}.0")
        })
    )
    git {
        requireBranch = ""
        pushToRemote = "origin"
    }
}

publishing {
    repositories {
        maven {
            name = "local"
            url = uri(mavenLocal().url)
        }
    }
}

pluginBundle {
    website = "https://opengood.io"
    vcsUrl = "https://github.com/opengoodio/config-gradle-plugin"
    description = "Gradle plugin providing centralized configuration of OpenGood Gradle projects"
    tags = listOf("kotlin", "spring-boot", "opengood")

    (plugins) {
        "opengood-config" {
            displayName = "OpenGood Config Gradle Plugin"
        }
    }
}