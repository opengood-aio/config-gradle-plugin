package io.opengood.gradle.enumeration

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape
import com.fasterxml.jackson.annotation.JsonValue

@JsonFormat(shape = Shape.OBJECT)
enum class ScmProvider(@JsonValue private val value: String) {
    GIT("git")
    ;

    override fun toString(): String = value

    companion object {
        const val PROTOCOL = "scm"
    }
}
