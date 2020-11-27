package io.opengood.gradle.property

import org.gradle.api.Project
import kotlin.reflect.KProperty

class GradleProperty<T, V>(
    project: Project,
    type: Class<V>,
    default: V? = null
) {
    private val property = project.objects.property(type).apply {
        set(default)
    }

    operator fun getValue(ref: T, property: KProperty<*>): V =
        this.property.get()

    operator fun setValue(ref: T, property: KProperty<*>, value: V) =
        this.property.set(value)
}