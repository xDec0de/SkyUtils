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
