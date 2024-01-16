plugins {
    id("basics.kotlin-conventions")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

// For now, the root module depends on all subprojects
// Later we'll only depend on core, plugin and a few selected modules
dependencies {
    rootProject.subprojects.forEach { subproject ->
        subproject.plugins.withId("kotlin") {
            api(subproject)
        }
    }
}
