plugins {
    // The 'java-library' plugin is the best practice for modules that expose an API
    // for other modules to use. It's an extension of the regular 'java' plugin.
    `java-library`
}

dependencies {
    // 'compileOnly' is the Gradle equivalent of Maven's '<scope>provided</scope>'.
    // It means this dependency is needed for compiling but will be provided by the server at runtime.
    // We explicitly define the version for the oldest API we want this module to support.
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
}