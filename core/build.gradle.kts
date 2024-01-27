import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Transformer
import com.github.jengelman.gradle.plugins.shadow.transformers.TransformerContext
import org.apache.tools.zip.ZipOutputStream
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    id("basics.dependency.acf")
    id("basics.dependency.placeholderapi")
    id("org.jetbrains.dokka") version "1.9.10"
    id("com.github.johnrengelman.shadow")
    //libs.plugins.dokka // TODO: Fix this not working because of missing import
}

dependencies {
//    api("net.kyori:adventure-api:4.14.0")
//    api("net.kyori:adventure-platform-bukkit:4.3.1")
    implementation(libs.adventure.api)
    implementation(libs.adventure.bukkit)
    implementation(libs.adventure.minimessage)
    implementation(libs.adventure.text.serializer.legacy)
    api(project(":pipe"))
    api(libs.paperlib)
}

tasks.withType(DokkaTask::class).configureEach {
    dokkaSourceSets.configureEach {
        //includeNonPublic.set(false)
    }
}

tasks.processResources {
    filesMatching("rusty-spigot-threshold") {
        expand("version" to libs.versions.spigot.get())
    }
}

tasks.shadowJar {
    val shaded = "shaded"
    archiveClassifier = "shaded"
    relocate("net.kyori", "$shaded.net.kyori")

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