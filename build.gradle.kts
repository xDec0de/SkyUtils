
group = "net.codersky"
version = "1.0.0-SNAPSHOT"
description = "An open source collection of utilities for Minecraft plugins designed to make your life easier"

plugins {
	java
	`maven-publish`
}

tasks {
	wrapper {
		gradleVersion = "8.8"
		distributionType = Wrapper.DistributionType.ALL
	}

	val libsPath = "libs"

	// Configure the existing build task
	named("build") {

		dependsOn(subprojects.filter { it.name != "platforms" }.map { it.tasks.named("build") })

		doLast {
			val buildOut = project.layout.buildDirectory.dir(libsPath).get().asFile.apply {
				if (!exists()) mkdirs()
			}

			subprojects.forEach { subproject ->
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

	// Configure the existing clean task
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

	if (name == "platforms")
		return@subprojects

	publishing {
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
		}

		publications {
			create<MavenPublication>("maven") {
				groupId = "${rootProject.group}.${project.group.toString().lowercase()}"
				artifactId = project.name // Use the subproject name as the artifactId

				// Include the main JAR
				artifact(tasks["shadowJar"])
				// Include the sources JAR
				artifact(tasks["sourcesJar"])
			}
		}
	}
}
