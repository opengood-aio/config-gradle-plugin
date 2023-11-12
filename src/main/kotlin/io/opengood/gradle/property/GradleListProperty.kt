package io.opengood.gradle.property

import org.gradle.api.Project
import kotlin.reflect.KProperty

class GradleListProperty<T, V>(
    project: Project,
    type: Class<V>,
    default: List<V>? = null,
) {
    private val property =
        project.objects.listProperty(type).apply {
            set(default)
        }

    operator fun getValue(
        ref: T,
        property: KProperty<*>,
    ): List<V> = this.property.get()

    operator fun setValue(
        ref: T,
        property: KProperty<*>,
        value: List<V>,
    ) = this.property.set(value)
}
