package helper

import io.opengood.gradle.config.MultipleFrameworkConfiguration
import org.gradle.api.Project

internal enum class MultipleFrameworks(val flag: Int) {
    NONE(0),
    KOTLIN(0b0000001)
}

internal val defaultMultipleFrameworks =
    MultipleFrameworks.NONE.flag

internal fun getMultipleFrameworks(project: Project, flags: Int): MultipleFrameworkConfiguration {
    val multipleFrameworks = MultipleFrameworkConfiguration(project)
    if (flags and MultipleFrameworks.KOTLIN.flag != 0) multipleFrameworks.kotlin = true
    return multipleFrameworks
}
