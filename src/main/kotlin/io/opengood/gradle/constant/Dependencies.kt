package io.opengood.gradle.constant

class Dependencies {

    companion object {
        const val ASSERTJ = "org.assertj:assertj-core:${Versions.ASSERTJ}"
        const val GROOVY = "org.codehaus.groovy:groovy-all:${Versions.GROOVY}"
        const val JUNIT_JUPITER = "org.junit.jupiter:junit-jupiter:${Versions.JUNIT_JUPITER}"
        const val KOTEST_JUNIT_EXTENSIONS = "io.kotest:kotest-extensions-junit5:${Versions.KOTEST}"
        const val KOTEST_JUNIT_RUNNER = "io.kotest:kotest-runner-junit5:${Versions.KOTEST}"
        const val KOTEST_KOIN_EXTENSIONS = "io.kotest:kotest-extensions-koin:${Versions.KOTEST}"
        const val KOTEST_SPRING_EXTENSIONS = "io.kotest:kotest-extensions-spring:${Versions.KOTEST}"
        const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
        const val KOTLIN_STD_LIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
        const val LOMBOK = "org.projectlombok:lombok:${Versions.LOMBOK}"
        const val MOCKITO = "org.mockito:mockito-core:${Versions.MOCKITO}"
        const val MOCKITO_JUNIT_JUPITER = "org.mockito:mockito-junit-jupiter:${Versions.MOCKITO}"
        const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"
        const val SPRING_BOOT_CONFIG_PROCESSOR = "org.springframework.boot:spring-boot-configuration-processor:${Versions.SPRING_BOOT}"
        const val SPRING_BOOT_STARTER = "org.springframework.boot:spring-boot-starter:${Versions.SPRING_BOOT}"
        const val SPRING_BOOT_STARTER_TEST = "org.springframework.boot:spring-boot-starter-test:${Versions.SPRING_BOOT}"
    }
}
