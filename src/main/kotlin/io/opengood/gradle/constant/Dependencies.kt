package io.opengood.gradle.constant

class Dependencies {

    companion object {
        const val KOTLIN_JDK_STD_LIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}"
        const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"

        const val J_COLOR = "com.diogonunes:JColor:${Versions.J_COLOR}"

        const val ASSERT_J = "org.assertj:assertj-core:${Versions.ASSERT_J}"
        const val JUNIT_JUPITER = "org.junit.jupiter:junit-jupiter:${Versions.JUNIT_JUPITER}"
        const val KOTLIN_TEST = "io.kotlintest:kotlintest-runner-junit5:${Versions.KOTLIN_TEST}"
        const val MOCK_K = "io.mockk:mockk:${Versions.MOCK_K}"
    }
}