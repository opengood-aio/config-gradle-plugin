# OpenGood Config Gradle Plugin

![Build](https://github.com/opengoodio/config-gradle-plugin/workflows/Build/badge.svg)
![Lint](https://github.com/opengoodio/config-gradle-plugin/workflows/Lint/badge.svg)
![Release](https://github.com/opengoodio/config-gradle-plugin/workflows/Release/badge.svg)
[![codecov](https://codecov.io/gh/opengoodio/config-gradle-plugin/branch/main/graph/badge.svg?token=AEEYTGK87F)](https://codecov.io/gh/opengoodio/config-gradle-plugin)
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fopengoodio%2Fconfig-gradle-plugin.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fcopengoodio%2Fconfig-gradle-plugin?ref=badge_shield)

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

* For local, using Gradle account with API key for publishing, add
credentials to `~/.gradle/gradle.properties`:

    ```properties
    gradle.publish.key=<gradle-api-key>
    gradle.publish.secret=<gradle-api-key-secret>
    ```

* For CI, using Gradle account with API key for publishing, add
environment variables to CI job:

    ```
    GRADLE_PUBLISH_KEY=<gradle-api-key>
    GRADLE_PUBLISH_SECRET=<gradle-api-key-secret>
    ```

* Create release version and publish plugin to Gradle plugin repository:

    ```bash
    ./gradlew clean release -Prelease.useAutomaticVersion=true
    ```

    **Note:** `publishPlugins` task is configured with Gradle release
    plugin to execute after release build

#### Artifact

**Note:** Used for publishing artifacts using `opengood-config` Gradle
plugin to Maven OSS repository

* For local, using Sonatype OSS account with GPG key for publishing, add
credentials to `~/.gradle/gradle.properties`:

    ```properties
    ossrhUsername=<sonatype-account-username>
    ossrhPassword=<sonatype-account-password>
    
    signing.keyId=<gpg-key-id>
    signing.password=<gpg-key-password>
    signing.secretKeyRingFile=<gpg-keys-file-path>
    ```

* For CI, using Sonatype OSS account with GPG key for publishing, add
environment variables to CI job:

    ```
    OSSRH_USERNAME=<sonatype-account-username>
    OSSRH_PASSWORD=<sonatype-account-password>
    GPG_SIGNING_PRIVATE_KEY=<gpg-key-private-key>
    GPG_SIGNING_PASSWORD=<gpg-key-password>
    ```

    **Note:** `gpg-key-private-key` must be in ASCII armored format

* Create release version and publish artifact to Maven OSS repository:

    ```bash
    ./gradlew clean release -Prelease.useAutomaticVersion=true
    ```

    **Note:** `uploadArchives` task is configured with Gradle release
    plugin to execute after release build
