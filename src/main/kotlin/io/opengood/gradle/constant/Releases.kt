package io.opengood.gradle.constant

import io.opengood.gradle.closure.KotlinClosure2
import org.gradle.api.Project
import java.util.regex.Matcher

class Releases {

    companion object {
        const val NEW_VERSION_COMMIT_MESSAGE = "[Gradle Release] - new version commit: "
        const val PRE_TAG_COMMIT_MESSAGE = "[Gradle Release] - pre tag commit: "
        const val PUSH_TO_REMOTE = "origin"
        const val REQUIRE_BRANCH = ""

        val VERSION_PATTERNS = mapOf(
            """[.]*\.(\d+)\.(\d+)[.]*""" to KotlinClosure2<Matcher, Project, String>({ matcher, project ->
                matcher.replaceAll(".${(matcher.group(0)[1]) + 1}.0")
            })
        )
    }
}