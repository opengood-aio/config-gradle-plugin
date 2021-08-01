package io.opengood.gradle.constant

class Repositories {

    companion object {
        const val GITHUB_PACKAGES_REPO_NAME = "GitHubPackages"
        const val GITHUB_PACKAGES_REPO_URI = "https://maven.pkg.github.com"
        const val LOCAL_REPO_NAME = "Local"
        const val OSS_SNAPSHOTS_REPO_NAME = "OssSnapshots"
        const val OSS_SNAPSHOTS_REPO_URI = "https://oss.sonatype.org/content/repositories/snapshots"
        const val OSS_STAGING_REPO_NAME = "OssStaging"
        const val OSS_STAGING_REPO_URI = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
    }
}
