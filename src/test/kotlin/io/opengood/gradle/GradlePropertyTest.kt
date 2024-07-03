package io.opengood.gradle

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.opengood.gradle.property.GradleProperty
import org.gradle.testfixtures.ProjectBuilder

class GradlePropertyTest :
    WordSpec({

        "Gradle Property" should {
            "Set given value" {
                val value = "Hello World!"
                val greeting = Greeting()
                greeting.message = value

                greeting.message shouldBeSameInstanceAs value
            }

            "Set default value" {
                Greeting("Hello World!").message shouldBe "Hello World!"
            }

            "Throw exception if not set" {
                shouldThrow<IllegalStateException> { Greeting().message }
            }
        }
    })

private class Greeting(
    default: String? = null,
) {
    var message by GradleProperty(ProjectBuilder.builder().build(), String::class.java, default)
}
