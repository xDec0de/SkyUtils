plugins {
	skyutils.`shadow-conventions`
	skyutils.`library-conventions`
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
}

tasks {
	shadowJar {
		exclude("org/yaml/snakeyaml/**",
			"org/jetbrains/**",
			"org/intellij/**",
			"net/codersky/skyutils/shaded/kyori/**")
	}
}
