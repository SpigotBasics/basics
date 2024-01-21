package com.github.spigotbasics.core.module

class ForbiddenFruitException(clazzName: String) :
    Exception("Module tried to eat forbidden fruit $clazzName") {
}