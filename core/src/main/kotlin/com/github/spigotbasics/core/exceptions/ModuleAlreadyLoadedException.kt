package com.github.spigotbasics.core.exceptions

import com.github.spigotbasics.core.module.ModuleInfo

class ModuleAlreadyLoadedException(moduleInfo: ModuleInfo) : Exception("Module ${moduleInfo.name} is already loaded")
