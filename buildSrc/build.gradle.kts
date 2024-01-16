plugins {
    `kotlin-dsl` // buildSrc scripts are using kotlin-dsl
}

repositories {
    mavenCentral() // kotlin-dsl requires stdlib
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
}