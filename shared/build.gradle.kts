plugins {
	skyutils.`library-conventions`
	skyutils.`shadow-conventions`
}

dependencies {
	api(libs.jsky.base)
	compileOnly(libs.jsky.yaml)
	compileOnly(libs.jetbrains.annotations)
	implementation(libs.adventure.api)
	implementation(libs.adventure.serializer.legacy)
	implementation(libs.adventure.serializer.gson)
}

tasks {
	shadowJar {
		relocate("net.kyori", "net.codersky.skyutils.shaded.kyori")
		relocate("com.google", "net.codersky.skyutils.shaded.google")
		exclude("org/jetbrains/**",
			"org/intellij/**",)
		minimize()
	}
}
