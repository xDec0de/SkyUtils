plugins {
	skyutils.`shadow-conventions`
	skyutils.`library-conventions`
	alias(libs.plugins.run.paper)
}

repositories {
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	compileOnly(project(":platforms:paper"))
	compileOnly(libs.paper)
}

tasks {
	val version = "1.21.4"
	val jvmArgsExternal = listOf("-Dcom.mojang.eula.agree=true")
	runServer {
		doFirst {
			val pluginDir = rootDir.resolve("run/paper/$version/plugins")
			copy {
				from(project(":platforms:paper").tasks.named("shadowJar"))
				into(pluginDir)
			}
		}
		minecraftVersion(version)
		runDirectory = rootDir.resolve("run/paper/$version")
		jvmArgs = jvmArgsExternal
	}
}
