package io.opengood.gradle

import helper.expectedProperty
import helper.getPlugin
import io.kotest.core.spec.style.funSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.types.shouldBeTypeOf
import io.opengood.gradle.constant.Artifacts
import io.opengood.gradle.constant.GitHub
import io.opengood.gradle.constant.Repositories
import io.opengood.gradle.constant.Tests
import io.opengood.gradle.enumeration.PackagingType
import io.opengood.gradle.enumeration.ScmProvider
import io.opengood.gradle.extension.opengood
import org.gradle.api.Project

fun applyPluginTest(project: Project) = funSpec {
    test("Applies plugin") {
        getPlugin(project, ConfigPlugin.PLUGIN_ID).shouldNotBeNull()
        getPlugin(project, ConfigPlugin.PLUGIN_ID).shouldBeTypeOf<ConfigPlugin>()
        getPlugin<ConfigPlugin>(project).shouldBeTypeOf<ConfigPlugin>()
    }
}

fun createExtensionTest(
    project: Project,
    properties: Map<String, Any>
) = funSpec {
    test("Creates plugin extension") {
        with(project.opengood()) {
            with(main) {
                languageType shouldBe project.languageType
                projectType shouldBe expectedProperty(properties, "projectType")
            }
            with(repo) {
                name shouldBe project.name
                baseUri shouldBe GitHub.OPENGOOD_ORG_URI
            }
            with(test) {
                maxParallelForks shouldBe Tests.MAX_PARALLEL_FORKS
            }
            with(artifact) {
                archiveBaseName shouldBe project.name
                name shouldBe project.name
                packaging shouldBe PackagingType.JAR
                description.shouldBeEmpty()
                uri shouldBe "${repo.baseUri}/${repo.name}"
                snapshotsUri shouldBe Repositories.OSS_SNAPSHOTS_REPO_URI
                stagingUri shouldBe Repositories.OSS_STAGING_REPO_URI
                with(scm) {
                    provider shouldBe ScmProvider.GIT
                    connection shouldBe "${ScmProvider.PROTOCOL}:$provider:${repo.baseUri}/${repo.name}"
                    developerConnection shouldBe "${ScmProvider.PROTOCOL}:$provider:${repo.baseUri}/${repo.name}"
                    uri shouldBe "${repo.baseUri}/${repo.name}"
                }
                with(licenses) {
                    if (isNotEmpty()) {
                        with(first()) {
                            id shouldBe Artifacts.LICENSE_ID
                            name shouldBe Artifacts.LICENSE_NAME
                            uri shouldBe "${repo.baseUri}/${repo.name}/${GitHub.RESOURCE_ENDPOINT}/${GitHub.BRANCH}/${Artifacts.LICENSE_RESOURCE}"
                        }
                    }
                }
                with(developers) {
                    if (isNotEmpty()) {
                        with(first()) {
                            id shouldBe Artifacts.DEVELOPER_ID
                            name shouldBe Artifacts.DEVELOPER_NAME
                            email shouldBe Artifacts.DEVELOPER_EMAIL
                        }
                    }
                }
            }
        }
    }
}
