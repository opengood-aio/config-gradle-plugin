package io.opengood.gradle

import io.kotest.core.spec.style.WordSpec
import io.opengood.gradle.enumeration.LanguageType
import test.spec.buildScriptTest

class BuildScriptKotlinTest : WordSpec({

    include(buildScriptTest(LanguageType.KOTLIN))
})
