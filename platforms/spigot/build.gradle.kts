plugins {
	skyutils.`shadow-conventions`
	skyutils.`library-conventions`
	alias(libs.plugins.run.paper)
}

repositories {
	maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
	api(project(":shared"))
	implementation(libs.adventure.api)
	implementation(libs.adventure.serializer.legacy)
	implementation(libs.adventure.serializer.gson)
	compileOnly(libs.spigot)
	compileOnly(libs.jetbrains.annotations)
}

tasks {
	shadowJar {
		relocate("net.kyori.adventure", "net.codersky.skyutils.shaded.adventure")
		relocate("net.kyori", "net.codersky.skyutils.shaded.kyori")
		relocate("com.google.gson", "net.codersky.skyutils.shaded.gson")
		exclude("org/jetbrains/**",
			"org/intellij/**",
			"org/yaml/**")
		minimize()
	}
}
