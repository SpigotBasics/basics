tasks.register("copyAllModulesToTestServer") {
    group = "test server"
    description = "Copies all modules to the test server"
    val copyAllModulesTask = this
    subprojects.forEach { module ->
        module.tasks.withType(CopyModule::class).forEach { copyModuleTask ->
            println("${copyAllModulesTask.name} dependsOn ${copyModuleTask.name}")
            copyAllModulesTask.dependsOn(copyModuleTask)
        }
    }
}