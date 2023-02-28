package io.opengood.gradle.enumeration

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape
import com.fasterxml.jackson.annotation.JsonValue

@JsonFormat(shape = Shape.OBJECT)
enum class BuildGradleType(@JsonValue private val value: String) {
    GROOVY_DSL("build.gradle"),
    KOTLIN_DSL("build.gradle.kts"),
    ;

    override fun toString(): String = value
}
