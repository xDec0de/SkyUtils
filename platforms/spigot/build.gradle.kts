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
	// TODO: Start - Remove when migration to SkyMessage is done.
	implementation(libs.adventure.api)
	implementation(libs.adventure.serializer.bungee)
	implementation(libs.adventure.serializer.legacy)
	// TODO: End - Remove when migration to SkyMessage is done.
	compileOnly(libs.spigot)
	compileOnly(libs.jetbrains.annotations)
}
