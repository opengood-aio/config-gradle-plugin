package io.opengood.gradle.constant

class Dependencies {

    companion object {
        const val ASSERT_J = "org.assertj:assertj-core:${Versions.ASSERT_J}"
        const val GROOVY = "org.codehaus.groovy:groovy-all:${Versions.GROOVY}"
        const val JUNIT_JUPITER = "org.junit.jupiter:junit-jupiter:${Versions.JUNIT_JUPITER}"
        const val KO_TEST_JUNIT_RUNNER = "io.kotest:kotest-runner-junit5:${Versions.KO_TEST}"
        const val KO_TEST_JUNIT_SPRING = "io.kotest:kotest-extensions-spring:${Versions.KO_TEST}"
        const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
        const val KOTLIN_STD_LIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
        const val LOMBOK = "org.projectlombok:lombok:${Versions.LOMBOK}"
        const val MOCK_K = "io.mockk:mockk:${Versions.MOCK_K}"
        const val SPRING_BOOT_CONFIG_PROCESSOR = "org.springframework.boot:spring-boot-configuration-processor:${Versions.SPRING_BOOT}"
        const val SPRING_BOOT_STARTER = "org.springframework.boot:spring-boot-starter:${Versions.SPRING_BOOT}"
        const val SPRING_BOOT_STARTER_TEST = "org.springframework.boot:spring-boot-starter-test:${Versions.SPRING_BOOT}"
    }
}