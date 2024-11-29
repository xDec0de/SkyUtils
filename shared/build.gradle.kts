plugins {
	skyutils.`shadow-conventions`
	skyutils.`library-conventions`
}

dependencies {
	compileOnly(libs.jetbrains.annotations)
	compileOnly(libs.adventure.serializer.legacy)
	compileOnly(libs.snakeyaml)
}
