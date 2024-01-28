plugins {
    `java-library`
    id("basics.kotlin-conventions")
    id("basics.dependency.spigot-api")
}

dependencies {
    implementation(project(":pipe:facade"))
}