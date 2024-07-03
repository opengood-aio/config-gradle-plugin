package io.opengood.gradle

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.opengood.gradle.property.GradleListProperty
import org.gradle.testfixtures.ProjectBuilder

class GradleListPropertyTest :
    WordSpec({

        "Gradle List Property" should {
            "Set given value" {
                val value = listOf("Hello World!", "Hello Us!")
                val greetings = Greetings()
                greetings.messages = value

                greetings.messages shouldBe value
            }

            "Set default value" {
                Greetings(listOf("Hello World!", "Hello Us!")).messages shouldBe
                    listOf(
                        "Hello World!",
                        "Hello Us!",
                    )
            }

            "Throw exception if not set" {
                shouldThrow<IllegalStateException> { Greetings().messages }
            }
        }
    })

private class Greetings(
    default: List<String>? = null,
) {
    var messages by GradleListProperty(ProjectBuilder.builder().build(), String::class.java, default)
}
