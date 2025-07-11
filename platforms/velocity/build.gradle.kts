plugins {
	skyutils.`shadow-conventions`
	skyutils.`library-conventions`
}

repositories {
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	api(project(":shared"))
	compileOnly(libs.adventure.api)
	compileOnly(libs.adventure.minimessage)
	compileOnly(libs.velocity)
	compileOnly(libs.jetbrains.annotations)
}

tasks.shadowJar {
	relocate("org.yaml.snakeyaml", "net.codersky.skyutils.shaded.snakeyaml")
}
