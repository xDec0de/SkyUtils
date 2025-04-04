plugins {
	skyutils.`library-conventions`
	skyutils.`shadow-conventions`
}

dependencies {
	compileOnly(libs.jsky.base)
	compileOnly(libs.jsky.yaml)
	compileOnly(libs.jetbrains.annotations)
	compileOnly(libs.adventure.serializer.legacy)
}
