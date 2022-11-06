import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.8"

    kotlin("jvm") version "1.7.10"
}

group = "ru.ezhov"
version = "1.0-SNAPSHOT"

javafx {
    version = "11.0.2"
    modules = listOf("javafx.controls", "javafx.graphics")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.atlassian.com/maven/repository/public")
    }
}

dependencies {
    implementation("no.tornado:tornadofx:1.7.20")

    implementation("io.github.microutils:kotlin-logging-jvm:2.0.10")

    implementation("com.atlassian.jira:jira-rest-java-client-core:5.2.4")
    implementation("io.atlassian.fugue:fugue:5.0.0")


    implementation("io.arrow-kt:arrow-core:0.13.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

    implementation("org.yaml:snakeyaml:1.21")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.3")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
