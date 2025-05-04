plugins {
    kotlin("jvm") version "2.1.10"
    id("com.gradleup.shadow") version "9.0.0-beta13"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "com.bruhdows"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
    compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin:2.19.0")
    compileOnly("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.19.0")
}

kotlin {
    jvmToolchain(17)
}

tasks.jar {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    enableRelocation = false
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
    }
}

tasks {
    runServer {
        minecraftVersion("1.20.4")
    }
}

tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
    javaLauncher = javaToolchains.launcherFor {
        vendor = JvmVendorSpec.JETBRAINS
        languageVersion = JavaLanguageVersion.of(21)
    }
    jvmArgs("-XX:+AllowEnhancedClassRedefinition")
}