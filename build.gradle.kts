import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val project_group: String by extra
val project_version: String by extra

plugins {
    kotlin("jvm") version "1.4.31" apply false
    kotlin("plugin.spring") version "1.4.31" apply false
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")

    buildscript { repositories { mavenCentral() } }
    repositories { mavenCentral() }

    group = project_group
    version = project_version

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        withSourcesJar()
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305strict")
            jvmTarget = "11"
        }
    }
    tasks.withType<Test>().all {
        useJUnitPlatform()
    }
}

