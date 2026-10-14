plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    id("basics.dependency.placeholderapi")
    id("org.jetbrains.dokka") version "1.9.10"
    id("com.github.johnrengelman.shadow")
}

// api            = shaded, exposed to modules
// apiCompileOnly = not shaded, exposed to modules
// implementation = shaded, internal use only
// compileOnly    = not shaded, internal use only
//
dependencies {
    implementation(libs.hikari)
    implementation(libs.adventure.api)
    implementation(libs.adventure.bukkit)
    implementation(libs.adventure.minimessage)
    implementation(libs.adventure.text.serializer.legacy)
    implementation(libs.paperlib)
    implementation(libs.folialib)
    compileOnly(libs.placeholderapi)
    api(project(":common"))
    api(project(":pipe:facade"))
    api(project(":nms:facade"))
    testImplementation(libs.gson)
    compileOnlyApi(libs.gson)
}

tasks.shadowJar {
    listOf(
        "net.kyori",
        "com.tcoded",
        "com.zaxxer",
        "io.papermc.lib",
    ).forEach {
        relocate(it, "com.github.spigotbasics.shaded.$it")
    }
}

tasks.processResources {
    filesMatching("rusty-spigot-threshold") {
        expand("version" to libs.versions.spigot.get())
    }
}

tasks.register("deployDocs", Exec::class) {
    dependsOn("dokkaHtml", "dokkaJavadoc")

    group = "basics"

    doFirst {
        // Access the build directory properly to avoid deprecation warnings
        val buildDirectory = project.layout.buildDirectory.dir("dokka")

        // Define the source directories for dokkaHtml and dokkaJavadoc outputs
        val dokkaHtmlDir = buildDirectory.map { it.dir("html").asFile.absolutePath }.get()
        val dokkaJavadocDir = buildDirectory.map { it.dir("javadoc").asFile.absolutePath }.get()

        val remoteDestination = "root@stomach:/mnt/web/var/www/hub.jeff-media.com/javadocs/basics-core/"

        if (file(dokkaHtmlDir).exists() && file(dokkaJavadocDir).exists()) {
            executable = "sh"
            args =
                listOf(
                    "-c",
                    """
                    rsync -avz --progress $dokkaHtmlDir $remoteDestination &&
                    rsync -avz --progress $dokkaJavadocDir $remoteDestination
                    """.trimIndent(),
                )
        } else {
            error("Dokka outputs not found: $dokkaHtmlDir, $dokkaJavadocDir")
        }
    }
}
