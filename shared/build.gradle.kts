plugins {
	mcutils.`shadow-conventions`
	mcutils.`library-conventions`
}

dependencies {
	compileOnly(libs.jetbrains.annotations)
	compileOnly(libs.adventure.serializer.legacy)
	compileOnly(libs.snakeyaml)
}
