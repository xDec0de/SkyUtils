group = "net.codersky"
version = "1.0.0"
description = "An open source collection of utilities for Minecraft plugins designed to make your life easier"

tasks {
	wrapper {
		gradleVersion = "8.8"
		distributionType = Wrapper.DistributionType.ALL
	}

	fun subModuleTasks(taskName: String): List<Task> {
		return subprojects
			.filter { it.name != "platforms" }
			.mapNotNull { it.tasks.findByName(taskName) }
	}

	val buildTask = "build"
	val libsPath = "libs"

	register(buildTask) {
		val subModuleBuildTasks = subModuleTasks(buildTask)
		dependsOn(subModuleBuildTasks)
		group = buildTask

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

	register<Delete>("clean") {
		val cleanTasks = subModuleTasks("clean")
		dependsOn(cleanTasks)
		group = buildTask
		delete(rootProject.layout.buildDirectory)
	}

	defaultTasks(buildTask)
}