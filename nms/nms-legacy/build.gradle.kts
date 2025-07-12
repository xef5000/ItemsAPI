/*
 * This is the build script for the 'nms-legacy' module.
 * It provides the NMS adapter for older server versions (pre-1.20.5).
 */

plugins {
    `java-library`
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    // This module needs access to the interfaces defined in the 'api' module.
    // 'implementation' is correct because the 'plugin' module will need this code compiled and bundled.
    implementation(project(":api"))

    // 'compileOnly' is used because the server will provide the Spigot API at runtime.
    // We define the specific legacy version this module compiles against.
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
}

// compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")