plugins {
	skyutils.`library-conventions`
	skyutils.`shadow-conventions`
}

dependencies {
	api(libs.jsky.base)
	api(libs.jsky.yaml)
	compileOnly(libs.jetbrains.annotations)
	compileOnly(libs.adventure.api)
	compileOnly(libs.adventure.minimessage)
	compileOnly(libs.adventure.serializer.legacy)
	compileOnly(libs.adventure.serializer.gson)
}

tasks {
	shadowJar {
		exclude(
			"org/jetbrains/**",
			"org/intellij/**")
	}
}
