// The 'plugins' block defines which plugins to apply.
// We are applying them inside 'subprojects' to avoid applying them to the root project itself.
plugins {
    // The base plugin provides basic Gradle tasks but doesn't assume any language.
    base
}


// The 'allprojects' block applies settings to ALL modules, including the root project.
allprojects {
    // Set the group and version for all modules. Replaces <groupId> and <version>.
    group = "com.github.xef5000"
    version = "1.1.1"

    // Define the repositories for all modules. Replaces the <repositories> section.
    // By defining them here, you don't need to repeat them in every child module.
    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.nexomc.com/releases")
        maven("https://repo.alessiodp.com/releases/")
        maven("https://maven.devs.beer/")
        maven("https://repo.oraxen.com/releases")
        maven("https://nexus.phoenixdevt.fr/repository/maven-public/")
        maven(url = "https://mvn.lumine.io/repository/maven-public/")
    }
}

// The 'subprojects' block applies settings only to the child modules (api, plugin, nms-*).
subprojects {
    // Apply the Java plugin to every sub-project.
    // This makes them Java projects capable of compiling Java code.
    apply(plugin = "java")

    // Now, configure the 'java' extension which was just applied.
    // We use 'extensions.configure<JavaPluginExtension>' for type-safe access.
    extensions.configure<JavaPluginExtension> {
        // Set the Java version for the compiler. Replaces <java.version>.
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    // You can also set source encoding here if needed.
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}