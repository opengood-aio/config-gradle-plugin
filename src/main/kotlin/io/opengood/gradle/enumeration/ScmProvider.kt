package io.opengood.gradle.enumeration

enum class ScmProvider(private val value: String) {
    GIT("git");

    override fun toString(): String = value

    companion object {
        const val PROTOCOL = "scm"
    }
}