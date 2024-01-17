tasks.register("copyAllModulesToTestServer") {
    group = "testserver"
    description = "Copies all modules to the test server"
    val copyAllModulesTask = this
    subprojects.forEach { module ->
        module.tasks.withType(CopyModule::class).forEach { copyModuleTask ->
            copyAllModulesTask.dependsOn(copyModuleTask)
        }
    }
}

tasks.register("createModule", CreateModule::class) {
    group = "module"
    description = "Creates a new module"
}