package io.opengood.gradle.enumeration

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape
import com.fasterxml.jackson.annotation.JsonValue
import io.opengood.gradle.constant.GitHub.Companion.OPENGOOD_ORG_URI

@JsonFormat(shape = Shape.OBJECT)
enum class ScmProvider(@JsonValue private val value: String) {
    GIT("git")

    override fun toString(): String = value

    companion object {
        const val PROTOCOL = "scm"
        const val CONNECTION = "$PROTOCOL:%s:$OPENGOOD_ORG_URI/%s"
        const val DEV_CONNECTION = "$PROTOCOL:%s:$OPENGOOD_ORG_URI/%s"
    },
}
