plugins {
    java
    id("io.izzel.taboolib") version "1.31"
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
}

group = "io.github.thehrz.snowcraft"
version = "2.0"

taboolib {
    description {
        contributors {
            name("Thehrz")
        }
        dependencies {
            name("Slimefun").optional(false)
            name("CS-CoreLib").optional(false)
        }
    }
    install(
        "common",
        "common-5",
        "module-ui",
        "module-nms",
        "module-nms-util",
        "module-chat",
        "module-lang",
        "module-metrics",
        "module-configuration",
        "platform-bukkit"
    )

    classifier = null
    version = "6.0.4-5"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(fileTree("libs"))
    compileOnly("ink.ptms.core:v11200:11200:all")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}