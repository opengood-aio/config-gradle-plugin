package io.opengood.gradle.constant

class Dependencies {

    companion object {
        const val ASSERTJ = "org.assertj:assertj-core:${Versions.ASSERTJ}"
        const val GROOVY = "org.codehaus.groovy:groovy-all:${Versions.GROOVY}"
        const val JACKSON_KOTLIN = "com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.JACKSON_KOTLIN}"
        const val JUNIT_JUPITER = "org.junit.jupiter:junit-jupiter:${Versions.JUNIT_JUPITER}"
        const val KOTEST = "io.kotest:kotest-runner-junit5:${Versions.KOTEST}"
        const val KOTEST_ASSERTIONS = "io.kotest:kotest-assertions-core:${Versions.KOTEST}"
        const val KOTEST_EXTENSIONS = "io.kotest:kotest-extensions-junit5:${Versions.KOTEST}"
        const val KOTEST_PROPERTIES = "io.kotest:kotest-property:${Versions.KOTEST}"
        const val KOTEST_SPRING = "io.kotest:kotest-extensions-spring:${Versions.KOTEST_SPRING}"
        const val KOTLIN_COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLIN_COROUTINES}"
        const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
        const val KOTLIN_STD_LIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
        const val KOTLIN_TEST = "org.jetbrains.kotlin:kotlin-test:${Versions.KOTLIN}"
        const val LOMBOK = "org.projectlombok:lombok:${Versions.LOMBOK}"
        const val MOCKITO = "org.mockito:mockito-core:${Versions.MOCKITO}"
        const val MOCKITO_JUNIT_JUPITER = "org.mockito:mockito-junit-jupiter:${Versions.MOCKITO}"
        const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"
        const val SPRING_BOOT_CONFIG_PROCESSOR = "org.springframework.boot:spring-boot-configuration-processor:${Versions.SPRING_BOOT}"
        const val SPRING_BOOT_STARTER = "org.springframework.boot:spring-boot-starter:${Versions.SPRING_BOOT}"
        const val SPRING_BOOT_STARTER_TEST = "org.springframework.boot:spring-boot-starter-test:${Versions.SPRING_BOOT}"
        const val SPRING_MOCKK = "com.ninja-squad:springmockk:${Versions.SPRING_MOCKK}"
    }
}
