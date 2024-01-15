plugins {
    id("basics.kotlin-conventions")
}

// For now, the root module depends on all subprojects
// Later we'll only depend on core and a few selected modules
dependencies {
    rootProject.subprojects.forEach { subproject ->
        subproject.plugins.withId("kotlin") {
            api(subproject)
        }
    }
}