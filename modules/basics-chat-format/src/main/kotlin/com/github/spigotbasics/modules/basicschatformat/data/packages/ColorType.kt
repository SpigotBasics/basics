package com.github.spigotbasics.modules.basicschatformat.data.packages

enum class ColorType(val colorPackage: ChatColorPackage) {
    GRADIENT_NAMED(GradientNamedPackage),
    GRADIENT_HEX(GradientHexPackage),
    NAMED_COLOR(NamedColorPackage),
    HEX_COLOR(HexColorPackage),
}
