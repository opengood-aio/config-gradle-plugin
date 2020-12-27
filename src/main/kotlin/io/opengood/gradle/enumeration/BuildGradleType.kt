package io.opengood.gradle.enumeration

enum class BuildGradleType(private val value: String) {
    GROOVY("build.gradle"),
    KOTLIN("build.gradle.kts");

    override fun toString(): String = value
}
