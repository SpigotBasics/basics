plugins {
    `kotlin-dsl` // buildSrc scripts are using kotlin-dsl
}

repositories {
    mavenCentral() // kotlin-dsl requires stdlib
}