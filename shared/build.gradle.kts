plugins {
	mcutils.`shadow-conventions`
	mcutils.`library-conventions`
}

dependencies {
	api(libs.jetbrains.annotations)
	compileOnly(libs.adventure)
	compileOnly(libs.adventure.legacy)
	compileOnly(libs.snakeyaml)
}