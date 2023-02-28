package test

internal inline fun <reified T : Any> Any.accessField(name: String): T =
    javaClass.getDeclaredField(name).let {
        it.isAccessible = true
        return@let it.get(this) as T
    }

infix fun <T> Boolean.then(param: () -> T): T? =
    if (this) param() else null
