plugins {
    kotlin("jvm") version "1.9.23"
}

allprojects {
    group = "hello.haha"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    dependencies {
        implementation(kotlin("stdlib"))
        testImplementation(kotlin("test"))
        testImplementation("io.kotest:kotest-runner-junit5:5.8.1")
        testImplementation("io.kotest:kotest-framework-datatest:5.8.1")
    }

    tasks.test { useJUnitPlatform() }
    kotlin { jvmToolchain(17) }
}
