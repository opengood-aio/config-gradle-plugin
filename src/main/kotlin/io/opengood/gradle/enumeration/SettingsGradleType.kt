package io.opengood.gradle.enumeration

enum class SettingsGradleType(private val value: String) {
    GROOVY("settings.gradle"),
    KOTLIN("settings.gradle.kts");

    override fun toString(): String = value
}
