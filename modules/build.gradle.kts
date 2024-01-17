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

//val createModuleTask = tasks.register("createModule") {
//    var moduleName: String?
//    val moduleInputPrompt = "Enter module name. Use only lowercase letters, numbers, underscores and dashes (${moduleNameRegex.pattern}): "
//    moduleName = readInput(moduleInputPrompt)
//    if(!isValidModuleName(moduleName)) {
//        error("Invalid module name: $moduleName")
//    }
//    println("Creating module $moduleName ...")
//}

tasks.register("createModule", CreateModule::class) {
    group = "module"
    description = "Creates a new module"
}