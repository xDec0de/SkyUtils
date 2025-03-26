plugins {
	skyutils.`library-conventions`
	skyutils.`shadow-conventions`
}

dependencies {
	// TODO: Implement JSky once the Codersky repository is back online.
	// api(libs.jsky)
	compileOnly(libs.jetbrains.annotations)
	compileOnly(libs.adventure.serializer.legacy)
	compileOnly(libs.snakeyaml)
}
