package io.opengood.gradle.closure

import groovy.lang.Closure

class KotlinClosure2<in T : Any?, in U : Any?, V : Any>(
    val function: (T, U) -> V?,
    owner: Any? = null,
    thisObject: Any? = null
) : Closure<V?>(owner, thisObject) {

    @Suppress("unused")
    fun doCall(t: T, u: U): V? = function(t, u)
}