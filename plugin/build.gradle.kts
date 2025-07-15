import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

/*
 * This is the build script for the 'plugin' module.
 * It assembles the final shadable JAR file by combining the 'api' and 'nms' modules
 * and shading any required libraries.
 */

plugins {
    // The regular Java plugin is fine here.
    java
    // This is the Gradle equivalent of the Maven Shade Plugin.
    id("com.gradleup.shadow") version "8.3.0"
    `maven-publish`
}

// Set a custom name for the final JAR file, replacing <finalName>
tasks.withType<ShadowJar> {
    archiveBaseName.set("ItemsAPI")
    archiveClassifier.set("") // Set classifier to empty to avoid "all" or "shadow" suffix
    archiveVersion.set(project.version.toString())
}

dependencies {
    // === LOCAL MODULES ===
    // 'implementation' is used for dependencies that should be bundled.
    // This tells Gradle to include the code from these modules in the final JAR.
    implementation(project(":api"))
    implementation(project(":nms:nms-legacy"))
    implementation(project(":nms:nms-1_20_5"))

    // === EXTERNAL APIs ===
    // 'compileOnly' is used for APIs provided by the server or other plugins.
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("com.nexomc:nexo:1.0.0")
    compileOnly("dev.lone:api-itemsadder:4.0.10")
    compileOnly("net.Indyuce:MMOItems-API:6.9.5-SNAPSHOT")

    compileOnly("io.lumine:MythicLib-dist:1.6.2-SNAPSHOT")
    compileOnly("io.lumine:Mythic-Dist:5.6.1")

    // For dependencies with exclusions
    compileOnly("io.th0rgal:oraxen:1.190.0") {
        // 'exclude' is much cleaner in Gradle.
        exclude(group = "me.gabytm.util", module = "actions-spigot")
        exclude(group = "org.jetbrains", module = "annotations")
        exclude(group = "com.ticxo", module = "PlayerAnimator")
        exclude(group = "com.github.stefvanschie.inventoryframework", module = "IF")
        exclude(group = "io.th0rgal", module = "protectionlib")
        exclude(group = "dev.triumphteam", module = "triumph-gui")
        exclude(group = "org.bstats", module = "bstats-bukkit")
        exclude(group = "com.jeff-media") // You can exclude a whole group
    }

    compileOnly("com.arcaniax:HeadDatabase-API:1.3.2")

    compileOnly("maven.modrinth:SCore:5.25.7.12")

    compileOnly("me.zombie_striker:QualityArmory:2.0.21")

    compileOnly("xyz.xenondevs.nova:nova-api:0.19")

    compileOnly("com.willfp:libreforge:4.75.1:all") {
        // Equivalent to <exclusions><exclusion>*:*</exclusion></exclusions>
        exclude(group = "*", module = "*")
    }

    compileOnly("com.willfp:eco:6.75.2")

    compileOnly("com.bgsoftware:WildToolsAPI:2025.1")

    // === SHADED LIBRARIES ===
    // Use 'implementation' for libraries you want to shade into your JAR.
    //implementation("net.byteflux:libby-bukkit:1.3.1")
}

tasks.build {
    // This task is used to build the final JAR file.
    // It depends on the ShadowJar task to ensure it runs after shading.
    dependsOn(tasks.shadowJar)
}

// Configure the ShadowJar task (replaces <configuration> in maven-shade-plugin)
tasks.shadowJar {
    // Relocate Libby to avoid conflicts with other plugins.
    relocate("net.byteflux.libby", "com.github.xef5000.libs.net.byteflux.libby")
}

publishing {
    publications {
        // You can name this anything, "shadow" is a good convention
        create<MavenPublication>("shadow") {
            // The group and version are inherited from the root project
            // The artifactId is the name of this module's directory by default
            // Tell it to publish the output from the shadowJar task
            artifact(tasks.shadowJar)
        }
    }
    repositories {
        // This block is optional but good practice. It defines where you could publish to.
        // JitPack doesn't use this, but it completes the configuration.
        maven {
            name = "local"
            url = uri("file://${buildDir}/repo")
        }
    }
}