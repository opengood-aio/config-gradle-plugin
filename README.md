# OpenGood Config Gradle Plugin

[![Build](https://github.com/opengoodio/config-gradle-plugin/workflows/build/badge.svg)](https://github.com/opengoodio/config-gradle-plugin/actions?query=workflow%3Abuild)
[![Release](https://github.com/opengoodio/config-gradle-plugin/workflows/release/badge.svg)](https://github.com/opengoodio/config-gradle-plugin/actions?query=workflow%3Arelease)
[![Codecov](https://codecov.io/gh/opengoodio/config-gradle-plugin/branch/main/graph/badge.svg?token=AEEYTGK87F)](https://codecov.io/gh/opengoodio/config-gradle-plugin)
[![Release Version](https://img.shields.io/github/release/opengoodio/config-gradle-plugin.svg)](https://github.com/opengoodio/config-gradle-plugin/releases/latest)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/opengoodio/config-gradle-plugin/master/LICENSE)
[![FOSSA](https://app.fossa.com/api/projects/custom%2B22161%2Fgithub.com%2Fopengoodio%2Fconfig-gradle-plugin.svg?type=small)](https://app.fossa.com/projects/custom%2B22161%2Fgithub.com%2Fopengoodio%2Fconfig-gradle-plugin?ref=badge_small)

Gradle plugin providing centralized configuration of OpenGood Gradle
projects


## Usage

Add `opengood-config` Gradle plugin to `build.gradle.kts`:

```kotlin
plugins {
    id("io.opengood.gradle.config") version "VERSION"
}
```

**Note:** See *Release* version badge above for latest version.

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

##### Local

* Using Gradle account with API key for publishing, add credentials to
`~/.gradle/gradle.properties`:

    ```properties
    gradle.publish.key=<gradle-api-key>
    gradle.publish.secret=<gradle-api-key-secret>
    ```

* Create release version and publish plugin to Gradle plugin repository:

    ```bash
    ./gradlew clean release -Prelease.useAutomaticVersion=true
    ```

    **Note:** `publishPlugins` task is configured with Gradle release
    plugin to execute after release build

##### CI/CD

* Using Gradle account with API key for publishing, add environment
variables to CI job:

    ```
    GRADLE_PUBLISH_KEY=<gradle-api-key>
    GRADLE_PUBLISH_SECRET=<gradle-api-key-secret>
    ```

* Create release version and publish plugin to Gradle plugin repository:

    ```bash
    git commit --allow-empty -m "Create release"; git push
    ```

#### Artifact

**Note:** Used for publishing artifacts using `opengood-config` Gradle
plugin to Maven OSS repository

##### Local

* Using Sonatype OSS account with GPG key for publishing, add
credentials to `~/.gradle/gradle.properties`:

    ```properties
    ossrhUsername=<sonatype-account-username>
    ossrhPassword=<sonatype-account-password>
    
    signing.keyId=<gpg-key-id>
    signing.password=<gpg-key-password>
    signing.secretKeyRingFile=<gpg-keys-file-path>
    ```

* Create release version and publish artifact to Maven OSS repository:

    ```bash
    ./gradlew clean release -Prelease.useAutomaticVersion=true
    ```

    **Note:** `uploadArchives` task is configured with Gradle release
    plugin to execute after release build

##### CI/CD

* Using Sonatype OSS account with GPG key for publishing, add
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
    git commit --allow-empty -m "Create release"; git push
    ```
