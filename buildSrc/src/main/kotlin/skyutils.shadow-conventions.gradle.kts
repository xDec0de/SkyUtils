plugins {
	java
	com.gradleup.shadow
}

tasks {

	jar {
		enabled = false
	}

	shadowJar {
		//relocate("org.jetbrains.annotations", "net.codersky.skyutils.shaded.jetbrains.annotations")
		//relocate("org.intellij.lang.annotations", "net.codersky.skyutils.shaded.intellij.annotations")
		archiveClassifier = null
		mergeServiceFiles()
		minimize()
	}

	assemble {
		dependsOn(shadowJar)
	}
}

configurations.implementation.get().extendsFrom(configurations.shadow.get())
