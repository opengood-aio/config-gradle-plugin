package io.opengood.gradle.constant

class Licenses {
    companion object {
        const val NAME = "MIT"
        const val RESOURCE = "LICENSE"
        const val URI = "${GitHub.OPENGOOD_ORG_URI}/%s/${GitHub.BLOB_ENDPOINT_NAME}/${GitHub.DEFAULT_BRANCH_NAME}/$RESOURCE"
    }
}
