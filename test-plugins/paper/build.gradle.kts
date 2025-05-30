plugins {
	skyutils.`shadow-conventions`
	skyutils.`library-conventions`
	alias(libs.plugins.run.paper)
}

repositories {
	maven("https://repo.papermc.io/repository/maven-public/")
}

val paperProject = project(":platforms:paper")

dependencies {
	compileOnly(project(":shared"))
	compileOnly(paperProject)
	compileOnly(libs.paper)
}

tasks {
	val version = "1.21.4"
	val jvmArgsExternal = listOf("-Dcom.mojang.eula.agree=true")
	runServer {
		val paperShadowJar = paperProject.tasks.named("shadowJar")
		dependsOn(named("build"))
		dependsOn(paperShadowJar)
		doFirst {
			val pluginDir = rootDir.resolve("run/paper/$version/plugins")
			copy {
				from(paperShadowJar)
				into(pluginDir)
			}
		}
		minecraftVersion(version)
		runDirectory = rootDir.resolve("run/paper/$version")
		jvmArgs = jvmArgsExternal
	}
}
