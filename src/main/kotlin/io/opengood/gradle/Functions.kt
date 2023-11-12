package io.opengood.gradle

import java.io.File
import java.lang.System.getenv
import java.nio.file.Path

internal inline fun <reified V : Any> getEnvOrDefault(
    name: String,
    default: V,
): V = if (getenv().containsKey(name)) getenv(name) as V else default

internal fun getPathAsFile(
    path: String,
    vararg paths: String,
): File = Path.of(path, *paths).toFile()

internal fun <E : Enum<E>> hasPath(
    dir: File,
    file: Enum<E>,
): Boolean = getPathAsFile(dir.absolutePath, file.toString()).exists()

internal fun hasPath(
    dir: File,
    files: List<String>,
): Boolean = files.any { getPathAsFile(dir.absolutePath, it).exists() }
