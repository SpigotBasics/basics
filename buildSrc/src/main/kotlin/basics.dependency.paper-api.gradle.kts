import org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
}

dependencies {
    compileOnly(libs().paper.api)
}

java.disableAutoTargetJvm()

tasks.withType<KotlinCompile>().configureEach {
    this.jvmTargetValidationMode = JvmTargetValidationMode.WARNING
}