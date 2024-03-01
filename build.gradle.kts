plugins {
    kotlin("jvm") version "1.9.21"
}

group = "com.bethibande.actors"
version = "1.0"

fun Project.applyRepos() {
    repositories {
        mavenLocal()
        maven("https://maven.bethibande.com/mirror")
        mavenCentral()
    }
}

applyRepos()

subprojects {
    applyRepos()

    apply(plugin = "org.jetbrains.kotlin.jvm")

    kotlin {
        jvmToolchain(17)
    }
}
