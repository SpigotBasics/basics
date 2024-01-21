import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    `java-library`
}


dependencies {
    api(libs().acf.paper)
}