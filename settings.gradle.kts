// This block configures where Gradle can find plugins, like paperweight.
// It's required for the nms modules later on.
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

// Sets the name of the root project.
rootProject.name = "ItemsAPI"

// Includes all the sub-modules. The ':' character is used for nested directories.
// This is the direct replacement for the <modules> section in your POM.
// I've anticipated that nms-legacy will exist alongside nms-1_20_5.
include("api")
include("plugin")
include("nms:nms-legacy")
include("nms:nms-1_20_5")