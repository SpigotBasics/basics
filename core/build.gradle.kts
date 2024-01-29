plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    id("basics.dependency.placeholderapi")
    id("org.jetbrains.dokka") version "1.9.10"
    id("com.github.johnrengelman.shadow")
}

val exposedVersion: String by project
dependencies {
    implementation(libs.adventure.api)
    implementation(libs.adventure.bukkit)
    implementation(libs.adventure.minimessage)
    implementation(libs.adventure.text.serializer.legacy)
    implementation(libs.paperlib)
    implementation(libs.folialib)
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    api(project(":common"))
    api(project(":pipe:facade"))
}


tasks.processResources {
    filesMatching("rusty-spigot-threshold") {
        expand("version" to libs.versions.spigot.get())
    }
}

tasks.shadowJar {
    archiveClassifier = "shaded"
    for (path in listOf(
        "net.kyori",
        "io.papermc.lib",
        "org.intellij",
        "org.jetbrains",
        "com.tcoded.folialib"
    )) {
        relocate(path, "$SHADED.$path")
    }

    exclude("kotlin/**")
}