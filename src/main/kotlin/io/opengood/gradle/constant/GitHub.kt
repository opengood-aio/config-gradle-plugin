package io.opengood.gradle.constant

class GitHub {
    companion object {
        const val BLOB_ENDPOINT_NAME = "blob"
        const val DEFAULT_BRANCH_NAME = "main"
        const val OPENGOOD_ORG_NAME = "opengood-aio"
        const val OPENGOOD_ORG_URI = "https://github.com/$OPENGOOD_ORG_NAME"
        const val OPENGOOD_REPO_URI = "$OPENGOOD_ORG_URI/%s"
    }
}
