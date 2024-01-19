plugins {
    `java-library`
}

dependencies {
    api(libs("acf-paper"))
}

tasks.compileJava {
    options.compilerArgs.add("-parameters")
    options.isFork = true
}