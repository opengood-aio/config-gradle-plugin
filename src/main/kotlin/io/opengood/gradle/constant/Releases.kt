package io.opengood.gradle.constant

import io.opengood.gradle.closure.KotlinClosure2
import org.gradle.api.Project
import java.util.regex.Matcher

class Releases {

    companion object {
        const val NEW_VERSION_COMMIT_MESSAGE = "[Gradle Release] - new version commit: "
        const val PRE_TAG_COMMIT_MESSAGE = "[Gradle Release] - pre tag commit: "
        const val PUSH_TO_REMOTE = "origin"
        const val REQUIRE_BRANCH = "main"
        const val VERSION_PATTERN = """[.]*\.(\d+)\.(\d+)[.]*"""

        val VERSION_PATTERN_CLOSURE = KotlinClosure2<Matcher, Project, String>({ matcher, _ ->
            matcher.replaceAll(".${(matcher.group(1)).toString().toInt() + 1}.0")
        })
        val VERSION_PATTERNS = mapOf(VERSION_PATTERN to VERSION_PATTERN_CLOSURE)
    }
}
