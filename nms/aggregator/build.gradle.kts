plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    compileOnly(project(":nms:facade"))
}

tasks.shadowJar {
    dependencies {
        exclude("kotlin/**")
        exclude("org/**")
    }
}

val nmsVersionProjects = project(":nms:versions").subprojects

// Sometimes Gradle thinks the "build" folder is a subproject, so check by regex for "1.20.4" etc
val nmsRegex = Regex("""\d+\.\d+(\.\d+)?""")

dependencies {
    nmsVersionProjects.forEach { project ->
        if (project.name.matches(nmsRegex)) {
            implementation(project(project.path, "reobf"))
        }
    }
}
