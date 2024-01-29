import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Transformer
import com.github.jengelman.gradle.plugins.shadow.transformers.TransformerContext
import org.apache.tools.zip.ZipOutputStream
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    id("basics.dependency.placeholderapi")
    id("org.jetbrains.dokka") version "1.9.10"
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(libs.adventure.api)
    implementation(libs.adventure.bukkit)
    implementation(libs.adventure.minimessage)
    implementation(libs.adventure.text.serializer.legacy)
    api(project(":common"))
    api(project(":pipe:facade"))
    implementation(libs.paperlib)
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
        "io.papermc.lib",
        "org.intellij",
        "org.jetbrains"
        ))
        relocate(path, "$SHADED.$path")

    exclude("kotlin/**")
}