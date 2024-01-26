plugins {
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
}

dependencies {
    implementation(project(":lib"))
    ksp(project(":processor"))
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}