plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    id("basics.dependency.placeholderapi")
    id("org.jetbrains.dokka") version "1.9.10"
    id("com.github.johnrengelman.shadow")
}

dependencies {
    api(libs.gson)
    testImplementation(libs.gson)
    implementation(libs.hikari)
    implementation(libs.adventure.api)
    implementation(libs.adventure.bukkit)
    implementation(libs.adventure.minimessage)
    implementation(libs.adventure.text.serializer.legacy)
    implementation(libs.paperlib)
    implementation(libs.folialib)
    api(project(":common"))
    api(project(":pipe:facade"))


}


tasks.processResources {
    filesMatching("rusty-spigot-threshold") {
        expand("version" to libs.versions.spigot.get())
    }
}