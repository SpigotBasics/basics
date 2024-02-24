import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.version

plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
}

dependencies {
    compileOnly(project(":nms:facade"))
}

tasks.assemble {
    dependsOn(tasks.getByName("reobfJar"))
}
