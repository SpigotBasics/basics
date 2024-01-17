plugins {
    id("basics.module")
}

dependencies {
    // Test dependency - This should get shaded without further configuration.
    // https://mvnrepository.com/artifact/org.yaml/snakeyaml
    implementation("org.yaml:snakeyaml:2.2")

}