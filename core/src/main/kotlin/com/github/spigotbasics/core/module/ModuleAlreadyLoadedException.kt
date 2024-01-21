package com.github.spigotbasics.core.module

class ModuleAlreadyLoadedException(moduleInfo: ModuleInfo): Exception("Module ${moduleInfo.name} is already loaded") {
}