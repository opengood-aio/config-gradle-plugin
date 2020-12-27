package io.opengood.gradle.builder.groovy

import groovy.lang.GroovyObject

internal class GroovyBuilderScopeForGroovyObject(
    override val delegate: GroovyObject
) : GroovyBuilderScope, GroovyObject by delegate {

    override fun String.invoke(vararg arguments: Any?): Any? =
        delegate.invokeMethod(this, arguments)
}
