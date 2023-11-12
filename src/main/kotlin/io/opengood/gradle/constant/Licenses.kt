package io.opengood.gradle.constant

import io.opengood.gradle.constant.GitHub.Companion.BLOB_ENDPOINT_NAME
import io.opengood.gradle.constant.GitHub.Companion.DEFAULT_BRANCH_NAME
import io.opengood.gradle.constant.GitHub.Companion.OPENGOOD_ORG_URI

class Licenses {
    companion object {
        const val NAME = "MIT"
        const val RESOURCE = "LICENSE"
        const val URI = "$OPENGOOD_ORG_URI/%s/$BLOB_ENDPOINT_NAME/$DEFAULT_BRANCH_NAME/$RESOURCE"
    }
}
