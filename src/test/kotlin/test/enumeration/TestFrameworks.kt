package test.enumeration

import io.opengood.gradle.model.TestFrameworkConfiguration
import org.gradle.api.Project

internal enum class TestFrameworks(val flag: Int) {
    DEFAULT(0),
    JAVA(0b0000001),
}

internal val defaultTestFrameworks =
    TestFrameworks.DEFAULT.flag

internal fun getTestFrameworks(project: Project, flags: Int): TestFrameworkConfiguration {
    val testFrameworks = TestFrameworkConfiguration(project)
    if (flags and TestFrameworks.JAVA.flag != 0) testFrameworks.java = true
    return testFrameworks
}
