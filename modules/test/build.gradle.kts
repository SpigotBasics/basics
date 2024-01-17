plugins {
    id("basics.module")
}

dependencies {
    // Test dependency - This should get shaded without further configuration.
    implementation("org.yaml:snakeyaml:2.2")

}