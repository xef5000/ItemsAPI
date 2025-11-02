# ItemsAPI
Easily manage items in config

Made for 1.18+

# Developers
Requires Java 17

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

__Maven__:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.yourplugin</groupId>
    <artifactId>your-artifact</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <!-- Replace VERSION with actual version -->
        <dependency>
            <groupId>com.github.xef5000</groupId>
            <artifactId>ItemsAPI</artifactId>
            <version>VERSION</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.5.1</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <!-- Replace with your actual base package -->
                            <relocations>
                                <relocation>
                                    <pattern>com.github.xef5000.itemsapi</pattern>
                                    <shadedPattern>com.yourplugin.libs.itemsapi</shadedPattern>
                                </relocation>
                            </relocations>
                            <shadedArtifactAttached>false</shadedArtifactAttached>
                            <finalName>${project.artifactId}</finalName>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```
