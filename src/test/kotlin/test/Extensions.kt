package test

import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

internal inline fun <reified T : Any> Any.accessField(name: String): T =
    javaClass.getDeclaredField(name).let {
        it.isAccessible = true
        return@let it.get(this) as T
    }

internal inline fun <reified T : Any> T.callFun(
    name: String,
    vararg args: Any?,
): Any? =
    T::class
        .declaredMemberFunctions
        .firstOrNull { it.name == name }
        ?.apply { isAccessible = true }
        ?.call(this, *args)

infix fun <T> Boolean.then(param: () -> T): T? = if (this) param() else null
