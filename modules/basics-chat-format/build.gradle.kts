plugins {
    id("basics.module")
    id("basics.dependency.paper-api")
}

dependencies {
    compileOnly(project(":pipe:facade"))
    compileOnly(project(":pipe:paper"))
}