
plugins {
    kotlin("jvm") version "1.9.22"
    id("application")
}

group = "me.timyang"
version = "1.0-SNAPSHOT"

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks {
    test {
        useTestNG()
    }
}

application {
    mainClass = "MainKt"
}