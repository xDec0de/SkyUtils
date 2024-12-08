group = "net.codersky"
version = "1.0.0-SNAPSHOT"
description = "An open source collection of utilities for Minecraft plugins designed to make your life easier"

plugins {
	java
	`maven-publish`
	com.gradleup.shadow
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
	apply(plugin = "java")
	apply(plugin = "maven-publish")
	apply(plugin = "com.gradleup.shadow")

	version = rootProject.version

	// Create a sources JAR task for each subproject
	tasks.register<Jar>("sourcesJar") {
		archiveClassifier.set("sources")
		from(sourceSets.main.get().allSource)
	}

	if (name != "platforms") {
		publishing {
			publishing {
				repositories {
					maven {
						val snapshot = version.toString().endsWith("SNAPSHOT")
						url =
							uri(if (snapshot) "https://repo.codersky.net/snapshots" else "https://repo.codersky.net/releases")
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
					//version = version

					// Include the main JAR
					artifact(tasks.shadowJar.get())
					// Include the sources JAR
					artifact(tasks["sourcesJar"])
				}
			}
		}
	}
}