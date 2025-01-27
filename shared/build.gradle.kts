plugins {
	skyutils.`library-conventions`
	skyutils.`shadow-conventions`
}

dependencies {
	api(libs.jsky)
	compileOnly(libs.jetbrains.annotations)
	compileOnly(libs.adventure.serializer.legacy)
	compileOnly(libs.snakeyaml)
}
