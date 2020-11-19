package io.opengood.gradle

import io.kotlintest.matchers.beTheSameInstanceAs
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.WordSpec
import io.opengood.gradle.property.GradleProperty
import org.gradle.testfixtures.ProjectBuilder

class GradlePropertyTest : WordSpec({

    "Gradle Property" should {
        "Set default value" {
            val greeting = object {
                val message by GradleProperty(
                    ProjectBuilder.builder().build(),
                    String::class.java,
                    "Hello World!")
            }

            greeting.message shouldBe "Hello World!"
        }

        "Set and return exact same value" {
            val greeting = Greeting()
            val value = "Hello World!"

            greeting.message = value
            greeting.message should beTheSameInstanceAs(value)
        }

        "Throw exception if not set"  {
            val greeting = Greeting()
            shouldThrow<IllegalStateException> {
                greeting.message
            }
        }
    }
})

private class Greeting {
    var message by GradleProperty(
        ProjectBuilder.builder().build(),
        String::class.java
    )
}