plugins {
    java
    com.gradleup.shadow
}

tasks {
    shadowJar {
        archiveClassifier = null
		archiveFileName = "MCUtils-" + project.name.uppercaseFirstChar() + "-${rootProject.version}.jar"

        relocate("org.jetbrains.annotations", "net.codersky.mcutils.shaded.jetbrains.annotations")
        relocate("org.intellij.lang.annotations", "net.codersky.mcutils.shaded.intellij.annotations")
        relocate("net.kyori", "net.codersky.mcutils.shaded.kyori")
        relocate("org.yaml.snakeyaml", "net.codersky.mcutils.shaded.snakeyaml")

        mergeServiceFiles()
        minimize()
    }

    assemble {
        dependsOn(shadowJar)
    }
}

configurations.implementation.get().extendsFrom(configurations.shadow.get())