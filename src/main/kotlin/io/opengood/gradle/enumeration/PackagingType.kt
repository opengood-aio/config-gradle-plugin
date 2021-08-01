package io.opengood.gradle.enumeration

enum class PackagingType(private val value: String) {
    JAR("jar")
    ;

    override fun toString(): String = value
}
