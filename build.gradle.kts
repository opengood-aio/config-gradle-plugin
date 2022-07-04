import de.fayard.refreshVersions.core.versionFor
import net.researchgate.release.GitAdapter.GitConfig
import net.researchgate.release.ReleaseExtension
import org.gradle.api.internal.classpath.ModuleRegistry
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
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.regex.Matcher

plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
    id("java-gradle-plugin")
    id("jacoco")
    id("maven-publish")
    id("net.researchgate.release")
    id("com.gradle.plugin-publish")
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

allOpen {
    annotation("io.opengood.gradle.annotation.AllOpen")
}

val kotlinVersion = getKotlinPluginVersion()
val javaVersion = JavaVersion.VERSION_17
val jvmTargetVersion = "17"

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
        if (requested.group == "org.jetbrains.kotlinx" && requested.name == "kotlinx-coroutines-core") {
            useVersion(versionFor("version.kotlinx.coroutines"))
            because("Incompatibilities with older Kotlin Coroutines versions")
        }
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("allopen:_"))
    implementation(kotlin("noarg:_"))
    implementation(kotlin("gradle-plugin:_"))
    implementation(gradleApi())

    implementation("org.springframework.boot:spring-boot-gradle-plugin:_")
    implementation("io.spring.gradle:dependency-management-plugin:_")
    implementation("io.franzbecker:gradle-lombok:_")
    implementation("net.researchgate:gradle-release:_")

    implementation("com.diogonunes:JColor:_")
    implementation("com.fasterxml.jackson.core:jackson-annotations:_")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")

    testRuntimeOnly(
        files(
            serviceOf<ModuleRegistry>().getModule("gradle-tooling-api-builders").classpath.asFiles.first()
        )
    )

    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-assertions-core:_")
    testImplementation("io.kotest:kotest-extensions-junit5:_")
    testImplementation("io.kotest:kotest-property:_")
    testImplementation("io.kotest:kotest-runner-junit5:_")
    testImplementation("io.mockk:mockk:_")

    // annotationProcessor("org.projectlombok:lombok:_")
    // annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:_")
    // implementation("com.fasterxml.jackson.module:jackson-module-kotlin:_")
    // implementation("com.ninja-squad:springmockk:_")
    // implementation("org.codehaus.groovy:groovy-all:_")
    // implementation("org.springframework.boot:spring-boot-starter:_")
    // testImplementation("io.kotest:kotest-extensions-spring:_")
    // testImplementation("io.mockk:mockk:_")
    // testImplementation("org.assertj:assertj-core:_")
    // testImplementation("org.junit.jupiter:junit-jupiter:_")
    // testImplementation("org.mockito:mockito-core:_")
    // testImplementation("org.mockito:mockito-junit-jupiter:_")
    // testImplementation("org.springframework.boot:spring-boot-starter-test:_")
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

    withType<Test> {
        finalizedBy("jacocoTestReport")
        jvmArgs("--add-opens", "java.base/java.util=ALL-UNNAMED")
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
            xml.required.set(true)
            html.required.set(false)
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
    (getProperty("git") as GitConfig).config()

release {
    preTagCommitMessage.set("[Gradle Release] - pre tag commit: ")
    newVersionCommitMessage.set("[Gradle Release] - new version commit: ")
    versionPatterns = mapOf(
        """[.]*\.(\d+)\.(\d+)[.]*""" to KotlinClosure2<Matcher, Project, String>({ matcher, _ ->
            matcher.replaceAll(".${(matcher.group(1)).toString().toInt() + 1}.0")
        })
    )
    git {
        requireBranch.set("main")
        pushToRemote.set("origin")
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
