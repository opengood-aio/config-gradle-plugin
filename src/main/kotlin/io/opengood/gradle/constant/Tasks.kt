package io.opengood.gradle.constant

class Tasks {
    companion object {
        const val AFTER_RELEASE_BUILD = "afterReleaseBuild"
        const val COMPILE_JAVA = "compileJava"
        const val COMPILE_KOTLIN = "compileKotlin"
        const val JACOCO_TEST_REPORT = "jacocoTestReport"
        const val JAVADOC_JAR = "javadocJar"
        const val PUBLISH_ALL_PUBLICATIONS_TO_MAVEN_CENTRAL_REPOSITORY = "publishAllPublicationsToMavenCentralRepository"
        const val PUBLISH_AND_RELEASE_TO_MAVEN_CENTRAL = "publishAndReleaseToMavenCentral"
        const val PUBLISH_PUBLICATION = "publish%sPublicationTo%sRepository"
        const val PUBLISH_TO_MAVEN_CENTRAL = "publishToMavenCentral"
        const val SIGN_PUBLICATION = "sign%sPublication"
        const val SOURCES_JAR = "sourcesJar"
        const val TEST = "test"
    }
}
