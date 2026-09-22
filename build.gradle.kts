import xyz.jpenilla.runpaper.task.RunServer

plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.1.0"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
    disableAutoTargetJvm()
}

tasks.withType<JavaCompile> {
    options.release.set(8)
}

val java8 = javaToolchains.launcherFor {
    languageVersion = JavaLanguageVersion.of(8)
}

val pluginJar = tasks.jar.flatMap { it.archiveFile }

tasks {
    runServer {
        minecraftVersion("26.2")
        jvmArgs("-Xms2G", "-Xmx2G")
    }

    register<RunServer>("runServer1_8_8") {
        minecraftVersion("1.8.8")
        javaLauncher.set(java8)
        pluginJars.from(pluginJar)
        jvmArgs("-Xms2G", "-Xmx2G")
    }

    processResources {
        val props = mapOf("version" to version, "description" to project.description)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}
