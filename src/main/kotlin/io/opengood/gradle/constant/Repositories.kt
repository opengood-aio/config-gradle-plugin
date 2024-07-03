package io.opengood.gradle.constant

class Repositories {
    companion object {
        const val GITHUB_PACKAGES_REPO_BASE_URI = "https://maven.pkg.github.com"
        const val GITHUB_PACKAGES_REPO_NAME = "gitHubPackages"
        const val GITHUB_PACKAGES_REPO_URI = "$GITHUB_PACKAGES_REPO_BASE_URI/${GitHub.OPENGOOD_ORG_NAME}/%s"
        const val LOCAL_REPO_NAME = "local"
        const val MAVEN_CENTRAL_REPO_NAME = "mavenCentral"
    }
}
