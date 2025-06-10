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

// Server platforms
include(":platforms:spigot")
include(":platforms:paper")

// Proxy platforms
include(":platforms:velocity")

// Testing
include(":test-plugins:paper")
