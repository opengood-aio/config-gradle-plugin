package io.opengood.gradle.constant

import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR

class Tests {
    companion object {
        val EXCEPTION_FORMAT = TestExceptionFormat.FULL
        val LOGGING_EVENTS = setOf(PASSED, SKIPPED, FAILED, STANDARD_ERROR)
        val MAX_PARALLEL_FORKS = Runtime.getRuntime().availableProcessors() / 2 + 1

        const val INSTANCE_LIFECYCLE_SYS_PROP_NAME = "junit.jupiter.testinstance.lifecycle.default"
        const val INSTANCE_LIFECYCLE_SYS_PROP_VALUE = "per_class"
        const val JVM_ARGS_ADD_OPENS = "--add-opens"
        const val JVM_ARGS_ADD_OPENS_JAVA_UTIL = "java.base/java.util=ALL-UNNAMED"
        const val SHOW_CAUSES = true
        const val SHOW_EXCEPTIONS = true
        const val SHOW_STACK_TRACES = true
        const val SKIP_TESTS = "skipTests"
    }
}
