package io.opengood.gradle

import io.kotest.core.spec.style.WordSpec
import io.opengood.gradle.enumeration.LanguageType
import test.spec.buildScriptTest

class BuildScriptGroovyTest :
    WordSpec({

        include(buildScriptTest(LanguageType.GROOVY))
    })
