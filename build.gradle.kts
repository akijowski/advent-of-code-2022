plugins {
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.serialization") version "1.7.21"
}

repositories {
    mavenCentral()
}

dependencies {
    // Day 13 helper
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "7.6"
    }
}
