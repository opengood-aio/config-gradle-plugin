# OpenGood Config Gradle Plugin

Gradle plugin providing centralized configuration of OpenGood Gradle
projects

## Usage

Add `opengood-config` Gradle plugin to `build.gradle.kts`:

```kotlin
plugins {
    id("io.opengood.gradle.config") version "VERSION"
}
```

Add plugin repository to `settings.gradle.kts`:

```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "opengood"
            url = uri("https://maven.pkg.github.com/opengoodio/config-gradle-plugin")
            credentials {
                val properties = java.util.Properties()
                properties.load(java.io.FileInputStream("${System.getenv("HOME")}/.github.properties"))

                username = properties["gh.api.user"] as String
                password = properties["gh.api.key"] as String
            }
        }
    }
}
```
