plugins {
    `java-library`
    id("basics.kotlin-conventions")
    id("basics.dependency.paper-api")
}

dependencies {
    implementation(project(":pipe:facade"))
}
