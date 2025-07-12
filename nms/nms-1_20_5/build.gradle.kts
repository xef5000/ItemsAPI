/*
 * This is the build script for the 'nms-1_20_5' module.
 * It uses the Paperweight toolchain to provide NMS access for modern versions.
 */

plugins {
    // Apply the official Paper plugin for NMS development.
    // This plugin automatically handles downloading the server JAR and remapping your code.
    id("io.papermc.paperweight.userdev") version "1.7.1"
    `java-library`
}



dependencies {
    // This module needs access to the interfaces from the 'api' module.
    implementation(project(":api"))

   // paperweight.paperDevBundle("1.20.5-R0.1-SNAPSHOT")

    paperDevBundle("1.20.6-R0.1-SNAPSHOT")

    // You only need to add other dependencies if you use them directly.
    compileOnly("com.google.code.gson:gson:2.10.1")
}