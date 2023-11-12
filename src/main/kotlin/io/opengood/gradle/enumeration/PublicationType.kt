package io.opengood.gradle.enumeration

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape
import com.fasterxml.jackson.annotation.JsonValue

@JsonFormat(shape = Shape.OBJECT)
enum class PublicationType(
    @JsonValue private val value: String,
) {
    GITHUB("GitHub"),
    OSS("OSS"),
    ;

    override fun toString(): String = value
}
