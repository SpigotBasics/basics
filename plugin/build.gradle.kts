plugins {
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
    `java-library`
}

dependencies {
    implementation(project(":core"))
}