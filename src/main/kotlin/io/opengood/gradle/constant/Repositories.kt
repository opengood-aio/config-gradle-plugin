package io.opengood.gradle.constant

import io.opengood.gradle.constant.GitHub.Companion.OPENGOOD_ORG_NAME

class Repositories {
    companion object {
        const val GITHUB_PACKAGES_REPO_BASE_URI = "https://maven.pkg.github.com"
        const val GITHUB_PACKAGES_REPO_NAME = "GitHubPackages"
        const val GITHUB_PACKAGES_REPO_URI = "$GITHUB_PACKAGES_REPO_BASE_URI/$OPENGOOD_ORG_NAME/%s"
        const val LOCAL_REPO_NAME = "Local"
        const val MAVEN_CENTRAL_PORTAL_REPO_BASE_URI = "https://central.sonatype.com"
        const val MAVEN_CENTRAL_PORTAL_SNAPSHOTS_REPO_NAME = "MavenCentralPortalSnapshots"
        const val MAVEN_CENTRAL_PORTAL_SNAPSHOTS_REPO_URI = "$MAVEN_CENTRAL_PORTAL_REPO_BASE_URI/api/v1/publisher"
        const val MAVEN_CENTRAL_PORTAL_STAGING_REPO_NAME = "MavenCentralPortalStaging"
        const val MAVEN_CENTRAL_PORTAL_STAGING_REPO_URI = "$MAVEN_CENTRAL_PORTAL_REPO_BASE_URI/api/v1/publisher"
    }
}
