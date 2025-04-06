plugins {
	java
	com.gradleup.shadow
}

tasks {

	jar {
		enabled = false
	}

	shadowJar {
		archiveClassifier = null
		mergeServiceFiles()
		minimize()
	}

	assemble {
		dependsOn(shadowJar)
	}
}

configurations.implementation.get().extendsFrom(configurations.shadow.get())
