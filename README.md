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

### Customize

The plugin supports customized properties:

### Sections

#### Main

| Property | Description | Default |
|---|---|---|
| projectType | Type of project. Supported (`APP`, `LIB`) | `APP` |

#### Test

| Property | Description | Default |
|---|---|---|
| maxParallelForks | Number of concurrent test classes to execute | `Available Processors / 2 + 1` |

#### Example

```kotlin
import io.opengood.gradle.enumeration.ProjectType

plugins {
    id("io.opengood.gradle.config") version "VERSION"
}

opengood {
    main {
        projectType = ProjectType.APP
    }
    test {
        maxParallelForks = 1
    }
}
```

## Development

### Publish

#### Plugin

**Note:** Used for publishing `opengood-config` Gradle plugin to Gradle
plugin repository

* Using Gradle account with API key for publishing, add credentials to
`~/.gradle/gradle.properties`:

    ```properties
    gradle.publish.key=<gradle-api-key>
    gradle.publish.secret=<gradle-api-key-secret>
    ```

* Create release version and publish plugin to Gradle plugin repository:

    ```bash
    ./gradlew clean assemble publishPlugins
    ```

#### Artifact

**Note:** Used for publishing artifacts using `opengood-config` Gradle
plugin to Maven OSS repository

* Using Sonatype OSS account with GPG key for publishing, add credentials
to `~/.gradle/gradle.properties`:

    ```properties
    ossrhUsername=<sonatype-account-username>
    ossrhPassword=<sonatype-account-password>
    
    signing.keyId=<gpg-key-id>
    signing.password=<gpg-key-password>
    signing.secretKeyRingFile=<gpg-keys-file-path>
    ```

* Create release version and publish artifact to Maven OSS repository:

    ```bash
    ./gradlew clean assemble uploadArchives
    ```
