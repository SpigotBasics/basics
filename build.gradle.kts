plugins {
    id("basics.kotlin-conventions")
    id("com.github.johnrengelman.shadow") apply false
}

tasks.register("copyAllToTestServer") {
    group = "testserver"
    description = "Copies the plugin and all modules to the test server"
    dependsOn("plugin:copyPluginToTestServer")
    dependsOn("modules:copyAllModulesToTestServer")
}