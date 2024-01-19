plugins {
    `java-library`
}

dependencies {
    api("co.aikar:acf-paper:0.5.1-SNAPSHOT")
}

tasks.compileJava {
    options.compilerArgs.add("-parameters")
    options.isFork = true
}