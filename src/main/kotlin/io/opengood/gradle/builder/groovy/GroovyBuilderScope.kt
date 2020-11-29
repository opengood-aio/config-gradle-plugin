package io.opengood.gradle.builder.groovy

import groovy.lang.Closure
import groovy.lang.GroovyObject

internal interface GroovyBuilderScope : GroovyObject {

    val delegate: Any

    operator fun String.invoke(vararg arguments: Any?): Any?

    operator fun String.invoke(): Any? =
        invoke(*emptyArray<Any>())

    operator fun <T> String.invoke(vararg arguments: Any?, builder: GroovyBuilderScope.() -> T): Any? =
        invoke(*arguments, closureFor(builder))

    operator fun <T> String.invoke(builder: GroovyBuilderScope.() -> T): Any? =
        invoke(closureFor(builder))

    operator fun <T> String.invoke(vararg keywordArguments: Pair<String, Any?>, builder: GroovyBuilderScope.() -> T): Any? =
        invoke(keywordArguments.toMap(), closureFor(builder))

    operator fun String.invoke(vararg keywordArguments: Pair<String, Any?>): Any? =
        invoke(keywordArguments.toMap())

    private fun <T> closureFor(builder: GroovyBuilderScope.() -> T): Closure<Any?> =
        object : Closure<Any?>(this, this) {
            @Suppress("unused")
            fun doCall() = delegate.withGroovyBuilder(builder)
        }

    companion object {
        fun of(value: Any): GroovyBuilderScope =
            when (value) {
                is GroovyObject -> GroovyBuilderScopeForGroovyObject(value)
                else -> GroovyBuilderScopeForRegularObject(value)
            }
    }
}