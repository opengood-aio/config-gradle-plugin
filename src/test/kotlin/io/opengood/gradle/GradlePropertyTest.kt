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
        "Set given value" {
            val greeting = Greeting()
            val value = "Hello World!"

            greeting.message = value
            greeting.message should beTheSameInstanceAs(value)
        }

        "Set default value" {
            Greeting().message shouldBe "Hello World!"
        }

        "Throw exception if not set"  {
            shouldThrow<IllegalStateException> { Greeting().message }
        }
    }
})

private class Greeting {
    var message by GradleProperty(
        ProjectBuilder.builder().build(),
        String::class.java,
        "Hello World!"
    )
}