package com.github.spigotbasics.nms

class UnsupportedNMSFacade : NMSFacade {
    override fun getTps(): DoubleArray {
        throw NMSNotSupportedException()
    }
}
