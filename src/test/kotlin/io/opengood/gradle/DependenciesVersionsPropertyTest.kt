package io.opengood.gradle

import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.opengood.gradle.constant.Dependencies
import org.gradle.testfixtures.ProjectBuilder

class DependenciesVersionsPropertyTest : WordSpec({

    "Dependencies Versions Property" should {
        "Return dependency and version" {
            val project = ProjectBuilder.builder().build()
            with(project.dependenciesVersions) {
                getDependencyAndVersion(Dependencies.TEST) shouldBe "io.opengood:test:1.0.0"
            }
        }

        "Return version" {
            val project = ProjectBuilder.builder().build()
            with(project.dependenciesVersions) {
                getVersion(Dependencies.TEST) shouldBe "1.0.0"
            }
        }
    }
})
