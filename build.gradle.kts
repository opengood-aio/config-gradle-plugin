import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.gradle.internal.logging.text.StyledTextOutput
import org.gradle.internal.logging.text.StyledTextOutput.Style
import org.gradle.internal.logging.text.StyledTextOutputFactory
import org.gradle.kotlin.dsl.support.serviceOf
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.*

plugins {
    kotlin("jvm") version "1.4.10"
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.github.ben-manes.versions") version "0.33.0"
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

val kotlinVersion =  plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion
val javaVersion = JavaVersion.VERSION_11
val jvmTargetVersion = "11"

object Versions {
    const val SPRING_BOOT_PLUGIN = "2.3.4.RELEASE"
    const val SPRING_DEPENDENCY_MANAGEMENT_PLUGIN = "1.0.10.RELEASE"
    const val VERSIONS_PLUGIN = "0.33.0"

    const val J_COLOR = "5.0.1"

    const val ASSERT_J = "3.18.0"
    const val JUNIT_JUPITER = "5.7.0"
    const val KOTLIN_TEST = "3.4.2"
    const val MOCK_K = "1.10.2"
}

java.sourceCompatibility = javaVersion
java.targetCompatibility = javaVersion

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
    implementation("com.github.ben-manes:gradle-versions-plugin:${Versions.VERSIONS_PLUGIN}")

    implementation("com.diogonunes:JColor:${Versions.J_COLOR}")

    testImplementation(kotlin("test"))

    testImplementation("io.kotlintest:kotlintest-runner-junit5:${Versions.KOTLIN_TEST}")
    testImplementation("org.junit.jupiter:junit-jupiter:${Versions.JUNIT_JUPITER}")
    testImplementation("org.assertj:assertj-core:${Versions.ASSERT_J}")
    testImplementation("io.mockk:mockk:${Versions.MOCK_K}")
}

val out: StyledTextOutput = project.serviceOf<StyledTextOutputFactory>().create("colored-output")

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = jvmTargetVersion
    }
}

tasks.withType<DependencyUpdatesTask> {
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

tasks.withType<Test> {
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
        out.style(Style.Header).println("***************************************************")
        out.style(Style.Header).println(" >> Running Tests")
        out.style(Style.Header).println("***************************************************")
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
                out.style(if (result.failedTestCount == 0L) Style.SuccessHeader else Style.FailureHeader).println("""
                    |
                    |${"-".repeat(repeatLength)}
                    |$startItem$output$endItem
                    |${"-".repeat(repeatLength)}
                    |
                    """.trimMargin())
            }
        }
    })

    doLast {
        out.style(Style.Header).println("***************************************************")
        out.style(Style.Header).println(" >> Tests FINISHED")
        out.style(Style.Header).println("***************************************************")
    }
}

publishing {
    repositories {
        maven {
            name = "github"
            url = uri("https://maven.pkg.github.com/opengoodio/config-gradle-plugin")
            credentials {
                val properties = Properties()
                properties.load(FileInputStream("${System.getenv("HOME")}/.github.properties"))

                username = properties["gh.api.user"] as String?
                password = properties["gh.api.key"] as String?
            }
        }
        maven {
            name = "local"
            url = uri(mavenLocal().url)
        }
    }
}
