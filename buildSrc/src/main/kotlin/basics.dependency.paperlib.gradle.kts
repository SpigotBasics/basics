import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.`java-library`

plugins {
    `java-library`
}


dependencies {
    api(libs().paperlib)
}