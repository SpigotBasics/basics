plugins {
    `java-library`
}


dependencies {
    compileOnlyApi(libs().placeholderapi) {
        isTransitive = false
    }
}