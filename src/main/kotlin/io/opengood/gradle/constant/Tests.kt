package io.opengood.gradle.constant

import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

class Tests {

    companion object {
        val EXCEPTION_FORMAT = FULL
        val LOGGING_EVENTS = setOf(PASSED, SKIPPED, FAILED, STANDARD_ERROR)
        val MAX_PARALLEL_FORKS = Runtime.getRuntime().availableProcessors() / 2 + 1

        const val INSTANCE_LIFECYCLE_SYS_PROP_NAME = "junit.jupiter.testinstance.lifecycle.default"
        const val INSTANCE_LIFECYCLE_SYS_PROP_VALUE = "per_class"
        const val SHOW_CAUSES = true
        const val SHOW_EXCEPTIONS = true
        const val SHOW_STACK_TRACES = true
    }
}
