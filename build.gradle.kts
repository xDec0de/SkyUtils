group = "net.codersky"
version = "1.0.0-SNAPSHOT"
description = "An open source collection of utilities for Minecraft plugins designed to make your life easier"

plugins {
	java
	`maven-publish`
}

// Disable jar task for root project
tasks.named<Jar>("jar") {
	enabled = false
}

tasks {
	wrapper {
		gradleVersion = "8.8"
		distributionType = Wrapper.DistributionType.ALL
	}

	val libsPath = "libs"

	named("build") {

		dependsOn(subprojects.filter { it.name != "platforms" }.map { it.tasks.named("build") })

		doLast {
			val buildOut = project.layout.buildDirectory.dir(libsPath).get().asFile.apply {
				if (!exists()) mkdirs()
			}

			subprojects.filter { it.name != "platforms" }.forEach { subproject ->
				val subIn = subproject.layout.buildDirectory.dir(libsPath).get().asFile
				if (subIn.exists()) {
					copy {
						from(subIn) {
							include("SkyUtils-*.jar")
							exclude("*-javadoc.jar", "*-sources.jar")
						}
						into(buildOut)
					}
				}
			}
		}
	}

	named("clean") {
		dependsOn(subprojects.filter { it.name != "platforms" }.map { it.tasks.named("clean") })
		doFirst {
			delete(rootProject.layout.buildDirectory)
		}
	}

	defaultTasks("build")
}

subprojects {

	apply(plugin = "maven-publish")
	apply(plugin = "skyutils.shadow-conventions")
	apply(plugin = "skyutils.library-conventions")

	version = rootProject.version

	// Disable all tasks for the "platforms" subproject
	if (name == "platforms") {
		tasks.forEach { it.enabled = false }
		return@subprojects
	}

	publishing {
		repositories {
			maven {
				val snapshot = version.toString().endsWith("SNAPSHOT")
				url = uri("https://repo.codersky.net/" + if (snapshot) "snapshots" else "releases")
				name = if (snapshot) "cskSnapshots" else "cskReleases"
				credentials(PasswordCredentials::class)
				authentication {
					create<BasicAuthentication>("basic")
				}
			}
		}

		publications {
			create<MavenPublication>("maven") {
				groupId = "${rootProject.group}.${project.group.toString().lowercase()}"
				artifactId = project.name // Use the subproject name as the artifactId

				pom {
					packaging = "jar"
				}

				// Include the main JAR
				artifact(tasks["shadowJar"]) {
					classifier = ""
				}
				// Include the sources JAR
				artifact(tasks["sourcesJar"])
			}
		}
	}
}
