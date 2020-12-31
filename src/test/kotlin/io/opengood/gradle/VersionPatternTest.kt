package io.opengood.gradle

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.opengood.gradle.constant.Releases
import org.gradle.testfixtures.ProjectBuilder
import java.util.regex.Pattern

class VersionPatternTest : WordSpec({

    "Version Pattern" should {
        val pattern = Pattern.compile(Releases.VERSION_PATTERN)
        val project = ProjectBuilder.builder().build()

        "Increment minor version part by one to single digit given single digit minor version part" {
            val matcher = pattern.matcher("1.1.0").also { it.find() }

            val result = Releases.VERSION_PATTERN_CLOSURE.doCall(matcher, project)

            result shouldBe "1.2.0"
        }

        "Increment minor version part by one to two digits given single digit minor version part" {
            val matcher = pattern.matcher("1.9.0").also { it.find() }

            val result = Releases.VERSION_PATTERN_CLOSURE.doCall(matcher, project)

            result shouldBe "1.10.0"
        }

        "Increment minor version part by one to three digits given two digit minor version part" {
            val matcher = pattern.matcher("1.99.0").also { it.find() }

            val result = Releases.VERSION_PATTERN_CLOSURE.doCall(matcher, project)

            result shouldBe "1.100.0"
        }

        "Increment minor version part by one to four digits given three digit minor version part" {
            val matcher = pattern.matcher("1.999.0").also { it.find() }

            val result = Releases.VERSION_PATTERN_CLOSURE.doCall(matcher, project)

            result shouldBe "1.1000.0"
        }
    }
})
