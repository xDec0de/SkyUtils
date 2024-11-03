import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
	java
	com.gradleup.shadow
}

tasks {
	shadowJar {
		archiveFileName = "MCUtils-" + project.name.uppercaseFirstChar() + "-${rootProject.version}.jar"
		archiveClassifier = null

		relocate("org.jetbrains.annotations", "net.codersky.mcutils.shaded.jetbrains.annotations")
		relocate("org.intellij.lang.annotations", "net.codersky.mcutils.shaded.intellij.annotations")

		mergeServiceFiles()
		minimize()
	}

	assemble {
		dependsOn(shadowJar)
	}
}

configurations.implementation.get().extendsFrom(configurations.shadow.get())
