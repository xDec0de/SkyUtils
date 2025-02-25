pluginManagement {
	repositories {
		mavenCentral()
		gradlePluginPortal()
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			from(files("libs.versions.toml"))
		}
	}
}

rootProject.name = "SkyUtils"
include(":shared")
include(":platforms:universal")
include(":platforms:spigot")
include(":platforms:velocity")
include("platforms:universal")
include("platforms:paper")
