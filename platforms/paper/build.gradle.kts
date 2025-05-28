plugins {
	skyutils.`shadow-conventions`
	skyutils.`library-conventions`
	alias(libs.plugins.run.paper)
}

repositories {
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	api(project(":shared"))
	api(project(":platforms:spigot")) {
		setTransitive(false)
	}
	api(libs.jsky.base)
	api(libs.jsky.yaml)
	compileOnly(libs.paper)
	// Test
	testImplementation(project(":shared"))
	testImplementation(project(":platforms:spigot")) {
		setTransitive(false)
	}
	testImplementation(libs.jsky.base)
	testImplementation(libs.jsky.yaml)
	testCompileOnly(libs.paper)
}

tasks {

	shadowJar {
		exclude("org/yaml/snakeyaml/**",
			"org/jetbrains/**",
			"org/intellij/**",
			"net/codersky/skyutils/shaded/kyori/**")
	}

	val version = "1.21.4"

	val jvmArgsExternal = listOf(
		"-Dcom.mojang.eula.agree=true"
	)

	runServer {
		minecraftVersion(version)
		runDirectory = rootDir.resolve("run/paper/$version")
		jvmArgs = jvmArgsExternal
	}

	runPaper.folia.registerTask {
		minecraftVersion(version)
		runDirectory = rootDir.resolve("run/folia/$version")
		jvmArgs = jvmArgsExternal
	}
}
