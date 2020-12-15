package helper

import io.opengood.gradle.config.FeatureConfiguration
import org.gradle.api.Project

internal enum class Features(val flag: Int) {
    NONE(0),
    SPRING(0b00000001),
    LOMBOK(0b00000010),
    JUNIT(0b00000100),
    ASSERTJ(0b00001000),
    MOCKITO(0b00010000),
    KOTEST(0b00100000),
    MOCKK(0b01000000),
    PUBLISHING(0b10000000)
}

internal val defaultFeatures =
    Features.SPRING.flag or
        Features.LOMBOK.flag or
        Features.JUNIT.flag or
        Features.ASSERTJ.flag or
        Features.MOCKITO.flag or
        Features.KOTEST.flag or
        Features.MOCKK.flag or
        Features.PUBLISHING.flag

internal fun getFeatures(project: Project, flags: Int): FeatureConfiguration {
    val features = FeatureConfiguration(project)
    if (flags and Features.SPRING.flag == 0) features.spring = false
    if (flags and Features.LOMBOK.flag == 0) features.lombok = false
    if (flags and Features.JUNIT.flag == 0) features.junit = false
    if (flags and Features.ASSERTJ.flag == 0) features.assertj = false
    if (flags and Features.MOCKITO.flag == 0) features.mockito = false
    if (flags and Features.KOTEST.flag == 0) features.kotest = false
    if (flags and Features.MOCKK.flag == 0) features.mockk = false
    if (flags and Features.PUBLISHING.flag == 0) features.publishing = false
    return features
}
