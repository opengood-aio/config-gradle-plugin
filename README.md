# OpenGood Config Gradle Plugin

[![Build](https://github.com/opengoodio/config-gradle-plugin/workflows/build/badge.svg)](https://github.com/opengoodio/config-gradle-plugin/actions?query=workflow%3Abuild)
[![Release](https://github.com/opengoodio/config-gradle-plugin/workflows/release/badge.svg)](https://github.com/opengoodio/config-gradle-plugin/actions?query=workflow%3Arelease)
[![Codecov](https://codecov.io/gh/opengoodio/config-gradle-plugin/branch/main/graph/badge.svg?token=AEEYTGK87F)](https://codecov.io/gh/opengoodio/config-gradle-plugin)
[![CodeQL](https://github.com/opengoodio/config-gradle-plugin/actions/workflows/github-code-scanning/codeql/badge.svg)](https://github.com/opengoodio/config-gradle-plugin/actions/workflows/github-code-scanning/codeql)
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

| Property      | Description                               | Default |
|---------------|-------------------------------------------|---------|
| `projectType` | Type of project. Supported (`APP`, `LIB`) | `LIB`   |

#### Features

| Property           | Description                                                 | Default |
|--------------------|-------------------------------------------------------------|---------|
| `assertj`          | Value indicating if AssertJ dependency is enabled           | `true`  |
| `jackson`          | Value indicating if Jackson dependency is enabled           | `true`  |
| `jacksonKotlin`    | Value indicating if Jackson Kotlin dependency is enabled    | `true`  |
| `junitJupiter`     | Value indicating if JUnit Jupiter dependency is enabled     | `true`  |
| `kotest`           | Value indicating if Kotest dependencies are enabled         | `true`  |
| `kotestSpring`     | Value indicating if Kotest Spring dependency is enabled     | `true`  |
| `kotlinCoroutines` | Value indicating if Kotlin Coroutines dependency is enabled | `true`  |
| `lombok`           | Value indicating if Lombok dependency is enabled            | `true`  |
| `mockito`          | Value indicating if Mockito dependency is enabled           | `true`  |
| `mockk`            | Value indicating if MockK dependency is enabled             | `true`  |
| `spring`           | Value indicating if Spring dependencies are enabled         | `true`  |
| `springMockk`      | Value indicating if Spring MockK dependency is enabled      | `true`  |

#### Test

| Property           | Description                                  | Default                                   |
|--------------------|----------------------------------------------|-------------------------------------------|
| `maxParallelForks` | Number of concurrent test classes to execute | `Available Processors / 2 + 1`            |
| `frameworks`       | Available test frameworks to enable          | see *[Test Frameworks](#test-frameworks)* |

#### Test Frameworks

| Property | Description                                          | Default |
|----------|------------------------------------------------------|---------|
| `java`   | Value indicating if Java test frameworks are enabled | `false` |

#### Artifact

| Property       | Description                                          | Default                                           |
|----------------|------------------------------------------------------|---------------------------------------------------|
| `name`         | Name of artifact                                     | `Gradle project.name`                             |
| `packaging`    | Type of packaging. Supported (`JAR`)                 | `JAR`                                             |
| `description`  | Description of artifact                              |                                                   |
| `uri`          | URI of artifact project repository                   | `GitHub Org URI + Gradle project.name`            |
| `publications` | Publication of artifact. Supported (`OSS`, `GITHUB`) | `OSS`                                             |
| `repo`         | Artifact repository details                          | see *[Artifact Repository](#artifact-repository)* |
| `scm`          | SCM details                                          | see *[SCM](#scm)*                                 |
| `licenses`     | License details                                      | see *[License](#license)*                         |
| `developer`    | Developer details                                    | see *[Developer](#developer)*                     |

#### Artifact Repository

| Property                | Description                                | Default                                                                |
|-------------------------|--------------------------------------------|------------------------------------------------------------------------|
| `ossSnapshotsRepoUri`   | URI of OSS snapshots artifact repository   | `https://oss.sonatype.org/content/repositories/snapshots`              |
| `ossStagingRepoUri`     | URI of OSS staging artifact repository     | `https://oss.sonatype.org/service/local/staging/deploy/maven2`         |
| `gitHubPackagesRepoUri` | URI of GitHub packages artifact repository | `https://maven.pkg.github.com/ + GitHub Org URI + Gradle project.name` |

#### SCM

| Property        | Description                                | Default                                           |
|-----------------|--------------------------------------------|---------------------------------------------------|
| `provider`      | SCM provider. Supported (`GIT`)            | `GIT`                                             |
| `connection`    | Connection URI to SCM repository           | `scm:git: + GitHub Org URI + Gradle project.name` |
| `devConnection` | Developer connection URI to SCM repository | `scm:git: + GitHub Org URI + Gradle project.name` |
| `uri`           | URI of SCM repository                      | `GitHub Org URI + Gradle project.name`            |

#### License

| Property | Description     | Default                                          |
|----------|-----------------|--------------------------------------------------|
| `name`   | Name of license | `MIT`                                            |
| `uri`    | URI of license  | `GitHub Org URI + Gradle project.name + LICENSE` |

#### Developer

| Property | Description                | Default           |
|----------|----------------------------|-------------------|
| `id`     | Identifier of developer    | `opengood`        |
| `name`   | Name of developer          | `OpenGood`        |
| `email`  | Email address of developer | `dev@opengood.io` |

#### Example

```kotlin
import io.opengood.gradle.enumeration.PackagingType
import io.opengood.gradle.enumeration.ProjectType
import io.opengood.gradle.enumeration.PublicationType
import io.opengood.gradle.enumeration.ScmProvider

plugins {
    id("io.opengood.gradle.model") version "VERSION"
}

opengood {
    main {
        projectType = ProjectType.APP
    }
    features {
        assertj = false
        jackson = false
        jacksonKotlin = false
        junitJupiter = false
        kotest = false
        kotestSpring = false
        kotlinCoroutines = false
        lombok = false
        mockito = false
        mockk = false
        spring = false
        springMockk = false
    }
    test {
        maxParallelForks = 1
        frameworks {
            java = true
        }
    }
    artifact {
        name = "test"
        packaging = PackagingType.JAR
        description = "description"
        uri = "https://artifact.uri"
        publications = listOf(PublicationType.GITHUB)
        repo {
            ossSnapshotsRepoUri = "https://oss.snapshots.uri"
            ossStagingRepoUri = "https://oss.staging.uri"
            gitHubPackagesRepoUri = "https://github.packages.uri"
        }
        scm {
            provider = ScmProvider.GIT
            connection = "scm:git:https://repo.uri/project"
            devConnection = "scm:git:https://repo.uri/project"
            uri = "https://repo.uri/project"
        }
        license {
            name = "MIT"
            uri = "https://repo.uri/project/LICENSE"
        }
        developer {
            id = "developer"
            name = "Developer"
            email = "dev@example.org"
        }
    }
}
```

## Development

### Gradle

To update Gradle wrapper to the latest version, run:

```bash
./gradlew wrapper --gradle-version VERSION
```

### Versions

To update dependency and plugin versions, run:

```bash
./gradlew refreshVersions
```

### Publish

#### Plugin

**Note:** Used for publishing OpenGood Gradle plugins to Gradle plugin
repository

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

###### Maven OSS

* Using Sonatype OSS account with GPG key for publishing, add
  credentials to `~/.gradle/gradle.properties`:

    ```properties
    ossRepoUsername=<sonatype-account-username>
    ossRepoPassword=<sonatype-account-password>
    
    signing.keyId=<gpg-key-id>
    signing.password=<gpg-key-password>
    signing.secretKeyRingFile=<gpg-keys-file-path>
    ```

* Create release version and publish artifact to Maven OSS repository:

    ```bash
    ./gradlew clean release -Prelease.useAutomaticVersion=true
    ```

  **Note:** `publishOssPublicationToOssStagingRepository` tasks are
  configured with Gradle release plugin to execute after release build

###### GitHub

* Using GitHub account for publishing, add
  credentials to `~/.gradle/gradle.properties`:

    ```properties
    githubPackagesRepoPassword=<github-account-username>
    githubPackagesRepoUsername=<github-access-token>
    ```

* Create release version and publish artifact to GitHub packages
  repository:

    ```bash
    ./gradlew clean release -Prelease.useAutomaticVersion=true
    ```

  **Note:** `publishGitHubPublicationToGitHubPackagesRepository` tasks
  are configured with Gradle release plugin to execute after release
  build

##### CI/CD

###### Maven OSS

* Using Sonatype OSS account with GPG key for publishing, add
  environment variables to CI job:

    ```
    OSS_REPO_USERNAME=<sonatype-account-username>
    OSS_REPO_PASSWORD=<sonatype-account-password>
    GPG_SIGNING_PRIVATE_KEY=<gpg-key-private-key>
    GPG_SIGNING_PASSWORD=<gpg-key-password>
    ```

  **Note:** `gpg-key-private-key` must be in ASCII armored format

* Create release version and publish artifact to Maven OSS repository:

    ```bash
    git commit --allow-empty -m "Create release"; git push
    ```

###### GitHub

* Using GitHub account for publishing, add
  environment variables to CI job:

    ```
    GITHUB_ACTOR=<github-account-username>
    GITHUB_TOKEN=<github-access-token>
    ```

  *Note*: By default, these environment variables are automatically
  by added GitHub

* Create release version and publish artifact to GitHub packages
  repository:

    ```bash
    git commit --allow-empty -m "Create release"; git push
    ```

