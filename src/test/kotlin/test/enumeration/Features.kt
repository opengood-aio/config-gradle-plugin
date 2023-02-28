package test.enumeration

import io.opengood.gradle.model.FeatureConfiguration
import org.gradle.api.Project

internal enum class Features(val flag: Int) {
    NONE(0),
    ASSERTJ(0b000000000001),
    JACKSON(0b000000000010),
    JACKSON_KOTLIN(0b000000000100),
    JUNIT_JUPITER(0b000000001000),
    KOTEST(0b000000010000),
    KOTEST_SPRING(0b000000100000),
    KOTLIN_COROUTINES(0b00000100000),
    LOMBOK(0b000010000000),
    MOCKITO(0b000100000000),
    MOCKK(0b001000000000),
    SPRING(0b010000000000),
    SPRING_MOCKK(0b100000000000),
}

internal val defaultFeatures =
    Features.ASSERTJ.flag or
        Features.JACKSON.flag or
        Features.JACKSON_KOTLIN.flag or
        Features.JUNIT_JUPITER.flag or
        Features.KOTEST.flag or
        Features.KOTEST_SPRING.flag or
        Features.KOTLIN_COROUTINES.flag or
        Features.LOMBOK.flag or
        Features.MOCKITO.flag or
        Features.MOCKK.flag or
        Features.SPRING.flag or
        Features.SPRING_MOCKK.flag

internal fun getFeatures(project: Project, flags: Int): FeatureConfiguration {
    val features = FeatureConfiguration(project)
    if (flags and Features.ASSERTJ.flag == 0) features.assertj = false
    if (flags and Features.JACKSON.flag == 0) features.jackson = false
    if (flags and Features.JACKSON_KOTLIN.flag == 0) features.jacksonKotlin = false
    if (flags and Features.JUNIT_JUPITER.flag == 0) features.junitJupiter = false
    if (flags and Features.KOTEST.flag == 0) features.kotest = false
    if (flags and Features.KOTEST_SPRING.flag == 0) features.kotestSpring = false
    if (flags and Features.KOTLIN_COROUTINES.flag == 0) features.kotlinCoroutines = false
    if (flags and Features.LOMBOK.flag == 0) features.lombok = false
    if (flags and Features.MOCKITO.flag == 0) features.mockito = false
    if (flags and Features.MOCKK.flag == 0) features.mockk = false
    if (flags and Features.SPRING.flag == 0) features.spring = false
    if (flags and Features.SPRING_MOCKK.flag == 0) features.springMockk = false
    return features
}
