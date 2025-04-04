plugins {
	skyutils.`shadow-conventions`
	skyutils.`library-conventions`
}

repositories {
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	api(project(":shared"))
	compileOnly(libs.velocity)
	api(libs.jsky.base)
	api(libs.jsky.yaml)
}

tasks {
	shadowJar {
		relocate("org.yaml.snakeyaml", "net.codersky.skyutils.shaded.snakeyaml")
	}
}
