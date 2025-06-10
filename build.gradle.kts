
group = "net.codersky"
version = "1.0.0-SNAPSHOT"
description = "An open source collection of utilities for Minecraft plugins designed to make your life easier"

plugins {
	java
	`maven-publish`
}

/*
 - Root project configuration
 */

tasks {
	// Disable jar task
	jar { enabled = false }

	wrapper {
		gradleVersion = "8.8"
		distributionType = Wrapper.DistributionType.ALL
	}

	named("build") {
		dependsOn(subprojects.map { it.tasks.named("build") })

		doLast {
			val buildOut = layout.buildDirectory.dir("libs").get().asFile
				.apply { if (!exists()) mkdirs() }

			subprojects.forEach { subproject ->
				if (subproject.parent?.name == "SkyUtils")
					return@forEach

				val subIn = subproject.layout.buildDirectory.dir("libs").get().asFile
				subIn.listFiles()
					?.filter { it.extension == "jar" }
					?.forEach { jarFile ->
						copy {
							from(jarFile)
							into(buildOut)
							var name = subproject.name;
							if (subproject.parent?.name == "test-plugins")
								name += "-test"
							rename { "SkyUtils-${name}-${version}.jar" }
						}
					}
					?: println("No JAR found in subproject: ${subproject.name}")
			}
		}
	}

	named("clean") {
		dependsOn(subprojects.map { it.tasks.named("clean") })
		doFirst {
			delete(layout.buildDirectory)
		}
	}

	defaultTasks("build")
}

/*
 - Subproject configuration
 */

subprojects {

	apply(plugin = "maven-publish")
	apply(plugin = "skyutils.shadow-conventions")
	apply(plugin = "skyutils.library-conventions")

	version = rootProject.version

	// Disable all tasks for container projects
	if (project.name == "platforms" || project.name == "test-plugins") {
		tasks.configureEach { enabled = false }
		return@subprojects
	}

	// Skip publishing setup for test-plugins
	if (project.parent?.name == "test-plugins") {
		return@subprojects
	}

	publishing {
		repositories {
			maven {
				val isSnapshot = version.toString().endsWith("SNAPSHOT")
				name = if (isSnapshot) "cskSnapshots" else "cskReleases"
				url = uri("https://repo.codersky.net/${if (isSnapshot) "snapshots" else "releases"}")

				credentials(PasswordCredentials::class)
				authentication { create<BasicAuthentication>("basic") }
			}
		}

		publications {
			create<MavenPublication>("maven") {
				groupId = "${rootProject.group}.${project.group.toString().lowercase()}"
				artifactId = project.name

				pom { packaging = "jar" }

				artifact(tasks.named("shadowJar").get()) {
					classifier = "" // Avoid "-all" suffix
				}

				artifact(tasks.named("sourcesJar").get())
			}
		}
	}
}
