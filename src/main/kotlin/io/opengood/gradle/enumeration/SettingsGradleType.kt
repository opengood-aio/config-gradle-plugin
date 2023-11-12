package io.opengood.gradle.enumeration

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape
import com.fasterxml.jackson.annotation.JsonValue

@JsonFormat(shape = Shape.OBJECT)
enum class SettingsGradleType(
    @JsonValue private val value: String,
) {
    GROOVY_DSL("settings.gradle"),
    KOTLIN_DSL("settings.gradle.kts"),
    ;

    override fun toString(): String = value
}
