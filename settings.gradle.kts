plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "actors"
include("lib")
include("processor")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("gradle/versions.toml"))
        }
    }
}
include("example")
include("base")
