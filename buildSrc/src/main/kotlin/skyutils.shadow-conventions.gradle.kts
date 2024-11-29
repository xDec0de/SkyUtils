import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
	java
	com.gradleup.shadow
}

tasks {
	shadowJar {
		archiveFileName = "SkyUtils-" + project.name.uppercaseFirstChar() + "-${rootProject.version}.jar"
		archiveClassifier = null

		relocate("org.jetbrains.annotations", "net.codersky.skyutils.shaded.jetbrains.annotations")
		relocate("org.intellij.lang.annotations", "net.codersky.skyutils.shaded.intellij.annotations")

		mergeServiceFiles()
		minimize()
	}

	assemble {
		dependsOn(shadowJar)
	}
}

configurations.implementation.get().extendsFrom(configurations.shadow.get())
