# ItemsAPI
Easily manage items in config

# Developers
Replace "VERSION" with the Jitpack version

[![](https://jitpack.io/v/xef5000/ItemsAPI.svg)](https://jitpack.io/#xef5000/ItemsAPI)

__Gradle__:
```groovy
plugins {
  id 'com.github.johnrengelman.shadow' version '8.1.1'
}

repositories {
  mavenCentral()
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.xef5000:ItemsAPI:VERSION'
}

tasks {
    shadowJar {
        // Replace "com.yourplugin" with your plugin's package name.
        relocate "com.github.xef5000.itemsapi", "com.yourplugin.libs.itemsapi"
        archiveClassifier.set('')
    }

    build {
        dependsOn shadowJar
    }
}
```
