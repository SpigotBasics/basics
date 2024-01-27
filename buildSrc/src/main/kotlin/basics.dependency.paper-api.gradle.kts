import org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
}

repositories {
    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
        name = "papermc"
    }
}

dependencies {
    compileOnly(libs().paper.api)
}

java.disableAutoTargetJvm()

tasks.withType<KotlinCompile>().configureEach {
    this.jvmTargetValidationMode = JvmTargetValidationMode.WARNING
}