plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    id("basics.dependency.acf")
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
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    api(project(":common"))
    api(project(":pipe:facade"))
    api(libs.paperlib)
}


tasks.processResources {
    filesMatching("rusty-spigot-threshold") {
        expand("version" to libs.versions.spigot.get())
    }
}

tasks.shadowJar {
    archiveClassifier = "shaded"
    for(path in listOf(
        "net.kyori",
        "co.aikar",
        "io.papermc.lib",
        "org.intellij",
        "org.jetbrains"
        ))
//    relocate("net.kyori", "$SHADED.net.kyori")
//    relocate("co.aikar", "$SHADED.co.aikar")
//    relocate("io.papermc.lib", "$SHADED.io.papermc.lib")
//    relocate("org.intellij", "$SHADED.org.intellij")
//    relocate("org.jetbrains", "$SHADED.org.jetbrains")
        relocate(path, "$SHADED.$path")

    exclude("kotlin/**")


    //exclude("com/github/spigotbasics/pipe/JoinQuitEventPipe.class")
    //finalizedBy(tasks.getByName("shadowPipe"))
}

//tasks.register("shadowPipe", ShadowJar::class) {
//    group = "shadow"
//    from(tasks.shadowJar.get().outputs)
//    from(project(":pipe").tasks.getByName("jar"))
//    archiveClassifier = "shaded"
//    //mustRunAfter(tasks.shadowJar)
//}
