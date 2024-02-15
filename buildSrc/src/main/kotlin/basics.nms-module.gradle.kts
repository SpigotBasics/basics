import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.version

plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
}

dependencies {
    compileOnly(project(":nms:facade"))
    //paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
}

tasks.assemble {
    dependsOn(tasks.getByName("reobfJar"))
}
