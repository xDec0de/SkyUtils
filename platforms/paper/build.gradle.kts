plugins {
	mcutils.`shadow-conventions`
	mcutils.`library-conventions`
	alias(libs.plugins.run.paper)
}

repositories {
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	api(project(":platforms:spigot")) {
		setTransitive(false)
	}
	compileOnly(libs.paper)
}

tasks {

	shadowJar {
		// Exclude dependencies included by Paper, so they don't get added into the jar
		val shaded = "net/codersky/mcutils/shaded/"
		exclude(shaded + "kyori/**", shaded + "jetbrains/**", shaded + "intellij/**")
		// Relocate included dependencies back to their original packages
		relocate("net.codersky.mcutils.shaded.kyori", "net.kyori")
		relocate("net.codersky.mcutils.shaded.jetbrains", "org.jetbrains")
		relocate("net.codersky.mcutils.shaded.intellij", "org.intellij")
	}

	configurations {
		runtimeOnly {
			exclude("net.codersky.mcutils.shaded.kyori")
		}
	}

	// 1.8.8 - 1.16.5 = Java 8
	// 1.17           = Java 16
	// 1.18 - 1.20.4  = Java 17
	// 1-20.5+        = Java 21
	val version = "1.21"
	val javaVersion = JavaLanguageVersion.of(21)

	val jvmArgsExternal = listOf(
		"-Dcom.mojang.eula.agree=true"
	)

	val sharedBukkitPlugins = runPaper.downloadPluginsSpec {
		url("https://github.com/ViaVersion/ViaVersion/releases/download/5.0.1/ViaVersion-5.0.1.jar")
		url("https://github.com/ViaVersion/ViaBackwards/releases/download/5.0.1/ViaBackwards-5.0.1.jar")
	}

	runServer {
		minecraftVersion(version)
		runDirectory = rootDir.resolve("run/paper/$version")

		javaLauncher = project.javaToolchains.launcherFor {
			languageVersion = javaVersion
		}

		downloadPlugins {
			from(sharedBukkitPlugins)
			url("https://ci.lucko.me/job/spark/418/artifact/spark-bukkit/build/libs/spark-1.10.73-bukkit.jar")
			url("https://download.luckperms.net/1549/bukkit/loader/LuckPerms-Bukkit-5.4.134.jar")
		}

		jvmArgs = jvmArgsExternal
	}

	runPaper.folia.registerTask {
		minecraftVersion(version)
		runDirectory = rootDir.resolve("run/folia/$version")

		javaLauncher = project.javaToolchains.launcherFor {
			languageVersion = javaVersion
		}

		downloadPlugins {
			from(sharedBukkitPlugins)
		}

		jvmArgs = jvmArgsExternal
	}
}
