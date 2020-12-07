package helper

import io.opengood.gradle.config.FeatureConfiguration
import org.gradle.api.Project

internal enum class Features(val flag: Int) {
    NONE(0),
    SPRING(0b0000001),
    LOMBOK(0b0000010),
    JUNIT(0b0000100),
    ASSERTJ(0b0001000),
    MOCKITO(0b0010000),
    KOTEST(0b0100000),
    MOCKK(0b1000000)
}

internal val defaultFeatures =
    Features.SPRING.flag or
        Features.LOMBOK.flag or
        Features.JUNIT.flag or
        Features.ASSERTJ.flag or
        Features.MOCKITO.flag or
        Features.KOTEST.flag or
        Features.MOCKK.flag

internal fun getFeatures(project: Project, flags: Int): FeatureConfiguration {
    val features = FeatureConfiguration(project)
    if (flags and Features.SPRING.flag == 0) features.spring = false
    if (flags and Features.LOMBOK.flag == 0) features.lombok = false
    if (flags and Features.JUNIT.flag == 0) features.junit = false
    if (flags and Features.ASSERTJ.flag == 0) features.assertj = false
    if (flags and Features.MOCKITO.flag == 0) features.mockito = false
    if (flags and Features.KOTEST.flag == 0) features.kotest = false
    if (flags and Features.MOCKK.flag == 0) features.mockk = false
    return features
}
