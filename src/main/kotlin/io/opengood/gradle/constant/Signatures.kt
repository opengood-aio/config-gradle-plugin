package io.opengood.gradle.constant

class Signatures {

    companion object {
        const val JAR = "%s-%s.jar.asc:jar.asc:asc:asc:null"
        const val JAVADOC = "%s-%s-javadoc.jar.asc:jar.asc:asc:asc:null"
        const val POM = "pom-default.xml.asc:xml.asc:asc:null"
        const val SOURCES = "%s-%s-sources.jar.asc:jar.asc:asc:asc:null"
    }
}
