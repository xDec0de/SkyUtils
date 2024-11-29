plugins {
	skyutils.`shadow-conventions`
	skyutils.`library-conventions`
}

repositories {
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	api(project(":shared"))
	implementation(libs.snakeyaml)
	compileOnly(libs.velocity)
}

tasks {
	shadowJar {
		relocate("org.yaml.snakeyaml", "net.codersky.skyutils.shaded.snakeyaml")
	}
}
