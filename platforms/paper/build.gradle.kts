plugins {
	skyutils.`shadow-conventions`
	skyutils.`library-conventions`
}

repositories {
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	api(project(":platforms:spigot"))
	compileOnly(libs.paper)
	compileOnly(libs.jetbrains.annotations)
}

tasks {
	shadowJar {
		relocate("net.codersky.skyutils.shaded.kyori", "net.kyori")
		relocate("com.google.gson", "net.codersky.skyutils.shaded.gson")
		exclude("org/jetbrains/**",
			"org/intellij/**",
			"org/yaml/**",
			"net/kyori/**") // TODO: Doesn't remove it for some reason
		minimize()
	}
}
