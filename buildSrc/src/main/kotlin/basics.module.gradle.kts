/**
 * Used for all modules
 */

plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    `java-library`
}

dependencies {
    compileOnly(project(":core"))
}