plugins {
	skyutils.`shadow-conventions`
	skyutils.`library-conventions`
}

repositories {
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	api(project(":shared"))
	api(libs.adventure.serializer.legacy)
	api(libs.snakeyaml)
}

tasks {
	shadowJar {
		relocate("net.kyori", "net.codersky.skyutils.shaded.kyori")
		relocate("org.yaml.snakeyaml", "net.codersky.skyutils.shaded.snakeyaml")
	}
}
