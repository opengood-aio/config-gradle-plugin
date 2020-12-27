package io.opengood.gradle.builder.groovy

import groovy.lang.MetaClass
import org.codehaus.groovy.runtime.InvokerHelper

internal class GroovyBuilderScopeForRegularObject(
    override val delegate: Any
) : GroovyBuilderScope {

    private val groovyMetaClass: MetaClass by unsafeLazy {
        InvokerHelper.getMetaClass(delegate)
    }

    override fun invokeMethod(name: String, args: Any?): Any? =
        groovyMetaClass.invokeMethod(delegate, name, args)

    override fun setProperty(propertyName: String, newValue: Any?) =
        groovyMetaClass.setProperty(delegate, propertyName, newValue)

    override fun getProperty(propertyName: String): Any =
        groovyMetaClass.getProperty(delegate, propertyName)

    override fun setMetaClass(metaClass: MetaClass?) =
        throw IllegalStateException()

    override fun getMetaClass(): MetaClass =
        groovyMetaClass

    override fun String.invoke(vararg arguments: Any?): Any? =
        groovyMetaClass.invokeMethod(delegate, this, arguments)
}
